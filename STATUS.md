# Status

Last updated: 2026-11-20

## Where we are

Phase 0 (evidence gate) is under way. No calendar schedule (D-016): work runs in dependency order and the gate passes when its evidence exists.

## Done

- Repository live; pull request 1 merged at the owner's instruction. The builder now merges uncertified work itself.
- Client one confirmed as TSTDRV1755417. Profile in `phase0/results/S-00-account-profile.md`.
- All five example datasets hand-written as manifests: gross orders, P&L, actual sales, cost analysis, FIFO cost layers. All validate.
- Grammar extended twice from what the manifests needed: a `derived` metric type (D-021) and a `certification` flag (D-022).
- Databricks and Cube Cloud workspaces exist. Catalog `client_one` with bronze, silver and gold schemas created. Details in `phase0/results/S-00b-workspaces.md`.
- Cube is linked to this repository (branch `main`, project directory `cube`); the demo project is gone. Verified 2026-11-20: the `connection_check` cube returned one row through Databricks, so repository to Cube to warehouse works end to end.
- Finding: that query reported the session catalog as `workspace`, not `client_one`. Models must use fully qualified names (`client_one.<schema>.<table>`) so they do not depend on the connection's default catalog.
- Cube project added to this repository in `cube/` (pull request 4): configuration plus one uncertified connection check that reads no client data.
- Databricks connection `ns_tstdrv` to NetSuite exists (host `tstdrv1755417.connect.api.netsuite.com`, port 1708), created by the owner 2026-11-20. Not yet proven by a read. Volume `client_one.bronze.netsuite_jars` created.
- NetSuite role 1453 ("Power User") carries every permission the connector guide lists, including SuiteAnalytics Connect and Log in using Access Tokens (read from the role page). It is also a full-access role; whether the token should move to a read-only role is the owner's call (O-015).
- S-01 done: no cost-layer table is visible. FIFO stays analysis-only.
- S-02 partly done: the Income Statement can be pulled programmatically, consolidated, at account level. Trial Balance, Inventory Valuation and AR aging are not available to the connected role.

## Blocked on the owner

| Item | Unblocks |
|---|---|
| In NetSuite: give the integration user the Data Warehouse Integrator role and create an access token under it. In Databricks: edit connection `ns_tstdrv` with that role's internal ID and the new token | Retrying ingestion (first attempt failed, see `phase0/results/S-03a-ingestion-first-attempt.md`) |
| Open a Snowflake trial when the comparison starts | The bake-off only |
| O-013: add Trial Balance, Inventory Valuation and AR aging to the connected role's reports, or accept Income Statement tie-outs only | Three of the four tie-outs |
| O-011: the rounding rule for "ties to the cent" | Certifying consolidated figures |

## Next for the builder (no owner input needed)

- O-014: check whether queries under this role reach every posting the Income Statement includes.
- O-006: find the item costing-method field and count items by method.
- O-012: propose other-income and other-expense filters so net income can be defined.
- Draft the hand-built reference query for gross orders (D-004), ready to run once a warehouse exists.

## Findings so far

- The posted data sits in 2017; recent years are nearly empty (D-023).
- Consolidated amounts carry fractions of a cent (O-011).
- 23 subsidiaries, five of them elimination subsidiaries, four consolidated views, one book.
- Default costing is average. A grouped query on item costing method was rejected; the field name needs checking.
- The API budget is small: light discovery hit "Too Many Requests" once.
