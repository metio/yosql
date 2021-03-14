---
title: Vendorization
date: 2019-06-16T18:51:18+02:00
menu:
  main:
    parent: SQL
categories:
  - SQL 
tags:
  - Vendorization
  - Vendor
---

Vendor specific statements are support. Just add a *vendor* key to your front-matter like this:

```sql
-- name: findUsers
-- vendor: Oracle
SELECT  *
FROM    (
    SELECT  ROWNUM rn, data.*
    FROM    (
        SELECT  *
        FROM    users
        WHERE   id = :userId
    ) data
    WHERE   rn <=  :offset + :limit
)
WHERE rn >= :offset
;

-- name: findUsers
-- vendor: PostgreSQL
SELECT  *
FROM    users
WHERE   id = :userId
OFFSET  :offset
FETCH NEXT :limit ROWS ONLY
;

-- name: findUsers
SELECT  *
FROM    users
WHERE   id = :userId
OFFSET  :offset
LIMIT   :limit
;
```

The first two statements specify a vendor which means that those queries will only be executed when running against the specified database. In case you want to specify a fallback-query that is used whenever no other vendor matches, specify another statement with the same name but no vendor.
