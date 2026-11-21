// One-time, read-only seed export from NetSuite SuiteAnalytics Connect (NetSuite2.com).
// Writes one gzipped JSON-lines file per table into ./seed_out plus _manifest.json.
// Secrets come from environment variables set by ns_seed_export.sh; never printed or saved.
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.*;
import java.util.*;
import java.util.zip.GZIPOutputStream;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class NsSeedExport {
  static final String[] TABLES = {
    "subsidiary", "accountingbook", "accountingperiod", "currency", "account",
    "department", "classification", "location", "item", "customer", "vendor",
    "consolidatedexchangerate", "transaction", "transactionline", "transactionaccountingline"
  };
  static String need(String k) {
    String v = System.getenv(k);
    if (v == null || v.trim().isEmpty()) { System.out.println("Missing value: " + k); System.exit(2); }
    return v.trim();
  }
  static String esc(String s) {
    StringBuilder b = new StringBuilder(s.length() + 8);
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch (c) {
        case '"': b.append("\\\""); break;
        case '\\': b.append("\\\\"); break;
        case '\n': b.append("\\n"); break;
        case '\r': b.append("\\r"); break;
        case '\t': b.append("\\t"); break;
        default: if (c < 0x20) b.append(String.format("\\u%04x", (int) c)); else b.append(c);
      }
    }
    return b.toString();
  }
  static boolean numeric(int t) {
    return t == Types.NUMERIC || t == Types.DECIMAL || t == Types.INTEGER || t == Types.BIGINT
        || t == Types.DOUBLE || t == Types.FLOAT || t == Types.REAL || t == Types.SMALLINT || t == Types.TINYINT;
  }
  public static void main(String[] a) throws Exception {
    String host = need("NS_HOST"), port = need("NS_PORT"), acct = need("NS_ACCOUNT"), role = need("NS_ROLE");
    String ck = need("NS_CONSUMER_KEY"), cs = need("NS_CONSUMER_SECRET");
    String tid = need("NS_TOKEN_ID"), tsec = need("NS_TOKEN_SECRET");
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    SecureRandom r = new SecureRandom();
    StringBuilder nonce = new StringBuilder();
    for (int i = 0; i < 20; i++) nonce.append(chars.charAt(r.nextInt(chars.length())));
    long ts = System.currentTimeMillis() / 1000L;
    String base = acct + "&" + ck + "&" + tid + "&" + nonce + "&" + ts;
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(new SecretKeySpec((cs + "&" + tsec).getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
    String sig = Base64.getEncoder().encodeToString(mac.doFinal(base.getBytes(StandardCharsets.UTF_8)));
    Properties p = new Properties();
    p.setProperty("user", "TBA");
    p.setProperty("password", base + "&" + sig + "&HMAC-SHA256");
    String url = "jdbc:ns://" + host + ":" + port + ";ServerDataSource=NetSuite2.com;Encrypted=1;"
        + "NegotiateSSLClose=false;CustomProperties=(AccountID=" + acct + ";RoleID=" + role + ")";

    File out = new File("seed_out");
    out.mkdirs();
    StringBuilder manifest = new StringBuilder("{\"account\":\"" + acct + "\",\"exported_at_epoch\":" + ts + ",\"tables\":[");
    int ok = 0, failed = 0;
    Class.forName("com.netsuite.jdbc.openaccess.OpenAccessDriver");
    System.out.println("Connecting ...");
    try (Connection c = DriverManager.getConnection(url, p)) {
      System.out.println("Connected. Exporting " + TABLES.length + " tables. Large ones can take several minutes.");
      for (String t : TABLES) {
        long n = 0; String err = null; StringBuilder cols = new StringBuilder();
        File f = new File(out, t + ".jsonl.gz");
        try (Statement s = c.createStatement()) {
          s.setFetchSize(1000);
          try (ResultSet rs = s.executeQuery("SELECT * FROM " + t);
               Writer w = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(f)), StandardCharsets.UTF_8))) {
            ResultSetMetaData md = rs.getMetaData();
            int k = md.getColumnCount();
            String[] names = new String[k]; boolean[] num = new boolean[k];
            for (int i = 0; i < k; i++) {
              names[i] = md.getColumnLabel(i + 1).toLowerCase();
              num[i] = numeric(md.getColumnType(i + 1));
              if (i > 0) cols.append(',');
              cols.append('"').append(esc(names[i])).append('"');
            }
            StringBuilder line = new StringBuilder();
            while (rs.next()) {
              line.setLength(0); line.append('{'); boolean first = true;
              for (int i = 0; i < k; i++) {
                String v;
                if (num[i]) { BigDecimal d = rs.getBigDecimal(i + 1); v = d == null ? null : d.toPlainString(); }
                else { String x = rs.getString(i + 1); v = x == null ? null : "\"" + esc(x) + "\""; }
                if (v == null) continue;
                if (!first) line.append(','); first = false;
                line.append('"').append(esc(names[i])).append("\":").append(v);
              }
              line.append("}\n"); w.write(line.toString()); n++;
              if (n % 20000 == 0) System.out.println("  " + t + ": " + n + " rows so far");
            }
          }
        } catch (Throwable e) { err = e.getClass().getSimpleName() + ": " + e.getMessage(); }
        if (err == null) { ok++; System.out.println("OK      " + t + "  rows=" + n); }
        else { failed++; f.delete(); System.out.println("FAILED  " + t + "  " + err); }
        if (manifest.charAt(manifest.length() - 1) != '[') manifest.append(',');
        manifest.append("{\"table\":\"").append(t).append("\",\"rows\":").append(n)
            .append(",\"ok\":").append(err == null).append(",\"error\":")
            .append(err == null ? "null" : "\"" + esc(err) + "\"").append(",\"columns\":[").append(cols).append("]}");
      }
    } catch (Throwable e) {
      System.out.println("RESULT: FAILED to connect.");
      for (Throwable t = e; t != null; t = t.getCause()) System.out.println("  " + t.getClass().getName() + ": " + t.getMessage());
      System.exit(1);
    }
    manifest.append("]}");
    try (Writer w = new OutputStreamWriter(new FileOutputStream(new File(out, "_manifest.json")), StandardCharsets.UTF_8)) { w.write(manifest.toString()); }
    System.out.println("RESULT: " + ok + " tables exported, " + failed + " failed. Files are in " + out.getAbsolutePath());
  }
}
