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

## Current step (D-031, owner, 2026-11-22)

- Knowi trial against client one. The plugin and its three skills were removed from the repository (recoverable from history, pull request 23). The draft Claude Data dashboard still exists in the owner's Claude account and was not touched.
- Carry into the trial: never sum `netamount` across subsidiaries (one subsidiary in another currency dominates the raw sum); `ORDER BY` a column position fails in SuiteQL; order counts by year and by subsidiary for 2017 are known and tie to each other, so they are the first check of any Knowi query.

## Direction (D-029, owner, 2026-11-21)

- Version 1: direct to NetSuite, no database. Chat and report building through Claude on Oracle's NetSuite AI Connector; standing dashboards in NetSuite's own analytics; Knowi under evaluation as a dashboard product on live SuiteQL.
- Version 2: Databricks + AWS extraction service + Cube. Paused. Everything below under D-028 is version 2.
- Version 1 design, proposed 2026-11-21, awaiting the owner's approval: https://claude.ai/artifact/6XHtS74JWe9Acv4DqmXxzb . Core: a Git-backed Claude plugin (skills + certified queries), Claude Data dashboards on the NetSuite connector, scheduled tasks for briefs. Verified from Anthropic's Claude Data article and the Data artifact type's guide: a dashboard dataset is one stored connector tool call run on each reader's own connection; certified queries (beta) carry a badge and can be managed in GitHub through a Git-backed plugin; no scheduled refresh of live queries. Unverified: that a NetSuite connector call works as a stored query, and the certified-query file format. Knowi trial is next for evaluation.
- Version 1 design approved (D-030). Build step 1 in progress: a Claude Data dashboard with one live query, `v1/queries/gross_orders_by_year.json`, exists at https://claude.ai/artifact/FpsDNjxT1vQykVhBv3nLgo . Verified: the stored call runs through the NetSuite connector and returns 11 yearly rows; its order count across all years is 389, equal to the 389 sales orders in the independent seed export (S-03c). Verified 2026-11-22: the page renders for the owner (the dashboard's own check reported passed), so a NetSuite connector call works as a stored Claude Data query. Unverified: the amount tie-out, currency basis.
- Finding 2026-11-22: summing `netamount` across subsidiaries is wrong, not merely unconverted. Grouped by subsidiary for 2017, order and line counts tie to the ungrouped query, but one subsidiary in a different currency dominates the raw sum. The draft dashboard's headline total and the peak-year share are therefore misleading until the currency basis is decided (owner). Skills corrected.
- Plugin scaffold in `plugin/` (three skills, one proposed query, nothing certified). Certified-query file format verified from the Data type's admin-setup reference. Unverified: installing this plugin from the repository into a Claude organization, and whether this organization may choose a repository-synced plugin for certified queries.
- Next: run the five datasets live against client one and record, for each, tie-out, duration and whether it stays inside the connector's limits. FIFO is the open one.
- The seed data in the first Databricks account and the two Databricks workspaces are idle. Nothing is deployed in AWS.

## Ingestion architecture (D-028, version 2, paused)

- Approved 2026-11-21: own extraction service on AWS pushing into Databricks. Document: https://claude.ai/artifact/3t3kBkq5i4PwX2wngyzKGx
- Verified from the export manifest (column names only): 14 of 15 tables carry a last-modified column (`lastmodifieddate`; `linelastmodifieddate` on transactionline). `consolidatedexchangerate` has none and is re-read in full each run (29,078 rows on client one).
- Verified 2026-11-21: role 57 reads DeletedRecord over Connect (68,120 rows, current to 2026-11-20). O-016 closed.
- Unverified: Zerobus in our workspace (O-017, needs a service principal from the owner). AWS account exists (us-east-2, "new" sign-up type), nothing deployed. Its limits are in `phase0/results/S-04a-aws-account-limits.md`; Fargate and fixed outbound address are unverified there.

## Findings so far

- The posted data sits in 2017; recent years are nearly empty (D-023).
- Consolidated amounts carry fractions of a cent (O-011).
- 23 subsidiaries, five of them elimination subsidiaries, four consolidated views, one book.
- Default costing is average. A grouped query on item costing method was rejected; the field name needs checking.
- The API budget is small: light discovery hit "Too Many Requests" once.
