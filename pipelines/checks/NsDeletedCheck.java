// Read-only check: can this role read deleted records? Prints counts and table names only.
// Derived from the seed export from NetSuite SuiteAnalytics Connect (NetSuite2.com).
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

public class NsDeletedCheck {
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

    Class.forName("com.netsuite.jdbc.openaccess.OpenAccessDriver");
    System.out.println("Connecting ...");
    try (Connection c = DriverManager.getConnection(url, p)) {
      System.out.println("Connected.");
      String[] qs = {
        "SELECT COUNT(*) FROM deletedrecord",
        "SELECT MIN(deleteddate), MAX(deleteddate) FROM deletedrecord",
        "SELECT type, COUNT(*) FROM deletedrecord GROUP BY type",
        "SELECT table_name FROM OA_TABLES WHERE LOWER(table_name) LIKE '%delet%'",
        "SELECT column_name FROM OA_COLUMNS WHERE LOWER(table_name) = 'deletedrecord'"
      };
      for (String q : qs) {
        System.out.println("QUERY: " + q);
        try (Statement s = c.createStatement(); ResultSet rs = s.executeQuery(q)) {
          int k = rs.getMetaData().getColumnCount(); int n = 0;
          while (rs.next() && n++ < 60) {
            StringBuilder b = new StringBuilder("  ");
            for (int i = 1; i <= k; i++) b.append(rs.getString(i)).append(i < k ? " | " : "");
            System.out.println(b);
          }
          System.out.println("  OK");
        } catch (Throwable e) { System.out.println("  FAILED " + e.getClass().getSimpleName() + ": " + e.getMessage()); }
      }
    } catch (Throwable e) {
      System.out.println("RESULT: FAILED to connect.");
      for (Throwable t = e; t != null; t = t.getCause()) System.out.println("  " + t.getClass().getName() + ": " + t.getMessage());
      System.exit(1);
    }
    System.out.println("RESULT: done. Copy everything above back to Claude.");
  }
}
