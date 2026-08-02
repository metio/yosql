---
title: SQL
date: 2020-04-13
menu:
  main:
    weight: 110
---

Statements live in `.sql` files, and a comment block above each one says what to generate for it:

```sql
-- name: findTenant
-- returning: single
-- resultRowType: com.example.domain.Tenant
-- parameters:
--   - name: id
--     type: java.util.UUID
select id, slug, created_at
from tenant
where id = :id
```

`name` becomes the method, `parameters` become its arguments, `:id` becomes the placeholder the
driver binds, and `returning` decides whether you get one row, many, or a stream.

## What you get back

A statement returns one of three things, depending on what you tell it:

| You write | You get |
| --- | --- |
| nothing | a `Map<String, Object>` per row |
| `resultRowType: com.example.domain.Tenant` | a `Tenant`, from a mapper written for you |
| `resultRowConverter: com.example.ToTenant` | whatever your own converter returns |

`returning` then wraps that in `Optional`, `List` or `Stream` — or drops it for a write, which
answers with the number of rows it changed.

## Where to go next

[SQL files](./sql-files/) — how a file is laid out, how several statements share one, and every key
the front matter accepts.

[Structure](./structure/) — how files and directories decide which repository a statement lands in,
and how to override that.

[Converters](./converters/) — how a row becomes something other than a `Map`. Naming a record is
usually all it takes; there is a hand-written escape hatch for mappings a record cannot express, and
the same types work for parameters on the way in.
