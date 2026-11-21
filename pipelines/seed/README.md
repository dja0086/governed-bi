# Seed load (stopgap, not production)

A one-time, read-only copy of 15 NetSuite tables into `client_one.bronze`, used while the Databricks managed connector is blocked (see `phase0/results/S-03b-ingestion-root-cause.md`). Evidence and counts: `phase0/results/S-03c-seed-load.md`.

## Steps

1. On a machine that can reach NetSuite, put `NQjc.jar` (SuiteAnalytics Connect JDBC driver 8.10.190.0) next to these two files and run `bash ns_seed_export.sh`. It prompts for the four token values with hidden input, passes them as environment variables for that one run and saves nothing. Output: `seed_out/<table>.jsonl.gz` and `seed_out/_manifest.json`.
2. Upload `seed_out/*` to the volume `client_one.bronze.netsuite_seed`.
3. Run `load_bronze.sql` once per table.
4. Compare `COUNT(*)` per table with `_manifest.json`.

## Tables

subsidiary, accountingbook, accountingperiod, currency, account, department, classification, location, item, customer, vendor, consolidatedexchangerate, transaction, transactionline, transactionaccountingline

## Limits

- A snapshot: no schedule, no change tracking, no deletes. Re-run to refresh.
- `SELECT *` under role 57 (Data Warehouse Integrator); columns are whatever that role can see.
- Numbers are written as plain decimals, everything else as text; `read_files` infers the types. Dates arrive as text and need casting in silver.
- Never commit `seed_out/` or the driver. Client rows do not belong in this repository.
