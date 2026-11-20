# Chairman's synthesis: follow-up review of the configurability build plan

## Verdict

The plan's design foundations hold, but it configures a base platform that nobody has scheduled. The previous council was mostly right on substance and wrong on its estimates and on phase 0 sizing. Nothing here was tested or checked against sources. All three members are from one model family, so their agreement is correlated, not independent evidence. Peer ranking: two reviewers put Response C first and one put B first; all three put A last.

## 1. The previous council's criticisms

**Correct (all three members agree):**
- Tie-outs against live NetSuite reports will trip the breaker; they need a common snapshot, provisional rows excluded and a stated delete latency.
- FIFO is a stateful transformation, not a metric type. Certify NetSuite's posted COGS and use rebuilt layers for analysis only.
- `exclude: [closed_lines]` is a hidden predicate language and needs a closed filter grammar.
- The live lane runs under a NetSuite role, outside Cube's security context.
- 0.1% is too loose for ledger figures, which should tie to the cent.
- Isolation arrives too late, the exit tests are self-graded, and controller time is the bottleneck.
- The riskiest unknowns have no spike.

**Overstated or mis-aimed:**
- "Real-time and chat have no work package" is true, but the real finding is that no base build plan exists.
- The missing metric types (eliminations, CTA, multi-book) matter only if consolidation is in scope, which nobody has decided.
- "AR aging will probably fail": current-date aging looks feasible; only as-of aging needs application history (unverified).
- "Exit test 2 contradicts the build": the actual defect is that costing strategies are never enumerated.
- The 100,000-row cap applies to SuiteQL REST, and the batch lane uses SuiteAnalytics Connect, so the delete-sweep concern depends on the path used (unverified).
- "A tie-out proves reproduction, not definition" bites only on derived metrics (C's judgement, unverified).

**Unsupported:**
- Every duration (about double, 24 to 32 weeks, 28 to 36 weeks) is as baseless as the plan's 16.
- Folding a six-week bake-off into weeks 1 to 3 is not credible. Only C caught this.
- "Generated golden questions give false assurance" was asserted without evidence; it holds only when questions are generated from the same manifest.

**Still disputed:** whether a front-end developer is needed. A and C said no if Cube's format is adopted, but C's own call for server-rendered components implies front-end work.

## 2. What both missed

- **Third-party chat clients (C).** Claude, ChatGPT and Copilot do their own arithmetic and HTML on certified numbers, so badges and "one answer" cannot be enforced there. My ruling, as judgement: certified answers come only from server-rendered components, and anything else is badged uncertified.
- **Two sources of truth (A).** Restate the rule: the manifest is the definition and Cube is the only serving surface.
- **Compiler regression (A).** One compiler change regenerates every client's artifacts, so it needs per-client golden-output snapshots and versioned releases.
- **Caching versus freshness (B, C).** Cube pre-aggregations may hide seconds-lane rows and falsify as-of badges (unverified).
- **Entitlement fidelity (B, C).** NetSuite role restrictions do not map cleanly to a row filter; a test oracle must compare what each role sees in both systems (unverified in detail).
- **Costing recalculation and backdated postings (A, B, C).** Either may rewrite history without updating the polled columns (unverified); period reopen needs detection.
- **"Event Subscriber scripts" may not be a real script type (A, unverified).** Either way it means maintaining a SuiteApp in every client account (C).
- **Commercial and security (all unverified).** Per-tenant unit economics, Oracle terms, data leaving to AI vendors, prompt injection through field text, secret custody, missing service levels, the incumbent NetSuite Analytics Warehouse, and how to obtain a realistic second account.
- **The grammar (B).** Five datasets from one client are too few to freeze it, and it is a third DSL to maintain.

## 3. Rulings

- **(a) Build order.** Manifests first, compiler built only from a vertical slice. Hand-write all five manifests, hand-build a reference output for gross orders only, make the compiler reproduce it, then generate P&L from its manifest. C dissents: it would hand-build both and delete the code; one reviewer called that an intention, not a control.
- **(b) Event lane.** Spike it in phase 0. Build it for gross orders only, after batch ties out and the breaker handles provisional rows, and never for the ledger. It is in client one's exit criteria. No real disagreement remains.
- **(c) Layers.** Two files (core, client), with subsidiary and book as scope keys and a resolver agnostic to layer count. C dissents and wants three.
- **(d) Onboarding.** A scripted checklist with all ten exit checks as executable code, run by a person. Build the state machine after two real onboardings. A would group them into five steps; this is a minor difference.
- **(e) Dashboard spec.** Adopt Cube's workbook format, kept in Git, with a lint binding each component to a metric ID and version. Write a neutral spec only if spike 5 shows it is not diffable or cannot pin versions (unverified), or if the bake-off drops Cube. Unanimous.
- **(f) AI drafting.** Test mappings first: hold out client one's real accounts, score first-pass tie-out and human edits, and seed errors including offsetting mis-mappings. Keep a leave-one-out test on the five manifests, reported as a correction rate, not an accuracy claim. C would drop the manifest test.
- **(g) Cube spike.** Both scales: synthetic tenants measured at 5, 10, 25 and 50 for performance, plus three genuinely different tenants for correctness. A and B proposed synthetic only.

## 4. Revised plan

**Phases (one integrated programme, funded a phase at a time):**
- **Phase 0, evidence gate, six weeks.** All the spikes, role mapping and a bake-off limited to two stacks. A and B said four weeks; reviewers found that overloaded, and six is still ambitious.
- **Phase 1, base slice.** Gross orders and P&L on batch, catalog per client, security context, snapshot tie-outs, breaker and chat with badges.
- **Phase 2.** Actual sales, cost analysis, the event lane for gross orders and a chat evaluation.
- **Phase 3.** Certified posted COGS, FIFO as analysis, drift detection and the flat-file adapter.
- **Phase 4.** A real second account.

**Out for client one:** industry packs, metering, the state machine, the review workspace, a neutral spec, a second ERP, the live lane until scope parity passes, and consolidation unless the owner decides otherwise now.

**Team:** architect, two data engineers, analytics engineer, NetSuite/SuiteScript developer, platform and identity engineer, finance owner at half time, and budgeted controller hours.

**Estimating:** publish no date before the gate. Measure effort on the slice, project P50 and P80 per work package, model controller time as queue time and re-forecast at each gate.

**Exit tests:**
- Tag the grammar, then finance picks a held-out dataset; count and report every platform change. A's binary fail rule was judged weaker.
- An outsider onboards client two against the tag, with controller hours logged.
- The controller checks 20 random cells to the cent.
- Seeded faults are each caught within a stated latency.
- Outsiders attempt cross-tenant and cross-subsidiary leaks, including the live lane.
- Freshness is measured as p95 over two weeks.
- Fifty real questions, including must-refuse ones, are graded by finance.
