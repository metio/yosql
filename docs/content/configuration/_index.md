---
title: Configuration
date: 2026-08-03
menu:
  main:
    weight: 120
---

Most projects configure almost nothing. `YoSQL` works the rest out from your SQL, your schema and
the records you already have, and every setting below defaults to what the majority of projects
would have picked anyway.

So start with an empty configuration, write a statement, and come back here when something is not
where `YoSQL` looked for it.

## What a project usually sets

Four, and a project on the conventional Maven or Gradle layout sets one:

| Setting | What it answers |
| --- | --- |
| [basePackageName](repositories/basepackagename/) | Which package the generated repositories live in. |
| [inputBaseDirectory](files/inputbasedirectory/) | Where the `.sql` files are, if not `src/main/yosql`. |
| [sourceDirectory](files/sourcedirectory/) | Where the records a statement names are, if not `src/main/java`. |
| [validation](schema/validation/) | Whether a statement disagreeing with your schema warns or stops the build. |

## What a statement says

Configuration belonging to one query rather than to the project goes in that statement's
[front matter](../sql/sql-files/), and most statements need three lines of it:

```sql
-- name: findTenant
-- returning: single
-- resultRowType: com.example.domain.Tenant
select id, slug, created_at from tenant where id = :id
```

[name](sql/name/), [returning](sql/returningmode/) and [resultRowType](sql/resultrowtype/) carry
almost every statement. [parameters](sql/parameters/) is needed only where neither the schema nor the
result row type says what a parameter holds, [vendor](sql/vendor/) only where a statement is written
once per database, and [repository](sql/repository/) only where a statement belongs somewhere other
than its directory suggests.

## Everything else

The remaining groups are worth reading when you need them, and not before:

- [repositories](repositories/) — which methods a repository gets, what they are called, and which
  name prefixes mean reading, writing or calling.
- [converter](converter/) — the fallback for a statement that names no result row type.
- [annotations](annotations/) — what the generated classes say about themselves.
- [files](files/) — file suffix, charset, statement separator, output directory.
- [logging](logging/) — which logging API the generated code writes to, if any.
- [schema](schema/) — where the `create table` statements are, when they are not next to the queries.
- [resources](resources/) — how many threads generation may use.
