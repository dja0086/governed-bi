# Configurability layer: build plan (19 Nov 2026)

## Summary

Build a control plane in which a typed manifest is the only thing anyone edits, and compilers generate the pipeline, semantic model, tests and dashboards from it. The estimate is 16 weeks to pass three exit tests, with three engineers and a part-time finance owner.

The plan closes the ten gaps in this order:
1. Foundation (weeks 1 to 7): dataset manifest and compiler, metric-type library, canonical entities, source adapter contract.
2. Variation (weeks 8 to 11): layered client configuration, dashboard specs, change rules, and FIFO as the stress case.
3. Operations (weeks 12 to 14): onboarding state machine, drift detection, gap capture, client isolation and metering.
4. Proof (weeks 15 to 16): a sixth dataset, a second client and a flat-file source, each added with zero platform code.

Three things are assumptions, not facts: the 16-week estimate, the six decisions defaulted below, and the platform (Databricks plus Cube), which has not been tested hands-on.

The ten gaps being closed: (1) dataset spec registry, (2) source adapter contract, (3) canonical entity layer, (4) metric-type library, (5) layered client configuration, (6) declarative dashboard specs, (7) metadata harvesting and drift detection, (8) change classes and approval rules, (9) gap-capture loop, (10) client isolation and metering.

## Assumptions on the six open decisions

None of the six decisions has been answered by the owner, so the plan runs on these defaults.

| # | Decision | Default taken | If decided otherwise |
|---|---|---|---|
| 1 | Wait for the platform bake-off? | No. Build now, with manifests and contracts kept platform-neutral and only the compilers platform-specific | Waiting delays the start by the bake-off's six weeks |
| 2 | Who owns metric definitions? | One named finance owner per domain, with the controller as tie-breaker | A definitions board adds a meeting cycle to every certification |
| 3 | Target scale | 10 clients in year one, 50 in year two (placeholder) | Above roughly 50, deployment tooling and isolation need a separate design pass |
| 4 | Canonical modeling | Narrow: only the entities the five datasets share, extended when a new dataset needs one | A fuller layer adds an estimated three to four weeks before the first dataset ships |
| 5 | Controller experience | Client one uses multiple-choice questions in chat plus one reviewed change set. A review workspace waits for client two | Building the workspace now adds an estimated four weeks |
| 6 | Client isolation | One catalog per client, shared code | Row-level isolation is cheaper to run but weaker for finance data |

## Design 1: the dataset manifest

One manifest per dataset is the unit of change. Adding a dataset means writing a manifest, not code.

What a manifest declares:
- Identity: name, version, owner, certification state.
- Shape: grain, keys, sign convention, as-of rule (transaction date, posting period or load time), currency basis.
- Inputs: the canonical entities it reads, never source tables.
- Freshness: which lanes are allowed and the target for each.
- Integrity: delete-detection strategy, tests, tie-out target and tolerance.
- Metrics: each one an instance of a library type with parameters.
- Dimensions: standard ones, plus slots that client configuration fills.

Worked example, gross orders (field names illustrative; phase 1 settles them by writing the manifest for all five datasets by hand first):

```yaml
dataset: gross_orders
version: 1.0.0
owner: finance.revenue
state: certified
grain: sales_order_line
keys: [transaction_id, line_id]
sign: positive_for_orders
as_of: transaction_date
currency_basis: [transaction, subsidiary_base, consolidated]
inputs: [order_line, item, party, period, currency_rate]
freshness:
  batch: {target_minutes: 15}
  event: {target_seconds: 60, status: provisional}
  live: {allowed: true, badge: uncertified}
integrity:
  deletes: key_set_sweep_nightly
  tie_out: {report: sales_orders_register, tolerance_pct: 0.1}
metrics:
  - id: gross_bookings
    type: additive
    measure: line_amount
    exclude: [tax_lines, shipping_lines]
  - id: net_bookings
    type: additive
    measure: line_amount
    exclude: [tax_lines, shipping_lines, closed_lines, cancelled_lines]
dimensions: [customer, item, subsidiary, department, {slot: custom_segments}]
```

