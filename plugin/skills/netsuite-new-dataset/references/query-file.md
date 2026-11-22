# Certified query file format

One file per query, named `<id>.query.json`, placed under `skills/certified-queries/references/` in the organization's certified-queries plugin.

```json
{
  "id": "gross-orders-by-year",
  "title": "Gross bookings by order year",
  "description": "Sales order line amounts by order year, excluding tax and shipping lines.",
  "call": {
    "connector": "NetSuite",
    "tool": "ns_runCustomSuiteQL",
    "args": { "sqlQuery": "SELECT … WHERE t.trandate >= TO_DATE({{start}},'YYYY-MM-DD')" }
  },
  "filters": { "start": { "label": "From", "type": "date", "default": "2017-01-01" } },
  "hints": ["Group by subsidiary before showing a total across subsidiaries."]
}
```

Rules:

- `id` equals the file name: lower-case letters, digits, `-` and `_`.
- `call` holds exactly `connector`, `tool` and `args`.
- Each value a reader supplies is `{{name}}` in `args` and described under `filters`.
- A filter's `type` is `text`, `number`, `boolean` or `date`. `allowed` lists the only values it takes; `default` is used when none is given.
- With no `kind`, a `{{name}}` is filled in as a literal that brings its own quotes, so do not put quotes around it.
- `hints` are notes on how to chart or explain the result, nothing else.

A file that breaks this format is listed as invalid and certifies nothing.
