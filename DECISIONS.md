# Decision log

Each entry: what, who decided, when, and on what basis. "Council" means the three-model follow-up review of 19 Nov 2026; its rulings are judgement, not tested evidence.

## Decided

| ID | Decision | Decided by | Date | Basis |
|---|---|---|---|---|
| D-001 | Consolidation across subsidiaries is in scope for client one | Owner | 2026-11-19 | Owner's call. Client one has multi-currency and the intercompany elimination engine enabled (S-00) |
| D-002 | Certified answers appear only in server-rendered components. Anything an assistant writes or computes itself is uncertified | Claude, delegated by owner | 2026-11-19 | MCP Apps let a server return UI rendered in Claude, ChatGPT and Microsoft 365 Copilot (vendor documentation). An assistant's own arithmetic cannot be governed |
| D-003 | The manifest is the definition. Cube is the only serving surface | Council | 2026-11-19 | Removes the two-sources-of-truth conflict |
| D-004 | Build order: hand-write all five manifests; hand-build a reference output for gross orders only; the compiler must reproduce it; P&L is generated from its manifest | Council (one dissent) | 2026-11-19 | Gives the compiler a tied-out target and limits hand-built code to one dataset |
| D-005 | Two configuration layers (core, client). Subsidiary and book are scope keys. The resolver accepts more layers later | Council (one dissent) | 2026-11-19 | Smaller test matrix; one client cannot define an industry layer |
| D-006 | Onboarding is a scripted checklist with every exit check as code. A state machine waits for two real onboardings | Council | 2026-11-19 | The checks hold the value; the order is not yet known |
| D-007 | Adopt Cube's workbook format with a lint that binds each component to a metric ID and version, unless the spike shows it cannot be diffed or pinned | Council | 2026-11-19 | A neutral spec is a second product |
| D-008 | FIFO: certify NetSuite's posted cost of goods sold and inventory value. Rebuilt cost layers are analysis only | Council | 2026-11-19 | NetSuite's posted ledger is the book of record |
| D-009 | Ledger figures tie to the cent. Percentage tolerances apply to operational datasets only | Council | 2026-11-19 | 0.1% of a large line hides a material mis-mapping |
| D-010 | Phase 0 is an evidence gate with a bake-off limited to two stacks: Databricks + Cube, and Snowflake + Cube | Council; stacks from the scored evaluation | 2026-11-19 | These ranked first and second (81.0 and 78.0) |
| D-011 | No go-live date is published before the phase 0 gate. Forecasts are ranges from measured effort | Council | 2026-11-19 | Every duration so far is unmeasured |
| D-012 | Seconds lane: gross orders only, built after batch ties out and the breaker handles provisional rows. Never for the ledger | Council | 2026-11-19 | Provisional rows are overwritten by batch |
| D-013 | The live lane (NetSuite's own MCP server) is out until a scope-parity test passes | Council | 2026-11-19 | It answers under a NetSuite role, outside Cube's security context |
| D-014 | Client one is NetSuite account TSTDRV1755417 | Owner | 2026-11-20 | Owner's statement; account ID confirmed from the connector's session information |
| D-015 | The owner is the only person and holds every human role. The builder merges uncertified work itself. Five things need the owner (see `GOVERNANCE.md`) | Owner | 2026-11-20 | Owner: "no artificial red tape" |
| D-016 | No calendar schedule. Work runs in dependency order; a gate passes when its evidence exists. This replaces the six-week timebox in D-010 | Owner | 2026-11-20 | Owner: no schedules that run slower than needed |
| D-017 | Multi-book translation is out of scope for client one | Claude | 2026-11-20 | The account has one accounting book (S-00) |
| D-018 | Custom read-only SuiteQL queries against client one have standing approval. Default to aggregates; hard ceiling of 1,000 rows per query; anything larger goes through the batch lane | Owner approved; cap set by Claude | 2026-11-20 | 1,000 rows is one page, so one request and one concurrency slot (Oracle paging documentation). The account already throttled light discovery. Bulk belongs in the governed batch lane |
| D-019 | SuiteAnalytics Connect is enabled on client one | Owner | 2026-11-20 | Owner's statement. Not yet confirmed by a connection test |
| D-020 | The connected NetSuite role stays as configured. The builder still never writes | Owner | 2026-11-20 | Owner: what is set up is permissible. If the tie-out reports are refused under this role, that comes back as a finding from S-02 |

## Open

| ID | Question | Owner of the answer | Blocks |
|---|---|---|---|
| O-005 | Is front-end work needed for certified components, or do Cube's interactive views carry the badges? | Spike S-10 | Go-live 1 design |
| O-006 | Does any item on client one use FIFO costing? The account default is average | Spike S-01 / discovery | FIFO analysis |
| O-007 | Does the owner still want a Fable 5.1 review? | Owner | Nothing |
| O-008 | The manifest grammar has no metric type for a difference of metrics (gross profit, net income). Add a `derived` type with a closed set of operators? | Claude to propose | P&L manifest completeness |
| O-010 | A test-drive account proves the method, not behaviour on a real company. Which real account follows? | Owner | Selling; not phase 0 |

## Closed

| ID | Question | Answer |
|---|---|---|
| O-001 | Is United Refractories client one? | No. Client one is TSTDRV1755417 (D-014). An earlier subsidiary read came from the United Refractories connector and is not used |
| O-002, O-003 | Who are the finance owner and NetSuite administrator? | The owner (D-015) |
| O-004 | Is SuiteAnalytics Connect enabled? | Yes, per the owner (D-019) |
| O-009 | Narrow the connected role? | No (D-020) |
