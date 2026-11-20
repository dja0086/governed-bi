# S-02: can NetSuite report figures be pulled programmatically for tie-outs?

Run on 2026-11-20 against TSTDRV1755417 through the report tools, under role `customrole_ns_agent`. Read-only. No amounts are recorded here.

## Result

**Partly.** The Income Statement tie-out is feasible today. The other three reports are not reachable under the current role.

| Report needed | Available to the role | Notes |
|---|---|---|
| Income Statement | Yes, as "Custom Income Statement - analysis" | Supports consolidation, date ranges, column grouping and accounting context |
| Trial Balance | No | Not in the role's report list |
| Inventory Valuation | No | Not in the role's report list |
| AR aging | No | Not in the role's report list |

The role sees five reports in total: the custom income statement, three open-estimate reports and an employee change history.

## What the income statement returns

- One consolidated run for a full year came back in a single call.
- It returns every section (Income, Cost Of Sales, Gross Profit, Expense, Net Ordinary Income, Other Income, Other Expense, Net Income) and every account under them, as a parent-child tree with an amount per row.
- Consolidated views are selected with a negative subsidiary ID. The account has four: headquarters, Americas, APAC and EMEA.
- That is enough to tie a P&L by account, period and consolidated subsidiary.

## Findings that change the design

1. **Amounts are not rounded to the cent.** Consolidated figures carry many decimal places from currency conversion. "Ties to the cent" (D-009) needs a stated rounding rule: at which level rounding is applied and how. See O-011.
2. **The data is in 2017.** Of roughly 3,400 transactions the role can see, about 2,200 posting ones are dated 2017; 2024 to 2026 hold a few dozen each. A report run for October 2026 returned zeros throughout. Tie-out tests should use 2017 periods.
3. **The report sees more ledger than the role's queries may.** The report shows payroll, rent and depreciation accounts, while the role's transaction permissions cover only sales, purchasing and journal types. Whether a query-built P&L can reach every posting the report includes under this role is unverified, and it decides whether the tie-out can pass.
4. **Other income and expense exist** (non-operating revenue, exchange variances, realized gains). The manifest grammar has no filters for them yet, so net income cannot be defined. See O-012.

## Structure of the account

23 subsidiaries, five of them elimination subsidiaries, under four consolidated views. One accounting book.

## What is needed to finish this spike

Trial Balance, Inventory Valuation and AR aging under the connected role. Per D-020 the role stays as configured, so this comes back to the owner as a finding (O-013): without those three reports, only the Income Statement can be tied out automatically.
