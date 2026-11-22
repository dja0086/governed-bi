---
name: netsuite-new-dataset
description: Guides creating a new reusable NetSuite dataset, metric or report, from a first question to a saved dashboard and, if wanted, a proposal for certification. Use when the user asks to create, define, save or certify a dataset, metric, KPI or recurring report on NetSuite.
---

# Creating a new dataset

A dataset here is one saved, read-only NetSuite query with a title and a definition. Follow the steps in order. Do not skip step 2.

## Stage 1: build it (anyone, no approval)

1. **Check for an existing one.** Look in certified queries, then the user's own dashboards. If one fits, use it and stop.
2. **Agree the definition before writing SQL.** Ask only what is missing, in one message:
   - What is being measured, and at what grain (order, line, invoice, account)?
   - What is included and excluded (tax, shipping, cancelled, intercompany)?
   - Which date decides the period?
   - One subsidiary or several, and which currency basis?
3. **Write the query** under the `netsuite-bi-governance` rules: read-only, aggregated, row cap outside the aggregation.
4. **Run it and sanity-check it.** Compare a count or total with a simpler query or a NetSuite standard report. Report the comparison, including when it does not match.
5. **Show it as a Claude Data dashboard** with the query as a live source. Put the definition in one sentence under the title, and the line `Not certified` beside it.
6. **Hand over.** Say what it shows, what was assumed, that it runs on each reader's own NetSuite access, and that it is private until they share it.

## Stage 2: propose it for certification (optional)

Only when the user asks for the dataset to become official.

1. **Freeze the call.** Turn each value a reader should choose into a `{{name}}` filter with a type and a default.
2. **Write the proposal file** in the format in `references/query-file.md`.
3. **Record the evidence** with it: the definition, the tie-out comparison and its result, and open questions.
4. **Send it for review.** If certified queries live in a repository, open a pull request adding the file, or give the user the file and say where it goes. If they are managed in Claude, tell the user to open the query in the dashboard's Sources panel and use Certify query, which needs the right permission.
5. **Never certify on the user's behalf and never describe a proposed query as certified.** Certification is a named person's decision.

## After certification

Changing a certified query removes its certification everywhere. Suggest a new query beside it instead, unless the owner asks for the change.
