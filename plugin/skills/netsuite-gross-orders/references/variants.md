# Grouping variants for gross orders

Keep the WHERE clause of the base query unchanged. Replace the grouping column in SELECT and GROUP BY.

| Group by | Column | Join to add |
|---|---|---|
| Subsidiary | `s.name` | `JOIN subsidiary s ON s.id = tl.subsidiary` |
| Customer | `c.companyname` | `JOIN customer c ON c.id = t.entity` |
| Item | `i.itemid` | `JOIN item i ON i.id = tl.item` |
| Department | `d.name` | `LEFT JOIN department d ON d.id = tl.department` |
| Month | `TO_CHAR(t.trandate,'YYYY-MM')` | none |

Date filter, added to WHERE:

```sql
AND t.trandate >= TO_DATE('2017-01-01','YYYY-MM-DD') AND t.trandate < TO_DATE('2018-01-01','YYYY-MM-DD')
```

Verified on a live account 2026-11-22: the subsidiary join, with the date filter; its order and line counts equal the ungrouped query's. The customer, item and department joins are unverified: confirm each column with the SuiteQL metadata tool the first time it is used, and tell the user if a column is missing.