What it compiles into: pipeline definition (Databricks declarative pipeline); semantic model (Cube YAML model); tests (key, null and tie-out checks); circuit-breaker rule (degrade the metric when the tie-out or freshness target fails); golden questions (generated per metric, run on every change); documentation (metric glossary the chat agent reads).

How AI is used: a model drafts or edits the manifest with its output constrained to the manifest's JSON Schema. The compiler rejects anything invalid before a person reviews it. The compiler itself is ordinary deterministic code.

## Design 2: metric types, canonical entities, source adapters

Metric-type library: a closed set of tested shapes. A new metric is a type plus parameters, never free-form SQL. New types arrive only through a reviewed release.

| Type | Rule | Example |
|---|---|---|
| Additive | Sums across every dimension | Gross bookings, revenue |
| Semi-additive balance | Sums across everything except time; takes the closing value over time | Inventory on hand, open receivables |
| Ratio | Computed from a stored numerator and denominator, never averaged | Gross margin % |
| Period-to-date and period-over-period | Runs on the client's fiscal calendar | P&L month-to-date, year-over-year |
| Currency-converted | Uses the rate type and accounting book the manifest names | Consolidated P&L |
| Allocation | Spreads an amount by a declared driver | Overhead in cost analysis |
| Layer-based | Stateful: consumes receipt layers in order; cannot be summed naively | FIFO cost layers |

Canonical entities, first release (narrow by default): order line, invoice line, GL posting, inventory movement, cost layer, item, party, period, currency rate, subsidiary and book. Each source maps into these. A new entity is added only when a dataset needs one.

Source adapter contract: every source declares the same capabilities, and the orchestrator plans around the answers.

| Capability | NetSuite | Flat file |
|---|---|---|
| Schema discovery | Metadata catalog and SuiteQL metadata | Inferred from the file, confirmed by the owner |
| Incremental key | Last-modified columns, polled | File arrival |
| Change log | None | None |
| Delete detection | Not logged for transaction lines; nightly key-set sweep | Full replace per file |
| Rate budget | Shared account pool; adapter takes a capped share | None |
| Live lane | Yes, read-only through NetSuite's MCP server | No |
| Drift reporting | Scheduled metadata re-read, diffed | Header diff per file |

A new source implements this contract. It does not change the lanes, the manifest or the compilers.

## Design 3: client configuration, dashboards, change rules

Layered client configuration: four layers, each overriding the one above: core pack, industry pack, client, then subsidiary or book.
- Custom fields and segments are declared attributes that fill the manifest's dimension slots.
- Account mappings and fiscal calendars are data tables, not code.
- Costing method is a selectable strategy.
- Subsidiaries, currencies and books come from the discovered client profile.
- Anything not yet mapped has an explicit unmapped state. Nothing defaults silently.
- Packs carry semantic versions. An upgrade opens one change set per client and re-runs that client's tie-outs before it applies.
- Client-specific SQL is not allowed. A need the grammar cannot express becomes a request for a new type or slot.

Dashboard specs:
- A dashboard is a spec: layout plus components, each bound to a certified metric ID and version.
- A fixed component library renders it (P&L tree, variance waterfall, aging, margin bridge, trend, table).
- A chat-drafted dashboard is saved in the same spec, so promotion to certified is a review of a diff.
- Free-form HTML from chat stays available for exploration and always carries an uncertified badge.
- Open point: whether to adopt Cube's own workbook format or define a neutral spec that compiles to it (spike 5).

Change classes:

| Class | Example | Approver | Effect on pinned dashboards |
|---|---|---|---|
| Additive | New dimension, new metric | Data owner | None |
| Corrective | Bug fix that changes a number | Finance owner | New version; pinned dashboards show a change notice |
| Breaking | Definition change, grain change | Finance owner and controller | Major version; old version stays available until retired |
| Client mapping | Account or segment mapping | Client controller | Tie-outs re-run before it applies |

The author of a change never approves it, whether the author is a person or an AI agent.

