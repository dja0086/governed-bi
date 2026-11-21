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

## Follow-up, same day: what the account console shows (read on screen)

| Item | Value |
|---|---|
| Plan | Premium. Enterprise is offered as an upgrade |
| Credit | $400 trial credit, $392 remaining, expires 2026-12-04. No payment method on file |
| Workspace | `workspace`, us-east-2, "Serverless only", default storage |
| Security, Networking | Private endpoints, Network connectivity configurations, Classic network configurations, Private access settings. No "Network policies" entry, so option 1 is not available on this account |
| Create workspace | Two choices: serverless with default storage, or "Use your existing cloud account" (needs the owner's AWS account; supports custom compute) |

Databricks' documentation says serverless egress network policies require the Enterprise tier. That matches the missing menu entry.

Open point on option 3: the NetSuite connector requires serverless pipelines. Whether serverless egress is unrestricted in a workspace created on the owner's own AWS account, inside this same trial account, is unverified. A classic-compute workspace alone would not help if the connector's serverless compute stays restricted.

Owner decision 2026-11-20: no Snowflake (D-025).

## NetSuite side proven independently (2026-11-20)

A login test run by the owner from their own computer, outside Databricks, with the same host, port, account, role 57 and the same four token values, using the same driver file (NQjc.jar 8.10.190.0): RESULT SUCCESS. It logged in with token-based authentication and ran `SELECT COUNT(*) FROM subsidiary` on NetSuite2.com. Source: the owner's report of the script's output; the builder never saw the secrets. Not cross-checked in the Login Audit Trail.

What this settles: SuiteAnalytics Connect is live on the account (closes the open part of D-019), token-based authentication, the integration record, the token, role 57 and driver 8.10.190.0 all work. The only remaining fault is Databricks' serverless egress.

Also on 2026-11-20: the owner added a payment method to the Databricks account (the workspace header changed from "Manage Trial" to "Credits"). A re-test minutes later still could not resolve netsuite.com hosts. Databricks' trial documentation lists "Limited external network access" as a trial limitation and says adding payment information removes trial limitations.

## Re-test 2026-11-21 03:21 UTC (about 40 minutes after the payment method was added)

Verified, notebook cell 2 re-run on serverless: `pypi.org` resolves; `www.netsuite.com`, `system.netsuite.com` and the three `tstdrv1755417.*.netsuite.com` hosts return no address. The block is unchanged. The pipeline's Source step was not retried, because it cannot succeed without name resolution. Modelling is not blocked: see `S-03c-seed-load.md`.

## New account on a business email (2026-11-21)

Workspace 7474653267700225, created by the owner with express setup on a company-domain email.

- Verified 12:33 UTC, still a trial: `pypi.org` resolves; `example.com`, `www.oracle.com` and five NetSuite hosts return no address; TCP to the Connect host on 1708 fails.
- Verified 12:45 UTC, after the owner added a payment method (the "Manage Trial" button is gone): same result, on a fresh run and again in a new serverless session.
- Verified: the account console's Security > Networking has no egress policy page (Private endpoints, Network connectivity configurations, Classic network configurations, Private access settings only).

Conclusion: neither a business email nor a payment method lifts the restriction by itself within minutes. This matches two Databricks Community reports (threads 174117 and 173281), where a Databricks employee says the account team has to verify the account. Unverified: whether the restriction clears with time, and whether verification works; no poster confirmed an outcome.

The old account could not be re-tested in the same browser session (signed in to the new account).
