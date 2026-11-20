# Phase 0 spikes

Each spike answers one question with a measured result. Spikes run in dependency order, as soon as what they need exists; there is no calendar. A spike with no measured result is not done. Results go in `phase0/results/S-xx.md`.

| ID | Question | Method | Pass looks like | Needs |
|---|---|---|---|---|
| S-01 | Is any FIFO cost-layer table queryable in NetSuite? | Check the Records Catalog and SuiteQL metadata in the live account | A named table, or a recorded "none found" with the catalog search terms used | NetSuite read |
| S-02 | Can NetSuite report figures be pulled programmatically for tie-outs? | Run Trial Balance, Income Statement, Inventory Valuation, AR aging by subsidiary, period and book through the report tool and the REST path | Each report returns figures at account, period and subsidiary level, consolidated and per subsidiary | NetSuite read; report permission (O-009) |
| S-03 | Do accounting lines sync incrementally and reliably? | Edit GL-impacting lines without a header change (revaluation, reallocation, cost recalculation); diff the connector output against a full extract | 100% of 30 seeded edits captured within the batch target | Both warehouses; a connector; owner approval for seeded edits |
| S-04 | Does the nightly delete sweep fit the API budget? | Run a key-set sweep over transaction lines through the chosen path; record duration and concurrency used | Completes in the nightly window at a capped share of the account's concurrency | A connector |
| S-05 | Do NetSuite role restrictions map to a row filter? | List restriction types per role; build the mapping; compare what three roles see in NetSuite with what they see in the semantic layer | Identical row sets for three differing roles | Cube workspace |
| S-06 | Does one template serve many clients in Cube? | Generate synthetic clients from the template; measure compile time, memory and cache at 5, 10, 25 and 50; add three structurally different clients | A growth curve that stays workable at 50; correct results for the three | Cube workspace |
| S-07 | Does one release deploy to many client catalogs? | Deploy the bundle (or the Snowflake equivalent) to 5 and 25 client targets | Deploy time and failure modes recorded for both stacks | Both warehouses |
| S-08 | What are the limits and latency of Event Subscriber scripts? | Read Oracle's script-type limits page; deploy a test subscriber in a sandbox; measure event-to-landing time under load | Limits confirmed from Oracle's page; p95 latency recorded | Owner approval to deploy a test script |
| S-09 | Does caching hide fresh rows? | Land provisional rows; query through pre-aggregations and the cache; check as-of times | As-of time on every answer matches the data served | Cube workspace; data landed |
| S-10 | Can certified answers be rendered by a server component inside Claude, ChatGPT and Copilot? | Build one component (KPI plus badge: scope, metric version, as-of, status) and render it in all three; check whether Cube's own interactive views can carry the badge | Renders in all three with the badge; decision on Cube views versus our own component | Cube workspace |
| S-11 | How accurate are AI-drafted account mappings? | Hold out a share of client one's chart of accounts; draft mappings; score first-pass tie-out and human edits; seed errors including offsetting ones | Correction rate with an interval; every seeded error caught by review or tie-out | Chart of accounts read |
| S-12 | Can FIFO layers be rebuilt for analysis? | Rebuild layers for 20 items from transactions; compare with Inventory Valuation Detail | Variance recorded per item; go or no-go for analysis-only FIFO | Transactions landed |
| S-13 | Does consolidated P&L reproduce NetSuite's report? | Apply consolidated exchange rates and the elimination subsidiary; compare with the consolidated Income Statement | Ties to the cent by account and period, or each variance explained | P&L slice built |
| S-14 | What does one client cost to run? | Record compute, licences and model usage for the slice on each stack | Cost per client per month at stated volumes, both stacks | Slice running on both stacks |

## Bake-off

Two stacks build the same slice (gross orders and consolidated P&L): Databricks + Cube, and Snowflake + Cube. Scored on the weighted criteria in `docs/` using measured results in place of every UNPROVEN.

## Gate pack (when every spike has a result)

Spike results, bake-off scores, platform decision, measured effort for the slice, a forecast range for go-live, and a go or no-go recommendation.
