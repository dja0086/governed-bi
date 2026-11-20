# S-01: is any FIFO cost-layer table queryable?

Run on 2026-11-20 against TSTDRV1755417 through the SuiteQL metadata tool, under role `customrole_ns_agent`. Read-only.

## Result

**None found.** The role can see 1,777 record types. No name or label among them contains "layer", "FIFO" or "LIFO", and nothing describes a cost layer.

Search terms used on names and labels: cost, layer, fifo, lifo, valuation, period, consolidat, exchange, inventory balance / number / assignment / cost, accounting line, deleted, aggregate.

## What is there instead

| Record type | Use |
|---|---|
| `TransactionAccountingLine` | Posted ledger lines: the source for P&L and posted cost |
| `consolidatedExchangeRate`, `ConsolidatedRateType` | Consolidation rates |
| `currencyRate`, `CurrencyRateType` | Transaction-level exchange rates |
| `InventoryBalance`, `ItemInventoryBalance`, `aggregateItemLocation` | On-hand quantities by item and location |
| `inventoryAssignment`, `inventoryNumber`, `transactionInventoryNumber` | Lot and serial assignment per transaction line |
| `InventoryCostAccounting` | Items with incomplete cost accounting |
| `VendorBillItemLandedCost` | Landed cost on bills |
| `AccountPeriodActivity`, `PostingAccountActivity` | Account activity summaries |

## Limits of this result

- It is the list visible to one role. A table hidden from this role would not appear. An administrator-level check of the Records Catalog would settle it.
- Record types this role cannot see, which other spikes need: accounting period, deleted record, item receipt, item fulfillment, inventory adjustment.

## Consequence

D-008 stands: certify posted cost of goods sold and inventory value; FIFO layers are a rebuild, for analysis only. The FIFO manifest is marked `certification: analysis_only`.

## Still open

O-006: whether any item uses FIFO costing. The account default is average. A grouped count on the item table by costing method was rejected by NetSuite as an unsupported search, so the field name needs checking in item metadata first.
