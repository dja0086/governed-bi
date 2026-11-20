# Chairman's synthesis: review of the configurability build plan

## Verdict

The council agrees the direction is right but the plan will not deliver what the owner asked for as written. The typed manifest, closed metric-type library, adapter contract, "no SQL in overrides" and "tie-outs decide, a person certifies" are the correct foundations. It will be configurable only for datasets that fit the current grammar, and it leaves the owner's top priority, real-time chat self-service, unbuilt. The 16-week estimate, the team and the exit tests are not credible.

Peer ranking: two reviewers ranked Response C first and one ranked Response B first; all three ranked Response A last.

## What is wrong or missing (all three members agree)

- **Real-time and chat have no work package.** The event and live lanes exist only as a manifest block. Nothing schedules the Event Subscriber scripts (which must be deployed into each client's NetSuite account), the provisional-overwrite logic, chat-to-metric resolution, disambiguation or refusal.
- **Architecture items sit in no phase.** These are the signed security context, per-user subsidiary and department filtering, cross-tenant leak testing, period-close snapshots and version pinning. Isolation arrives in weeks 12 to 14, after client data is flowing.
- **The grammar is narrower than it looks.**
  - Missing types include eliminations, CTA and FX revaluation, multi-book translation, distinct counts, weighted-average cost, budget versus actual, and as-of aging buckets.
  - `exclude: [closed_lines]` is a hidden predicate language and needs a restricted filter grammar now.
  - Costing strategies are never enumerated, so exit test 2 (a different costing method) contradicts the build.
  - Exit test 1 (AR aging) will probably fail on its own premise, because past-date aging needs payment-application history and bucketing.
- **The riskiest unknowns have no spike.** These are accounting-line incremental sync, how NetSuite report figures are pulled programmatically for tie-outs, the delete sweep against the shared API pool and 100,000-row cap, and Event Subscriber limits. Per-tenant SuiteAnalytics Connect licensing is also unspiked (pricing unverified).
- **Controller time is the real bottleneck.** The "10 working days" target depends on the controller's calendar, and the client's time is unplanned.

## Points raised by one member and endorsed in review

- **Breaker conflict (A).** Tie-outs run against live NetSuite reports, so provisional rows and unswept deletes will trip the circuit breaker in the daytime. Tie-outs need a common snapshot, provisional rows excluded, and a stated delete latency.
- **Live lane bypass (B).** NetSuite's MCP server answers under a NetSuite role, outside Cube's security context, which breaks the "Cube is the only surface" rule. Decide where it sits and test that the two scopes match.
- **FIFO (B).** It is a stateful transformation that builds the cost-layer entity, not a metric type. Certify NetSuite's posted COGS and inventory GL impacts, and use rebuilt layers for analysis only.
- **Tolerance (B and C).** 0.1% is too loose for ledger figures. Trial Balance and Income Statement should tie to the cent or to rounding; keep percentages for operational datasets. A did not challenge this.
- **Assurance gaps (C).** A tie-out proves reproduction of NetSuite's report, not a correct definition. There is no restatement policy for closed periods. Generated golden questions give false assurance (asserted, not evidenced).
- **Drift and entities.** Drift detection must cover renamed and deleted fields (B). Canonical entities need versioning rules (A).

## Over-engineered for client one

All three would defer metering, keeping only tagging or the API budget governor. B and C would also cut industry packs and pack versioning. A's claim that the registry, manifest and canonical layer overlap was judged weak by one reviewer.

## Where the council disagreed

- **Sequencing.**
  - B: hand-build gross orders and P&L first, then extract the manifest and compiler from working code. One reviewer noted this weakens the plan's own guard against hand-coding at client one.
  - C: write manifests first, but build the compiler only from what a client-one vertical slice needs.
  - A: keep the plan's order and fix it.
- **Event lane timing.** A moves it to phase 2, B puts the seconds lane in the first slice, and C adds it for one dataset in weeks 11 to 16.
- **Configuration layers.** B wants three, C wants two with scope columns. One reviewer thought two may be too few.
- **Onboarding state machine.** B would build five gated steps; C would run a scripted checklist until client two.
- **Dashboard spec.** A says pick one format now, B says adopt Cube's workbook format unless spike 5 fails, and C defers the question.
- **Spike 3.** A calls it circular, B calls the test set contaminated and wants held-out datasets, and C calls the design right once ground truth exists. Reviewers noted held-out datasets may not exist yet.
- **Spike 1 scale.** C would test at 3 to 5 tenants; a reviewer said this defeats a spike meant to check 50.

## Credibility

- **Estimate.** Not credible; every figure here is judgement and unverified. A says about double, B says 24 to 32 weeks, and C says 28 to 36 for full scope or 16 to 20 if cut.
- **Team.** It lacks a NetSuite/SuiteScript developer, a front-end/BI developer, and security, identity and DevOps capacity. The finance owner needs half time or more.
- **Spikes.** One week is too short for FIFO and for spike 3.
- **Exit tests.** They are self-graded and gameable, because "zero platform code" can be met by stretching the grammar or the packs.

## What to change

The council did not settle the build order; this sequence follows C's, which two of three reviewers preferred.

1. **Weeks 1 to 3:** run the spikes plus accounting-line sync, the tie-out harness, the delete sweep and Event Subscriber limits, and fold the platform bake-off in. Hold a decision gate and re-estimate at week 4.
2. **Next:** build a client-one vertical slice of gross orders and P&L with chat, badges and the security context live.
3. **Before the grammar freezes:** run the hand-written FIFO manifest and its tie-out as a go/no-go.
4. **Specify now:** the closed sets (metric types, costing strategies, filter grammar), tie-out semantics, per-metric tolerance, a restatement policy and whether consolidation is in scope.
5. **Replace the exit tests** with:
   - a structurally different dataset;
   - onboarding by someone outside the build team, with controller hours logged;
   - a cross-tenant isolation test;
   - batch and seconds-lane freshness tests;
   - a chat evaluation on 30 to 50 real client-one questions;
   - a deleted-line correction and a breaking change reaching pinned dashboards;
   - the flat-file test as written.
6. **Defer** industry packs, metering and the full component library until measured override growth at client two justifies them.
