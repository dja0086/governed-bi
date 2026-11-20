# Status

Last updated: 2026-11-20

## Where we are

Phase 0 (evidence gate) has started. There is no calendar schedule (D-016): work runs in dependency order and the gate passes when its evidence exists.

## Done

- Repository live at github.com/dja0086/governed-bi.
- Client one confirmed as TSTDRV1755417 (D-014). Account profile read and recorded in `phase0/results/S-00-account-profile.md`.
- Governance reduced to one owner and one builder (D-015). Five things need the owner; everything else the builder does and merges.
- Manifests drafted: gross orders, P&L. Both validate.
- Decisions D-001 to D-017 recorded.

## Running now (no owner input needed)

- Remaining three manifests by hand: actual sales, cost analysis, FIFO cost layers.
- S-01: look for a cost-layer table in metadata.
- S-02: list and run the standard reports the tie-outs need, one call at a time.

## Blocked on the owner

| Item | Unblocks |
|---|---|
| Open workspaces: Databricks, Snowflake, Cube Cloud (trials are enough) | The bake-off and every spike from S-03 on |
| Check whether SuiteAnalytics Connect is enabled (O-004) | The batch lane design |
| Narrow the connected role to read-only and add financial-report access (O-009) | S-02, and safe operation |
| Say yes or no to custom read-only SuiteQL queries against client one, capped at 1,000 rows | Faster discovery in S-01 and S-03 |

## Findings so far

- The connected role is not read-only and may not be able to run financial statements.
- The account's default costing is average, not FIFO.
- One accounting book, so multi-book is out of scope (D-017).
- Light discovery already hit "Too Many Requests". The API budget is a real constraint.
- The P&L manifest exposed a grammar gap: no way to define gross profit or net income as a difference (O-008).

## Next step

Builder continues the three manifests and S-01/S-02. Owner opens the three workspaces and fixes the role.
