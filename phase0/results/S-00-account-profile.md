# S-00: client-one account profile

Read on 2026-11-20 through the "NetSuite" connector's session-information and accounting-book tools. Read-only. Feature and permission identifiers are quoted as NetSuite returned them.

## Verified

| Fact | Value | Why it matters |
|---|---|---|
| Account | TSTDRV1755417, release 2026.2 | A NetSuite test-drive account: demo data, not a trading company |
| Accounting books | One: Primary Accounting Book. No multi-book feature in the enabled list | Multi-book translation is out of scope for client one |
| Multi-currency | Enabled (`MULTICURRENCY`), with automatic rate updates | Consolidated P&L needs currency conversion |
| Intercompany | `INTERCOMPANYELIMINATIONENGINE` and `INTERCOMPANYAUTOELIMINATION` enabled | Eliminations can be tested here |
| Default inventory costing | Average (`INVTCOSTMETHOD: AVG`) | FIFO is not the account default. Whether any item uses FIFO is not yet known |
| Inventory features | Multi-location, lot-numbered, serialized, bins, advanced receiving | Advanced receiving changes when costing happens |
| Revenue | Advanced revenue recognition enabled | "Actual sales" must separate billings from recognized revenue |
| Custom segments | Enabled | The dimension slots in the manifests have something to fill |
| Multiple calendars | Enabled (`MULTIPLECALENDARS`) | Fiscal calendar must be discovered per subsidiary, not assumed |
| Event scripting | Server-side scripting and the SuiteCloud Development Framework enabled | The seconds lane can be deployed here |
| API pressure | A fourth call in quick succession returned "Too Many Requests" | The concurrency budget is real even for light discovery |

## Role gaps

The connected role is `customrole_ns_agent`.

- **Not read-only.** It has full permission on customers (`LIST_CUSTJOB`) and edit on the File Cabinet. The builder does not write, but the role should not allow it.
- **Financial reports.** The role lists analytics permission (`REPO_ANALYTICS`) but no financial-statement or general-ledger report permission. Unverified whether the Income Statement and Trial Balance will run under it. Spike S-02 tests this.
- **Missing views.** No view permission appears for item receipts, fulfillments, inventory adjustments or accounting periods. FIFO, cost analysis and period handling need them.

## Not verified

- SuiteAnalytics Connect. No identifier that names it appears in the enabled-feature list. Unverified whether that list would show it; the owner should check Setup > Company > Enable Features > Analytics.
- Subsidiary list. The call was throttled. Retry singly.
- Report list. The call returned nothing. Retry singly.
- Any cost-layer table (S-01).

## Consequence for the plan

A test-drive account proves the method (manifests, compiler, tie-outs, consolidation, certified components). It does not prove behaviour on a real company's volume, customizations or close process. Go-live on this account is a proof, and a real account is still needed before selling.
