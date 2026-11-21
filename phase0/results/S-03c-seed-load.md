# S-03c: seed load into `client_one.bronze`

Date: 2026-11-20 (US), run by Claude with the owner. Status: done. Stopgap, not production.

## Why

The managed connector cannot run: the trial workspace blocks serverless outbound DNS to `netsuite.com` (S-03b). The owner added a payment method on 2026-11-20; the block was still there on two re-tests after about 20 minutes (verified: notebook DNS test). The Databricks help centre returned "Login Error" for the owner's account, so no support request has been filed (verified: owner's report and the page).

## What was done

1. The owner ran `pipelines/seed/ns_seed_export.sh` on their Mac. It logged in to SuiteAnalytics Connect with token-based authentication under role 57 and exported 15 tables. Verified: the script's `_manifest.json`, all 15 marked ok.
2. The 15 files plus the manifest were uploaded to the volume `client_one.bronze.netsuite_seed`. Verified: volume listing shows 16 files.
3. Each table was created with `pipelines/seed/load_bronze.sql`. Verified: all statements completed.
4. Row counts in Databricks match the manifest for all 15 tables. Verified: one `COUNT(*)` query, 2026-11-20 22:16 local.

| Table | Rows exported | Rows in bronze |
|---|---|---|
| subsidiary | 23 | 23 |
| accountingbook | 1 | 1 |
| accountingperiod | 664 | 664 |
| currency | 11 | 11 |
| account | 290 | 290 |
| department | 18 | 18 |
| classification | 22 | 22 |
| location | 18 | 18 |
| item | 890 | 890 |
| customer | 1,016 | 1,016 |
| vendor | 141 | 141 |
| consolidatedexchangerate | 29,078 | 29,078 |
| transaction | 4,157 | 4,157 |
| transactionline | 28,074 | 28,074 |
| transactionaccountingline | 28,053 | 28,053 |

## What this does and does not prove

- Verified: NetSuite data can be landed and queried in `client_one.bronze`; modelling and tie-out work is unblocked.
- Verified: row counts match the export. Unverified: that the export matches NetSuite's own totals. The Income Statement tie-out (D-023) is the test.
- Unverified: column types. `read_files` inferred them from JSON; dates are text.
- Not production: no schedule, no change tracking, no deletes. The managed connector, or an extractor that runs where NetSuite is reachable, is still required.

## Client data handling

The seed files passed through the builder's workspace only to upload them, at the owner's instruction, and were deleted there afterwards. No rows or amounts are in this repository.
