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
| D-019 | SuiteAnalytics Connect is enabled on client one | Owner | 2026-11-20 | Confirmed 2026-11-20 by a successful token-based login and query from the owner's computer (S-03b) |
| D-020 | The connected NetSuite role stays as configured. The builder still never writes | Owner | 2026-11-20 | Owner: what is set up is permissible. If the tie-out reports are refused under this role, that comes back as a finding from S-02 |

| D-021 | The manifest grammar gains a `derived` metric type: add or subtract two or more named metrics. Closes O-008 | Claude | 2026-11-20 | Gross profit and net ordinary income could not be defined without it. The operator list is closed, so it adds no free-form logic |
| D-022 | Datasets carry an optional `certification` flag; FIFO cost layers are `analysis_only` | Claude | 2026-11-20 | Makes D-008 enforceable by the compiler instead of by convention |
| D-023 | Tie-out tests on client one use 2017 periods | Claude | 2026-11-20 | That is where the posted data is (S-02) |
| D-024 | The Cube project lives in `cube/` in this repository; Cube Cloud builds from branch `main` with project directory `cube` | Claude | 2026-11-20 | Cube's connect dialog offers a project directory for monorepos (seen on the settings page). One repository keeps manifests and generated model under one review and one history (D-003) |
| D-030 | Version 1 design approved: a Git-backed Claude plugin (skills + certified queries), Claude Data dashboards on the NetSuite connector, chat on the same definitions, scheduled tasks for briefs. Claude Duet is out of version 1 (research preview, waitlist) | Owner ("Approved.") | 2026-11-21 | Anthropic's Claude Data article and the Data artifact type's guide: datasets are stored connector calls run on each reader's connection; certified queries (beta) carry a badge and can be managed in GitHub. Design: https://claude.ai/artifact/6XHtS74JWe9Acv4DqmXxzb |
| D-029 | Version 1 is direct to NetSuite with no database: NetSuite is the only data source, and analytics and chat are bolted on top of it. Version 2 is Databricks + AWS extraction service + Cube, built when version 1 hits a documented limit (large results, concurrency, FIFO and cost layers, history). D-028 and the Cube analytics plan now describe version 2 and are paused, not withdrawn | Owner | 2026-11-21 | Owner's instruction. Oracle's NetSuite AI Connector is generally available with role-based access; live access removes ingestion lag and delete capture. Limits stated before building: 5,000-row tool cap, small API concurrency observed on client one, no cost-layer table (S-01), non-repeatable ad hoc SQL, AI-provider data terms |
| D-028 | Production ingestion is our own extraction service on AWS, pulling from SuiteAnalytics Connect and pushing into Databricks (polling lane as truth, optional event lane). The Databricks managed NetSuite connector is not the production path | Owner ("AWS, approved") | 2026-11-21 | The connector cannot ingest DeletedRecord (Databricks limits page) and runs triggered only; NetSuite has no change feed; serverless on both accounts cannot reach NetSuite. Full reasoning: https://claude.ai/artifact/3t3kBkq5i4PwX2wngyzKGx |
| D-027 | Architecture decisions are production-grade, not one-off proofs of concept. Stopgaps are labelled as such and not built on. Platform tier and trial limits are checked before building | Owner | 2026-11-21 | Owner's instruction, after a GitHub Actions extractor was proposed as the ingestion route. It fails on best-effort scheduling, shared addresses and tenant isolation. Also covers the earlier miss: the trial's documented network limit was not checked before building on it |
| D-026 | Seed load: a one-time export from the owner's Mac into `client_one.bronze` stands in for ingestion until Databricks egress opens. Not the production path | Owner ("Agreed with 1 and 2") | 2026-11-20 | Login and query from the Mac succeeded while Databricks DNS to netsuite.com failed (S-03b). 15 tables landed, counts match (S-03c) |
| D-025 | No Snowflake. The two-stack bake-off (D-010) is withdrawn; Databricks + Cube is the stack | Owner | 2026-11-20 | Owner's instruction. Consequence: the choice rests on the documentary comparison, not on a head-to-head test |

## Open

| ID | Question | Owner of the answer | Blocks |
|---|---|---|---|
| O-005 | Is front-end work needed for certified components, or do Cube's interactive views carry the badges? | Spike S-10 | Go-live 1 design |
| O-006 | Does any item on client one use FIFO costing? The account default is average | Spike S-01 / discovery | FIFO analysis |
| O-007 | Does the owner still want a Fable 5.1 review? | Owner | Nothing |
| O-011 | Consolidated report amounts carry fractions of a cent. What is the rounding rule for "ties to the cent": round each account, or only totals, and how? | Owner | Certifying any consolidated figure |
| O-012 | The filter list has no other-income or other-expense account filters, so net income cannot be defined. Add them? | Claude to propose | Full P&L |
| O-013 | Trial Balance, Inventory Valuation and AR aging are not in the connected role's report list. Add them to the role, or tie out only the Income Statement? | Owner | Three of the four tie-outs |
| O-014 | Can queries under this role reach every posting the Income Statement includes (payroll, rent, depreciation)? | Next discovery pass | Whether the P&L tie-out can pass |
| O-015 | Partly resolved: the Databricks connection and the seed export use role 57 (Data Warehouse Integrator), verified by a successful login under that role. Still open: confirm role 57 is read-only in practice and retire the token's access to role 1453 before any real account | Owner | Onboarding a real account |
| O-017 | Zerobus Ingest in our workspace: needs a service principal and secret (owner creates) before the 100-row push test | Owner | Landing step |
| O-010 | A test-drive account proves the method, not behaviour on a real company. Which real account follows? | Owner | Selling; not phase 0 |

## Closed

| ID | Question | Answer |
|---|---|---|
| O-016 | Can role 57 read DeletedRecord over Connect? | Yes. Verified 2026-11-21 by the owner running `pipelines/checks/ns_deleted_check.sh`: 68,120 rows, deleteddate from 2010-05-31 to 2026-11-20. The MCP connector's role cannot see the table; role 57 can. Open detail: column list and the count by type sorted by size were not captured (output cut at 60 types) |
| O-018 | AWS account for the extraction service | Created by the owner 2026-11-21, home region us-east-2. Nothing deployed yet |
| O-001 | Is United Refractories client one? | No. Client one is TSTDRV1755417 (D-014). An earlier subsidiary read came from the United Refractories connector and is not used |
| O-002, O-003 | Who are the finance owner and NetSuite administrator? | The owner (D-015) |
| O-004 | Is SuiteAnalytics Connect enabled? | Yes, per the owner (D-019) |
| O-009 | Narrow the connected role? | No (D-020) |
