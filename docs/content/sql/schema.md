---
title: Schema validation
date: 2026-08-03
menu:
  main:
    parent: SQL
categories:
  - SQL
tags:
  - schema
  - validation
---

`YoSQL` reads the `create table` statements your project already keeps, and checks the rest of your
SQL against them. A column that does not exist, a parameter whose type disagrees with its column, a
record component that cannot hold what its column can — each of those becomes a build failure naming
the file and the statement, instead of an exception on whichever request reached the query first.

It also answers a question you used to have to answer yourself: what type a parameter is.

## Turn it on

```xml
<schema>
  <validation>WARN</validation>
</schema>
```

Start at `WARN`. Turning this on in an existing project is a migration rather than a switch — the
first run finds everything at once, and a build that fails on all of it is a build nobody can
bisect. Fix what it reports, then move to `ERROR` so nothing new gets in.

It is `OFF` by default, so upgrading changes nothing until you ask it to.

## Where the schema comes from

Your own DDL. **Nothing connects to a database** — that is why generation still works in a checkout
with no services running, and why this costs nothing in a build that has no database available.

By default the `create table` statements among your own statements are used, so a project that keeps
its schema next to its queries configures nothing:

```text
src/main/yosql/
├── schema/createSchema.sql   ← read as the schema
└── tenant/findTenant.sql     ← checked against it
```

Keep your schema elsewhere — Flyway, Liquibase — and point at it:

```xml
<schema>
  <validation>ERROR</validation>
  <sqlStatementsDirectory>src/main/resources/db/migration</sqlStatementsDirectory>
</schema>
```

Files are read in name order and `alter table` applies to whatever came before, so `V1__create.sql`
followed by `V2__add_column.sql` describes the schema those two migrations leave behind.

## What it checks

**A column no table declares.** The typo that returns an error from the database on the first
request:

```sql
select id, slgu from tenant
```

> Statement 'findTenant' in tenant/findTenant.sql does not match the schema:
>   no column 'slgu' in 'tenant'.

**A parameter that disagrees with its column.** `id` is a `uuid`, so declaring it a `Long` is wrong
now rather than at run time.

**A record component that cannot hold its column.** The one worth having: a **nullable column read
into a primitive** compiles and then throws on the first row that has a null in it.

```sql
-- resultRowType: com.example.domain.Tenant
select id, nickname from tenant
```

> component 'nickname' is int but column 'nickname' is nullable.

**`select *` is checked too.** With a catalog the star expands to the columns the table declares, in
declaration order, so a record built from one is checked like any other.

## What it says nothing about

This is the important half. Anything it cannot read with certainty is **unknown**, and an unknown
skips a check rather than failing it:

- SQL the parser does not cover — a stored function whose body is another language, a dialect
  extension
- a **subquery**, a **common table expression**, or a **union**, any of which can produce a column
  the catalog never saw
- a table the catalog does not have
- a SQL type nobody mapped, which stays unknown rather than becoming `Object`

That is deliberate, and it is what makes this safe to turn on in a project that was not written with
it in mind.

Where it is wrong about one statement, that statement opts out:

```sql
-- name: findTenant
-- validateSchema: false
select whatever from wherever
```

## Parameter types come free

The part that removes work. A parameter takes the type of the column it is named after, so a write
statement needs no `parameters` block at all:

```sql
-- name: insertTenant
-- returning: none
insert into tenant (id, slug, created_at)
values (:id, :slug, :createdAt)
```

`UUID`, `String` and `Instant` come from the DDL. A **nullable** column gives the boxed type, because
the parameter can be null; a `not null` column gives the primitive.

What you write always wins. Naming a type in the front matter is how you use a type of your own —
a `TenantId` wrapping a `UUID` — and how you settle anything this gets wrong.

## More than one database

`YoSQL` [picks a statement by vendor at run time](../sql-files/), and the schema follows the same
rule. Mark DDL with a `vendor` and it builds that database's schema; DDL with no vendor applies to
all of them, so only the tables that actually differ are written twice.

```sql
-- vendor: PostgreSQL
create table tenant (id bigserial primary key, slug varchar(64) not null)
;

-- vendor: MySQL
create table tenant (id bigint auto_increment primary key, slug varchar(64) not null)
```

Most dialect differences never surface, because validation compares **Java** types rather than SQL
ones: `bigserial`, `bigint auto_increment` and `int8` are all a `long`.

Where two databases genuinely disagree — a `uuid` on one and a `varchar(36)` on the other — a
statement naming no vendor cannot be generated, because it is the fallback for both and one method
cannot have two signatures. That is reported rather than resolved by picking one:

> Statement 'findTenant' runs against databases that disagree about what a column holds:
>   'id' reads as java.util.UUID and java.lang.String.

Name the type in the front matter to settle it, or write a statement per vendor.
