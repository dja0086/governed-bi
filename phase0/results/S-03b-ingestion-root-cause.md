# S-03b: root cause of the NetSuite ingestion failure (2026-11-20)

Error under investigation (Databricks, pipeline `netsuite_client_one_ingest`, Source step):
"The source database rejected the schema-exploration query (SQLNonTransientConnectionException: [NetSuite][SuiteAnalytics Connect JDBC Driver][OpenAccess SDK SQL Engine] Internal network error, connection closed.)"

## Finding

Serverless compute in this Databricks trial workspace cannot resolve any `netsuite.com` hostname. Outbound access is limited to an allowlist enforced at DNS. The connection never reaches NetSuite.

Confidence: high for serverless notebook compute (measured). One link is inferred, not measured: that the ingestion pipeline's serverless compute sits under the same egress policy. Everything observed is consistent with it.

## Evidence, Databricks side (measured in a serverless notebook, same workspace)

| Test | Result |
|---|---|
| Resolve `tstdrv1755417.connect.api.netsuite.com` | Fails: `gaierror: [Errno -2] Name or service not known` |
| Resolve `www.netsuite.com`, `system.netsuite.com`, `tstdrv1755417.app.netsuite.com`, `tstdrv1755417.suitetalk.api.netsuite.com` | All fail (no address returned) |
| Resolve `example.com`, `www.oracle.com`, `www.google.com` | All fail |
| Resolve `pypi.org`, `files.pythonhosted.org`, `github.com` | Succeed with public addresses |
| Resolve `api.salesforce.com` | Succeeds, to an internal address (192.168.200.20), i.e. routed through a Databricks-side proxy |
| Resolver in use | `nameserver 192.168.200.6` (internal) |
| Pipeline event log | Two INFO rows only; nothing about schema exploration |
| `system.access.outbound_network` filtered on "netsuite" | 0 rows at the time of the query. Not conclusive: the table can lag |

## Evidence, NetSuite side

| Check | Result |
|---|---|
| Host, port, data source, account ID on NetSuite's SuiteAnalytics Connect page | Match the Databricks connection exactly |
| Login Audit Trail, newest rows, after three attempts (two under role 1453, one under role 57) | No failed login. No login under Data Warehouse Integrator. No new source address. Only the usual sessions (NS Agent Integration, Administrator, Power User, SR Agent Proposer), all Success |
| Role 57 in the connection (changed by the owner from 1453) | Same error, so the role was not the cause |

## Ruled out

- Role or token: changing the role changed nothing, and NetSuite recorded no attempt at all.
- Driver version 8.10.190.0: a Databricks employee states on the community forum that it is good for production (thread "NetSuite JDBC Driver 8.10.190.0 - Databricks support"). Not independently tested here, because the connection never gets that far.
- Wrong host or port: they match NetSuite's own page.

## Prior reports of the same thing

- Databricks community, "Serverless outbound internet still restricted after upgrading express setup trial to Premium": an express-setup trial reaches pypi.org and github.com but other lookups are refused at DNS. A Databricks employee says the restriction has to be cleared by Databricks on the backend and suggests a request through help.databricks.com. No accepted solution in the thread.
- Databricks documentation, "Manage network policies for serverless egress control": in Restricted access mode only listed domains are reachable; policies are managed by account admins.
- Other published causes of this driver message (outdated cipher suite; port blocked; long-query timeouts) do not fit: here the name does not resolve at all.

## Options (owner decides; some cost money)

1. Check the account console (Security, Networking, Network policies) for a policy that can be set to full access or have `*.netsuite.com` added.
2. Ask Databricks to lift the trial egress restriction (help.databricks.com; include workspace ID 7474655419666822 and this evidence).
3. Use a workspace that is not an express-setup trial.
4. Run the Snowflake arm of the bake-off first while this is resolved.
