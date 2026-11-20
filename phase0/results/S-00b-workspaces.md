# S-00b: workspaces

Set up on 2026-11-20 by the builder through the owner's browser, at the owner's request. No credentials are stored here or anywhere in this repository.

## Databricks

| Item | Value |
|---|---|
| Workspace host | `dbc-8a0a3f41-02fa.cloud.databricks.com` (trial) |
| Workspace ID | `7474655419666822` |
| SQL warehouse | Serverless Starter Warehouse, size Small |
| Warehouse HTTP path | `/sql/1.0/warehouses/7c055623cc917a32` |
| Catalog for client one | `client_one` (D-005 isolation: one catalog per client) |
| Schemas | `bronze`, `silver`, `gold` |

Created with:

```sql
CREATE CATALOG IF NOT EXISTS client_one;
CREATE SCHEMA IF NOT EXISTS client_one.bronze;
CREATE SCHEMA IF NOT EXISTS client_one.silver;
CREATE SCHEMA IF NOT EXISTS client_one.gold;
```

Verified: `SHOW SCHEMAS IN client_one` returned bronze, silver, gold, default and information_schema.

## Cube Cloud

| Item | Value |
|---|---|
| Deployment | `domcowboy.cubecloud.dev`, deployment 1 |
| State | Linked to `dja0086/governed-bi`, branch `main`, project directory `cube` (2026-11-20). Pushes to `main` deploy automatically. Direct commits from Cube to `main` are off |
| Connection test | Passed 2026-11-20: semantic SQL on `connection_check` in Explore returned one row from Databricks |
| Default catalog seen by Cube | `workspace` (from `current_catalog()`), not `client_one`. Models use fully qualified names |
| GitHub app scope | The repository picker listed other repositories of the account, so the Cube app appears to have access beyond `governed-bi` (observed, not checked in GitHub settings) |
| JDBC URL (no token) | `jdbc:databricks://dbc-8a0a3f41-02fa.cloud.databricks.com:443/default;transportMode=http;ssl=1;AuthMech=3;httpPath=/sql/1.0/warehouses/7c055623cc917a32` |
| Catalog | `client_one` |

## How the builder reaches these

- Databricks: through the SQL editor in the owner's browser, when it is open and connected. No token is held by the builder.
- Cube: through the owner's browser today. The durable route is Cube's Git integration with this repository, so the builder changes the data model by pull request.

## Not yet done

- NetSuite data landed in `client_one.bronze`. Requires a NetSuite connection in Databricks (Lakeflow Connect) with SuiteAnalytics Connect credentials, which the owner must enter.
