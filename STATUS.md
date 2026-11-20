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
| Merge pull request 1 (it changes `GOVERNANCE.md`) | The builder merging its own later work |
| Open workspaces: Cube Cloud and Databricks first, Snowflake when the bake-off starts (trials are enough) | Every spike from S-03 on |

Answered on 2026-11-20: Connect is enabled (D-019), the role stays as configured (D-020), custom read-only queries are approved up to 1,000 rows (D-018).

## Findings so far

- The connected role is not read-only and may not be able to run financial statements.
- The account's default costing is average, not FIFO.
- One accounting book, so multi-book is out of scope (D-017).
- Light discovery already hit "Too Many Requests". The API budget is a real constraint.
- The P&L manifest exposed a grammar gap: no way to define gross profit or net income as a difference (O-008).

## Next step

Builder continues the three manifests and S-01/S-02. Owner opens the three workspaces and fixes the role.
