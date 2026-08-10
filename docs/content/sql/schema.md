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

## Turning it up

`WARN` is the default: disagreements are reported and nothing stops. **No build that passed before
fails because of this.**

Move it to `ERROR` once you have dealt with what it reports, so nothing new gets in:

```xml
<schema>
  <validation>ERROR</validation>
</schema>
```

`ERROR` from a standing start is the wrong order. The first run finds everything at once, and a
build that fails on all of it is a build nobody can bisect.

To hear nothing at all, `OFF`.

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

```text
Statement 'findTenant' in tenant/findTenant.sql does not match the schema:
  no column 'slgu' in 'tenant'.
```

**A parameter that disagrees with its column.** `id` is a `uuid`, so declaring it a `Long` is wrong
now rather than at run time.

**A record component that cannot hold its column.** The one worth having: a **nullable column read
into a primitive** compiles and then throws on the first row that has a null in it.

```sql
-- resultRowType: com.example.domain.Tenant
select id, nickname from tenant
```

```text
component 'nickname' is int but column 'nickname' is nullable.
```

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

### What "named after" means

The parameter's own name, matched against the columns of the tables the statement reads. Nothing
about the comparison it appears in is used, so this infers every parameter:

```sql
insert into tenant_invitation (id, tenant_id, email, invited_at)
values (:id, :tenantId, :email, :invitedAt)
```

and this infers none of its one:

```sql
select currency from tenant where id = :tenantId
```

The column is `id`, the parameter is `tenantId`, and the statement is otherwise as simple as it
gets. Rename the parameter after its column, or declare the type — a parameter named for what it
means rather than for its column is worth keeping and declaring. `staleBefore` in
`last_seen_at < :staleBefore` says more than `lastSeenAt` would, and it needs a type.

Two boundaries follow from "the tables the statement reads":

- A table reached only inside a subquery is not one of them, so `:tenantId` in
  `order_id in (select id from placed_order where tenant_id = :tenantId)` has nothing to match. The
  statement's own parameters are unaffected.
- A statement selecting from a derived table or a common table expression has no scope that can be
  listed at all, and none of its parameters are inferred.

The build says which of these it is when it cannot type a parameter, including the columns it
matched against.

A block only has to name what inference cannot reach. Declaring one parameter of six leaves the
other five inferred, and the method's parameters stay in the order the statement binds them either
way — so a statement with one awkward name keeps one line of front matter rather than six:

```sql
-- name: findOrderSuspensionsOfTenant
-- returning: multiple
-- parameters:
--   tenantId: uuid
select * from order_suspension
where suspended_at > :suspendedAt
  and order_id in (select id from placed_order where tenant_id = :tenantId)
```

## Records come free too

A result row type is usually a record whose components repeat, one by one, what the `select` list
already says. Set `generateResultRowType` and `YoSQL` writes it:

```sql
-- name: findTenantSummary
-- returning: multiple
-- resultRowType: com.example.domain.TenantSummary
-- generateResultRowType: true
select id, slug, created_at from tenant where account_id = :accountId
```

which produces

```java
public record TenantSummary(UUID id, String slug, Instant createdAt) {
}
```

next to its converter, in the package the type name gives. Aliases decide the component names, so
`select amount_cents as minor_units` gives a `minorUnits`; a nullable column gives the boxed type.

It is per statement, and off unless you ask. A `resultRowType` naming a record that is not there is
far more often a typo than a request, and quietly writing a new record for a misspelled name would
replace a build error with a mystery.

The record has to be describable in full. A computed expression, a subquery or a column no `create
table` mentions leaves `YoSQL` with nothing to write, and it says so rather than guessing:

```text
Statement 'findTenantSummary' asks YoSQL to write 'com.example.domain.TenantSummary', but the
schema does not say what every column it selects holds: 'tenant.settings' is declared 'jsonb',
which is not a type YoSQL maps without a vendor. Set 'schema.vendor', or a 'vendor' on the
statement, so that the types only one database has are looked up.
```

The message names the column and the one thing that answers it, which is worth reading rather than
skimming — the four situations that end here are fixed in four different files. A missing table asks
for more DDL, a computed expression asks for a record written by hand, and the case above is neither.

## Types only one database has

Standard spellings resolve without being told which database wrote the DDL. A vendor's own —
PostgreSQL's `bytea`, `timestamptz`, `bigserial`, `json`, `jsonb` and `citext`, MySQL's `datetime`
and `longtext` — resolve only once something declares the vendor. Until then those columns are
described but untyped: parameters naming them need their type written out, and
`generateResultRowType` will not write the record.

A run says which columns that leaves untyped, so the question does not have to be guessed at:

```text
The schema holds 1 column(s) whose type YoSQL does not map: document.payload (jsonb). No vendor
is declared, and the types only one database has are looked up only for a declared one — so
'schema.vendor' may be all that is missing.
```

The table count above it is the same either way — it is the columns that change — so it is the
wrong figure to read a vendor's effect off. An untyped column that no statement selects and no
parameter is named after costs nothing, which is why this is said rather than warned about.

Mark the DDL itself, once per file, and every statement reading it follows:

```sql
-- vendor: PostgreSQL

create table document (
    id      uuid  not null primary key,
    payload jsonb not null
)
```

Where the schema is a Flyway or Liquibase directory the files are checksummed, and adding a comment
to a migration already applied makes the tool refuse to run. Say it in the build instead:

```xml
<schema>
  <sqlStatementsDirectory>src/main/resources/db/migration</sqlStatementsDirectory>
  <vendor>PostgreSQL</vendor>
</schema>
```

A file naming its own vendor still keeps it.

### JSON columns

`json` and `jsonb` are a `String` holding the JSON as text. Postgres has no JDBC type of its own for
them — the driver refuses `getObject(column, String.class)` and answers `getString` — so that is the
accessor generated code uses, and a `String` is both the record component and the parameter type.

Reading is otherwise unremarkable. Writing needs a cast in the query, because a `String` parameter
binds as `varchar` and Postgres will not assign or compare that to a JSON column:

```sql
-- name: insertDocument
-- returning: none
insert into document (id, payload)
values (:id, cast(:payload as jsonb))
```

`:payload::jsonb` does the same. Without one the build passes and the statement fails at run time
with `column "payload" is of type jsonb but expression is of type character varying`. Nothing
generated can add the cast, because the type a parameter has to arrive as is a property of the query.

Two things differ from what the `String` suggests. A `jsonb` column is stored parsed, so it reads
back with its keys ordered and its whitespace normalised rather than as the text that was written;
`json` keeps the bytes verbatim. And through the [map converter](../converters/#map-converter) a JSON
column is a `PGobject` rather than a `String`.

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

```text
Statement 'findTenant' runs against databases that disagree about what a column holds:
  'id' reads as java.util.UUID and java.lang.String.
```

Name the type in the front matter to settle it, or write a statement per vendor.
