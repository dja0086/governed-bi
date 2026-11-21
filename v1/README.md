# Version 1: governed BI on Claude, direct to NetSuite (D-029, D-030)

Design: https://claude.ai/artifact/6XHtS74JWe9Acv4DqmXxzb

`queries/` holds each query as the exact connector call a Claude Data dashboard stores. A query is certified only when its file says so and the owner has certified the metric. The file format a Git-backed Claude plugin expects for certified queries is unverified; these files are our own record until that is read from Anthropic's reference.