Gap capture: every chat question the semantic layer refuses is logged with the question, the user's scope and the nearest metrics. A weekly triage turns the repeated ones into draft manifests.

## Onboarding

Onboarding is a ten-stage state machine, and a client moves forward only when a deterministic check passes. Code discovers, AI proposes, tie-outs decide, a person certifies.

| # | Stage | What happens | Who acts | Exit check |
|---|---|---|---|---|
| 1 | Connect | Read-only role, credentials, API budget set | Client admin | Test query succeeds within budget |
| 2 | Discover | Code reads record types, custom fields, segments, books, subsidiaries, calendar, costing method | Code | Typed client profile validates against its schema |
| 3 | Profile | Profile selects the industry pack and feature flags | Code | Every required profile field is filled |
| 4 | Propose mappings | AI drafts account and custom-field mappings with a confidence score | AI | Draft validates; every item is mapped or explicitly unmapped |
| 5 | Ask | Ambiguous choices become multiple-choice questions, ranked by dollar impact | Client controller | No open question above the materiality threshold |
| 6 | Backfill | History loads through the batch lane | Code | Row counts and key sets match the source |
| 7 | Reconcile | Results compared with NetSuite reports by account, period and subsidiary. AI iterates on the differences | AI, then code | Tie-out set within tolerance, or each variance explained and accepted |
| 8 | Shadow run | One or two closed periods run with the circuit breaker armed | Code | No breaker trips for the agreed period |
| 9 | Certify | Controller reviews tie-outs, mapping changes and open items in one change set | Client controller | Signed approval recorded |
| 10 | Publish | Certified metrics and dashboard templates go live; golden questions run | Code | Golden-question pass rate at or above the threshold |

Tie-out set, agreed before stage 1: Trial Balance, Income Statement, Inventory Valuation, AR aging, open AP.

After go-live: the discover stage re-runs on a schedule. A new custom field or segment opens a mapping proposal at stage 4 for that item only.

Productization measure: the size of each client's overrides relative to the pack. If it grows from client to client, the packs are wrong.

## Build sequence

Four phases over an estimated 16 weeks, each ending at a gate. The estimates have no measured basis; re-estimate after the spikes in weeks 1 to 3.

| Phase | Weeks | Builds | Gaps closed | Gate |
|---|---|---|---|---|
| 1. Foundation | 1 to 7 | Manifest schema, written by hand for all five datasets first. Compiler to pipeline, Cube model and tests. Metric types except layer-based. Canonical entities. NetSuite adapter. Gross orders, actual sales, P&L and cost analysis generated from manifests | 1, 2, 3, 4 | Four datasets run end to end from manifests and tie to NetSuite reports |
| 2. Variation | 8 to 11 | Layered configuration and pack versioning. Dashboard spec and component library. Change classes in CI. Layer-based metric type and the FIFO rebuild | 5, 6, 8 | FIFO ties to Inventory Valuation within tolerance. A pack upgrade applies to a test client without manual edits |
| 3. Operations | 12 to 14 | Onboarding state machine. Scheduled drift detection. Gap-capture log and triage. Catalog per client, cost attribution, API budget governor. AI drafting of manifests and mappings | 7, 9, 10 | A sandbox client completes all ten onboarding stages |
| 4. Proof | 15 to 16 | The three exit tests | All | All three pass at the thresholds below |

Spikes, weeks 1 to 3, in parallel with phase 1, each time-boxed to one week:
1. Does Cube compile per-client models from one template and hold up at 50 clients?
2. Does one Databricks bundle deploy to many client catalogs through targets and variables, and how does that scale?
3. Does an AI model produce correct manifests, not only valid ones, when constrained to the schema? Measure against the five hand-written manifests.
4. Can FIFO cost layers be rebuilt from transactions and tied to Inventory Valuation? Check the Records Catalog in a live account for any layer table first.
5. Can Cube's workbook format carry the dashboard spec, or is a neutral spec needed?

A failed spike changes the plan before week 4: spike 1 or 2 changes the deployment design, spike 3 moves AI drafting behind more human review, and spike 4 decides whether FIFO stays in the first release.

