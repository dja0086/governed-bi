# Decision log

Each entry: what, who decided, when, and on what basis. "Council" means the three-model follow-up review of 19 Nov 2026; its rulings are judgement, not tested evidence.

## Decided

| ID | Decision | Decided by | Date | Basis |
|---|---|---|---|---|
| D-001 | Consolidation across subsidiaries is in scope for client one | Owner | 2026-11-19 | Owner's call. The connected NetSuite account has eight subsidiaries, one of them an elimination subsidiary |
| D-002 | Certified answers appear only in server-rendered components. Anything an assistant writes or computes itself is uncertified | Claude, delegated by owner | 2026-11-19 | MCP Apps let a server return UI rendered in Claude, ChatGPT and Microsoft 365 Copilot (vendor documentation). An assistant's own arithmetic cannot be governed |
| D-003 | The manifest is the definition. Cube is the only serving surface | Council | 2026-11-19 | Removes the two-sources-of-truth conflict |
| D-004 | Build order: hand-write all five manifests; hand-build a reference output for gross orders only; the compiler must reproduce it; P&L is generated from its manifest | Council (one dissent) | 2026-11-19 | Gives the compiler a tied-out target and limits hand-built code to one dataset |
| D-005 | Two configuration layers (core, client). Subsidiary and book are scope keys. The resolver accepts more layers later | Council (one dissent) | 2026-11-19 | Smaller test matrix; one client cannot define an industry layer |
| D-006 | Onboarding is a scripted checklist with every exit check as code, run by a person. A state machine waits for two real onboardings | Council | 2026-11-19 | The checks hold the value; the order is not yet known |
| D-007 | Adopt Cube's workbook format with a lint that binds each component to a metric ID and version, unless the spike shows it cannot be diffed or pinned | Council | 2026-11-19 | A neutral spec is a second product |
| D-008 | FIFO: certify NetSuite's posted cost of goods sold and inventory value. Rebuilt cost layers are analysis only | Council | 2026-11-19 | NetSuite's posted ledger is the book of record |
| D-009 | Ledger figures tie to the cent. Percentage tolerances apply to operational datasets only | Council | 2026-11-19 | 0.1% of a large line hides a material mis-mapping |
| D-010 | Phase 0 is a six-week evidence gate with a bake-off limited to two stacks: Databricks + Cube, and Snowflake + Cube | Council; stacks from the scored evaluation | 2026-11-19 | These ranked first and second (81.0 and 78.0) |
| D-011 | No go-live date is published before the phase 0 gate. Forecasts are ranges from measured effort | Council | 2026-11-19 | Every duration so far is unmeasured |
| D-012 | Seconds lane: gross orders only, built after batch ties out and the breaker handles provisional rows. Never for the ledger | Council | 2026-11-19 | Provisional rows are overwritten by batch |
| D-013 | The live lane (NetSuite's own MCP server) is out until a scope-parity test passes | Council | 2026-11-19 | It answers under a NetSuite role, outside Cube's security context |

## Open

| ID | Question | Owner of the answer | Needed by |
|---|---|---|---|
| O-001 | Is United Refractories client one? | Owner | Start of phase 0 |
| O-002 | Who is the finance owner? | Owner | Week 1 |
| O-003 | Who is the NetSuite administrator for the read-only role? | Owner | Week 1 |
| O-004 | Does client one hold a SuiteAnalytics Connect licence? | Owner / NetSuite administrator | Week 1 |
| O-005 | Is front-end capacity needed for certified components, or do Cube's interactive views carry the badges? | Spike S-10 | Gate |
| O-006 | Which costing methods does client one use? | Discovery | Week 2 |
| O-007 | Does the owner still want a Fable 5.1 review? | Owner | Any time |
