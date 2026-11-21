-- One statement per table. Run in the Databricks SQL editor after the seed files are in the volume.
-- Replace <t> with each table name in README.md. Re-running replaces the table.
CREATE OR REPLACE TABLE client_one.bronze.<t> AS
SELECT * FROM read_files('/Volumes/client_one/bronze/netsuite_seed/<t>.jsonl.gz', format => 'json');
