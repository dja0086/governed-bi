---
name: netsuite-gross-orders
description: Definition and query for gross orders and gross bookings from NetSuite sales orders. Use when asked about orders, bookings, order intake or sales order value by year, customer, item, subsidiary or department.
---

# Gross orders (status: draft, not certified)

Until this dataset is certified, label every number from it `Not certified: gross orders draft definition`.

## Definition

- **Grain:** one sales order line.
- **Gross bookings:** the line amount of every sales order line, excluding tax lines and shipping lines. Closed and cancelled lines are included.
- **Net bookings:** as above, also excluding closed and cancelled lines. Not built yet; say so if asked.
- **Date:** the sales order's transaction date.
- **Sign:** NetSuite stores sales order line amounts as negative; the query negates them so orders are positive.

## Base query

Use this shape and change only the grouping and the date filter. See `references/variants.md` for grouping by customer, item, subsidiary or department.

```sql
SELECT * FROM (
  SELECT TO_CHAR(t.trandate,'YYYY') AS order_year,
         COUNT(DISTINCT t.id) AS orders,
         COUNT(*) AS order_lines,
         SUM(NVL(-tl.netamount,0)) AS gross_bookings
  FROM transaction t
  JOIN transactionline tl ON tl.transaction = t.id
  WHERE t.type = 'SalesOrd'
    AND tl.mainline = 'F'
    AND tl.taxline = 'F'
    AND NVL(tl.itemtype,'x') NOT IN ('ShipItem','TaxItem','TaxGroup')
  GROUP BY TO_CHAR(t.trandate,'YYYY')
  ORDER BY 1
) WHERE ROWNUM <= 1000
```

## Always tell the user

- **Currency:** `netamount` is in each subsidiary's own currency. Never add it across subsidiaries: on the first test account, one subsidiary reporting in a different currency made up most of the unconverted sum. Always group by subsidiary, or filter to one, until a consolidated basis is defined.
- **Tie-out:** this number has not been tied to a NetSuite report.

## Check before answering

The count of orders must equal `SELECT COUNT(*) FROM transaction WHERE type = 'SalesOrd'` for the same date range. If it does not, say so and show both.
