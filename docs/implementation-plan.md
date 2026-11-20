# Implementation plan (repository copy)

The full plan is the document "Implementation plan: governed AI analytics for NetSuite":
https://claude.ai/artifact/GNP7gzuk4F7dKJbJfSsB3c

This copy holds the path to go-live so the repository stands alone.

## What go-live means

Go-live 1: client one's users ask questions in their AI assistant and get certified gross orders and consolidated P&L, rendered by a server component with scope, metric version, as-of time and status, on a 15-minute batch lane, with per-user security enforced.

## Phases and gates

| Phase | Builds | Gate to pass |
|---|---|---|
| 0. Evidence gate (no calendar; passes when every spike has a result) | Spikes S-01 to S-14, five hand-written manifests, two-stack bake-off | Platform chosen on measured results; forecast range issued; owner says go |
| 1. Base slice | Gross orders and consolidated P&L on batch; catalog per client; security context; snapshot tie-outs and breaker; certified components in chat; compiler for what the slice needs | Go-live 1 criteria met |
| 2. Extend | Actual sales, cost analysis, seconds lane for gross orders, chat evaluation | Go-live 2: four datasets live; seconds lane measured |
| 3. Inventory and operations | Certified posted cost of goods sold and inventory value; FIFO layers as analysis; drift detection; flat-file adapter | Go-live 3: five example datasets covered; flat-file source added |
| 4. Client two | A real second account onboarded by someone outside the build team | Productization proven; exit tests passed |

There is no calendar schedule (D-016). Each gate passes when its evidence exists. A go-live forecast is issued at the phase 0 gate from measured effort (D-011).

Client one is a NetSuite test-drive account (D-014). Go-live 1 on it proves the method; a real account is still needed before selling (O-010).

## Go-live 1 criteria

1. Trial Balance and consolidated Income Statement tie to the cent by account, period and subsidiary on a common snapshot.
2. The owner checks 20 random cells against the NetSuite screen, to the cent.
3. Three users with different roles see only their scope; an outsider's leak attempts fail.
4. Batch freshness is at or under 15 minutes at the 95th percentile over two weeks.
5. Fifty real questions, including ones that must be refused, are graded by the owner.
6. Seeded faults (wrong mapping, deleted line, backdated entry, renamed field) are each caught within the stated latency.
7. The owner signs the certification.
