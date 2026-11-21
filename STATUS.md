# Status

Last updated: 2026-11-21

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
- NetSuite Connect login and query proven from the owner's Mac under role 57 with driver 8.10.190.0, so the NetSuite side is correct (S-03b).
- Seed load done: 15 NetSuite tables are in `client_one.bronze`, row counts matching the export (`phase0/results/S-03c-seed-load.md`). A snapshot, not a pipeline. Scripts in `pipelines/seed/`.
- S-01 done: no cost-layer table is visible. FIFO stays analysis-only.
- S-02 partly done: the Income Statement can be pulled programmatically, consolidated, at account level. Trial Balance, Inventory Valuation and AR aging are not available to the connected role.

## Blocked on the owner

| Item | Unblocks |
|---|---|
| Databricks serverless egress is still blocked after the payment method was added, and the help centre rejects the owner's login. Routes left: wait and re-test, a public community post, Enterprise tier (costs money; unverified that it lifts the block), or an extractor outside Databricks serverless | Scheduled, incremental ingestion. No longer blocks modelling: the seed load covers it |
| O-013: add Trial Balance, Inventory Valuation and AR aging to the connected role's reports, or accept Income Statement tie-outs only | Three of the four tie-outs |
| O-011: the rounding rule for "ties to the cent" | Certifying consolidated figures |

## Next for the builder (no owner input needed)

- O-014: check whether queries under this role reach every posting the Income Statement includes.
- O-006: find the item costing-method field and count items by method.
- O-012: propose other-income and other-expense filters so net income can be defined.
- Write and run the hand-built reference query for gross orders (D-004) on `client_one.bronze`.
- Income Statement tie-out on the seed data (report 589, 2017 periods, D-023).
- First real Cube model on bronze, fully qualified names.
- Re-test Databricks egress; if open, run the managed pipeline.

## Standing instruction

- D-027: production-grade architecture only. The seed load is a stopgap. The GitHub runner reach test (branch `runner-reach-test`, unmerged) proved reachability and is not an ingestion design.
- New Databricks account on a business email exists (workspace 7474653267700225). Verified 2026-11-21: serverless egress to NetSuite is blocked there too while it is a trial. Payment method added 2026-11-21; still blocked (S-03b). Not migrated. Next: account verification request to Databricks, sent by the owner.

## Ingestion architecture (D-028)

- Approved 2026-11-21: own extraction service on AWS pushing into Databricks. Document: https://claude.ai/artifact/3t3kBkq5i4PwX2wngyzKGx
- Verified from the export manifest (column names only): 14 of 15 tables carry a last-modified column (`lastmodifieddate`; `linelastmodifieddate` on transactionline). `consolidatedexchangerate` has none and is re-read in full each run (29,078 rows on client one).
- Verified 2026-11-21: role 57 reads DeletedRecord over Connect (68,120 rows, current to 2026-11-20). O-016 closed.
- Unverified: Zerobus in our workspace (O-017, needs a service principal from the owner). AWS account pending (O-018).

## Findings so far

- The posted data sits in 2017; recent years are nearly empty (D-023).
- Consolidated amounts carry fractions of a cent (O-011).
- 23 subsidiaries, five of them elimination subsidiaries, four consolidated views, one book.
- Default costing is average. A grouped query on item costing method was rejected; the field name needs checking.
- The API budget is small: light discovery hit "Too Many Requests" once.
