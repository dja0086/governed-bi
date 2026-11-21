# S-03a: first NetSuite ingestion attempt (2026-11-20)

Status: FAILED at schema exploration. Cause not yet isolated.

## What was set up (verified on screen)

| Item | Value |
|---|---|
| Databricks connection | `ns_tstdrv`: host `tstdrv1755417.connect.api.netsuite.com`, port 1708, data source NetSuite2.com, account `TSTDRV1755417`, role id `1453` |
| Driver | `/Volumes/client_one/bronze/netsuite_jars/NQjc.jar`, version 8.10.190.0 (sha256 matches NetSuite's `versions.txt`) |
| Pipeline | `netsuite_client_one_ingest`, id `74ba6a9d-9246-459c-a114-f57ca947654a`, event log in `client_one.bronze` |
| NetSuite's own configuration page | Same host, port, data source and account. Driver offered: 8.10.190.0 only |

## Result

Source step, twice (one retry): "The source database rejected the schema-exploration query (SQLNonTransientConnectionException: [NetSuite][SuiteAnalytics Connect JDBC Driver][OpenAccess SDK SQL Engine] Internal network error, connection closed.)"

NetSuite's Login Audit Trail for the same minutes shows no failed login and no login under role 1453 from a new address (verified by reading the newest rows; all were Success under NS Agent Integration, Administrator or Power User from already-seen addresses). Unverified inference: the session ended before or during authentication.

## Two departures from Databricks' guide

1. Role id. The guide says to enter the internal ID of the Data Warehouse Integrator role. The connection has 1453 ("Power User").
2. Driver version. The guide lists 8.10.147.0, 8.10.170.0 and 8.10.184.0 as supported. NetSuite now offers only 8.10.190.0.

## Test that separates them

Change only the role: give the user the Data Warehouse Integrator role, issue the access token under it, put that role's internal ID and the new token in the connection, retry. If it then works, the role was the cause (and the token is on a read-only role, which also closes O-015). If the same error remains, the driver version is the remaining suspect and goes to Databricks support.