Team: one architect lead, one data engineer, one analytics engineer, and a finance owner at roughly a quarter time for manifests, tie-outs and certification.

Deliberately deferred: the controller review workspace, cross-client mapping memory, and a second ERP adapter. Each waits for client two.

## Exit tests

The layer is done when three changes need zero platform code. Thresholds are proposals for the owner to confirm.

| Test | Pass threshold |
|---|---|
| Add a sixth dataset (AR aging) by writing a manifest only | Zero changes to compiler, adapter or library code. Certified within 5 working days. Ties to the NetSuite AR aging report within 0.1% |
| Onboard a second NetSuite sandbox with different custom fields, fiscal calendar and costing method | Zero platform code changes. All ten stages complete. First certified metric within 10 working days. Overrides contain mappings and declarations only, no SQL |
| Add one flat-file source through the adapter contract | Code changes confined to the new adapter. One dataset built on it passes its tests |

A test that needs platform code has found a hole in the manifest, the library or the contract. Fix the hole, then re-run all three.

## Evidence status

Four capabilities the plan depends on are documented by the vendor (checked against vendor documentation on 19 Nov 2026; nothing tested hands-on):

| Plan depends on | What the vendor documents | Still unproven |
|---|---|---|
| One Cube template producing per-client models | Cube supports Jinja and Python templating of YAML models, a custom data model per tenant driven by the security context, and per-tenant drivers and repositories | Compile time and cache behaviour at 50 clients (spike 1) |
| One Databricks release deployed per client | Declarative Automation Bundles (formerly Asset Bundles) define targets and variables, including catalog and schema per target | Whether one target per client is workable at 50 clients (spike 2) |
| AI drafts that are always schema-valid | Claude structured outputs are generally available and constrain output to a JSON Schema, with documented schema limitations | Valid is not correct. Draft accuracy is unmeasured, and the manifest schema must fit within those limitations (spike 3) |
| Code-driven discovery of client customizations | NetSuite's REST metadata catalog describes standard and custom records and fields, and its MCP server has a SuiteQL metadata tool | Coverage of accounting books, fiscal calendars and costing settings. Oracle notes record types can differ slightly between the catalog and the Connect service |

No evidence yet:
- A queryable FIFO cost-layer table in NetSuite (spike 4).
- Reliable incremental sync of accounting lines. Two connector vendors' documentation disagrees.
- Whether Cube's workbook format can carry the dashboard spec (spike 5).
- The platform choice itself, pending the bake-off.
- Every duration in this plan.

## Risks and what would change the plan

The largest risk is building the compiler before the manifest is right, which is why phase 1 writes all five manifests by hand first.

| Risk | Why it matters | Mitigation |
|---|---|---|
| The manifest cannot express a real dataset | The whole design rests on one schema fitting all five, including FIFO | Hand-write all five before any compiler work. If FIFO does not fit, it ships as a hand-built exception with its own tie-out |
| The compiler becomes its own product | Generating pipelines, models and tests for every case is open-ended | Compile only what the five datasets need. Add a target only when a manifest requires it |
| Hand-coding to save time at client one | Faster now, and client two then costs as much as client one | Phase 1 gate: four datasets run from manifests, with no hand-built copies |
| Clients become forks | Client-specific logic makes pack upgrades impossible | No SQL in overrides. Track override size per client |
| AI drafts are valid but wrong | A wrong mapping can still pass schema validation | Tie-outs decide. A person certifies. The author never approves |
| NetSuite API contention | Polling shares the client's own integration pool | Capped adapter budget, with drift and discovery scheduled off-peak |
| The platform changes after the bake-off | Compilers target Databricks and Cube | Manifests and contracts stay platform-neutral. Only the compilers are rewritten |

What would change the plan:
- A different answer to any of the six decisions.
- A failed spike.
- A bake-off result that replaces Databricks or Cube: the designs stand, and the compiler estimates are redone.
- A target above roughly 50 clients: isolation and deployment need their own design pass first.
