---
name: yosql
description: Write and configure YoSQL statements — .sql files with YAML front matter that generate Java repositories at build time. Use when a project has .sql files under a yosql source directory, a yosql-tooling-maven/gradle/ant plugin, a yosql.args file, or when adding a query, a result record, a converter, or changing generated repository configuration.
---

# YoSQL

`YoSQL` turns `.sql` files into Java repositories during the build. You write the query and the
record it produces; the generator writes the repository, the parameter binding and the row mapping.
Nothing is resolved at runtime and nothing is added to the runtime classpath.

Full reference: <https://yosql.projects.metio.wtf/>

## Before writing anything

Find out how the project is set up, because it decides where files go:

1. **Where statements live** — `files.inputBaseDirectory`, default `src/main/yosql`. Look for a
   `<inputBaseDirectory>` in `pom.xml`, a `yosql { files { ... } }` block in `build.gradle(.kts)`, or
   `--files-input-base-directory` in a `yosql.args` file.
2. **Where records live** — `files.sourceDirectory`, default `src/main/java`. Result row types are
   read **from source**, not from the classpath, so a record must exist as a file under this
   directory before a statement can name it.
3. **The package repositories are generated into** — `repositories.basePackageName`.

Statement files are grouped into repositories by their directory: every `.sql` file under
`src/main/yosql/tenant/` generates into `TenantRepository`.

## A statement

One `.sql` file holds one or more statements, separated by `;`. Each has YAML front matter written
inside SQL comments:

```sql
-- name: findTenant
-- returning: single
-- resultRowType: com.example.domain.Tenant
select id, account_id, slug, created_at
from tenant
where id = :id
;
```

That generates `Optional<Tenant> findTenant(UUID id)` on `TenantRepository`, plus a
`ToTenantConverter` reading each column by name.

**Front matter is YAML, one key to a `--` line.** A value continued on a line of its own is read as
the next key, and the parse fails — most often on a `description` long enough to want wrapping. Keep
each value on its line, however long:

```sql
-- name: findTenant
-- description: Reads the tenant an account owns, including the ones marked inactive.
```

### The keys that matter

| Key | What it does |
| --- | --- |
| `name` | The method name. Defaults to the file name; required when a file holds several statements. |
| `returning` | `none`, `single` (`Optional<T>`), `multiple` (`List<T>`) or `cursor` (lazy `Stream<T>`). |
| `resultRowType` | A record to build each row into. The converter is generated from it. |
| `generateResultRowType` | Write that record too, from the schema, instead of reading one you wrote. |
| `resultRowConverter` | A converter class you wrote yourself. Use instead of `resultRowType`. |
| `parameters` | Parameter types, when they cannot be inferred. |
| `type` | `reading`, `writing` or `calling`. Usually inferred from the name prefix. |
| `resultRowColumns` | Which column a component reads, when aliasing in SQL is not possible. |
| `vendor` | Marks a statement as being for one database only. |
| `description` | Carried into the generated method's javadoc. One line. |
| `injectConverter` | Take this statement's converter as a constructor parameter instead of building it. |

### Statement type comes from the name

A statement whose name starts with a read prefix (`find`, `get`, `select`, `read`, `query`, …) is a
read; a write prefix (`insert`, `update`, `delete`, `write`, `create`, `drop`, …) is a write; a call
prefix (`call`, `execute`) calls a procedure. **A name matching none of them generates nothing at
all** — name it accordingly, or set `type` explicitly.

## Parameters

Named parameters are written `:name` and become method parameters in the order they first appear.

The generator needs a Java type for each one. It takes it from the front matter, from the component
of the same name on the `resultRowType` record, or from the column the parameter is named after:

```sql
-- name: findTenant
-- returning: single
-- resultRowType: com.example.domain.Tenant
select id, slug from tenant where id = :id
```

`Tenant` declares `UUID id`, so `:id` is a `UUID` and nothing needs saying.

Where the project keeps its `create table` statements somewhere `YoSQL` reads them, the schema
answers the same question, so a write statement usually needs no `parameters` block either:

```sql
-- name: insertTenant
-- returning: none
insert into tenant (id, slug, created_at) values (:id, :slug, :createdAt)
```

A parameter names its column the way a record component does: `:accountId` reads `account_id`, and
`:account_id` reads it too. `uuid`, `varchar` and `timestamp` columns give `UUID`, `String` and
`Instant`; a nullable column gives the boxed type.

Four sources, in this order, and the first that answers wins:

1. the `parameters` block — always authoritative, whatever the schema says;
2. a component of the same name on the `resultRowType` record;
3. a column spelled exactly as the parameter is;
4. a column whose `snake_case` name the parameter's `camelCase` converts to.

A parameter naming nothing at all still fails the build. Inference fills in what nobody declared; it
never overrides and never falls back to `Object`. Check whether a schema is being read before writing types out by hand — look
for `create table` statements among the `.sql` files, or a `schema.sqlStatementsDirectory`. That
directory can be a Flyway migrations directory as it stands: files are read in version order, so the
schema is the one the migrations leave behind rather than the one the first file created. Undo
migrations (`U48__…`) are skipped, since a migration never runs them.

Standard SQL spellings are the ones it knows without being told which database it is looking at:
`char`, `varchar`, `text`, `clob`, `smallint`, `int`, `bigint`, `boolean`, `real`, `double
precision`, `numeric`, `decimal`, `date`, `time`, `timestamp`, `timestamp with time zone`, `uuid`,
`blob`, `binary`, `varbinary`.

A vendor's own spellings — PostgreSQL's `bytea`, `timestamptz`, `bigserial`, `serial`, `int8`,
`jsonb` and `citext`, MySQL's `datetime` and `longtext` — need somebody to say which database the
DDL was written for. **Mark the DDL itself**, once per schema file, and every statement reading it
gets those spellings without declaring anything:

```sql
-- vendor: postgresql

create table attachment (
    id      bigserial primary key,
    payload bytea not null,
    at      timestamptz not null
);
```

A statement's own `vendor` key does the same, but it says something else as well — that the
statement is *for* that database and is not the fallback for any other — so it is the wrong tool for
a project with one database. It also disables collection expansion. Mark the schema, not the
queries.

Where the schema is a Flyway or Liquibase directory the files are checksummed, and adding a comment
to a migration already applied makes the tool refuse to run. Say it in the build instead, with
`schema.vendor`:

```xml
<schema>
  <sqlStatementsDirectory>src/main/resources/db/migration</sqlStatementsDirectory>
  <vendor>PostgreSQL</vendor>
</schema>
```

A file naming its own vendor still keeps it.

Where neither says, a column spelled that way is unknown: the parameter needs its type written out,
and `generateResultRowType` will not write the record.

The type to write for `bytea` is `byte[]`:

```sql
-- name: insertAttachment
-- returning: none
-- parameters:
--   id: uuid
--   payload: byte[]
insert into attachment (id, payload) values (:id, :payload)
```

Note `byte[]`, not the `blob` short name — `blob` in front matter means `java.sql.Blob`, which is a
different thing from the bytes a `bytea` column holds. Getting this wrong produces a compile error at
the *call site* rather than a generation error, so it reads as unrelated to the statement.

Where neither answers — no schema, no matching component — name the types:

```sql
-- name: insertTenant
-- returning: none
-- parameters:
--   id: uuid
--   slug: string
--   createdAt: instant
insert into tenant (id, slug, created_at) values (:id, :slug, :createdAt)
```

A type is a fully-qualified class name (`com.example.domain.TenantId`), a primitive (`int`), or one
of the short names: `string`, `uuid`, `instant`, `localdate`, `localdatetime`, `localtime`,
`offsetdatetime`, `zoneddatetime`, `duration`, `period`, `bigdecimal`, `biginteger`, `date`, `time`,
`timestamp`, `blob`, `clob`, `array`, `url`, `uri`, `currency`, `locale`, `zoneid`, `object`.

**A parameter no type can be found for fails the build.** That is deliberate — the alternative is
binding it as `Object`, which compiles and accepts anything.

The longer list form is still there when a parameter needs more than a type:

```sql
-- parameters:
--   - name: payload
--     type: java.lang.String
--     sqlType: 2005
```

### Names a parameter cannot have

A generated method declares variables of its own, and a parameter cannot be called after one of
them: `LOG`, `query`, `rawQuery`, `executedQuery`, `databaseProductName`, `action`, `exception`,
`throwable`, `suppressed`, `dataSource`, `connection`, `statement`, `resultSetMetaData`,
`databaseMetaData`, `resultSet`, `columnCount`, `columnLabel`, `batch`, `list`, `jdbcIndex`,
`index`, `row`. The build says so naming the file and the parameter. Rename it in the SQL — the
name reaches no further than the method signature.

Check these before writing the SQL rather than after: `action`, `index`, `row`, `statement` and
`list` are ordinary column names, and `insert into audit (action) values (:action)` is the shape
that hits it. Two parameters also cannot be named `x` and `xElement`, or `x` and `xParameter`,
because the second is what the generator would have called a local of its own derived from the
first.

### A list of values

A prepared statement has one placeholder per value, so `in (:ids)` only works if the query is built
around the list. Declare the parameter as a collection and that happens:

```sql
-- name: findTenantsByIds
-- returning: multiple
-- resultRowType: com.example.domain.Tenant
-- parameters:
--   - name: ids
--     type: java.util.List<java.util.UUID>
select id, slug from tenant where id in (:ids)
```

`List`, `Set`, `Collection` and `Iterable` all work; an array does not, because an array parameter
is how a batch statement passes one value per execution. For the same reason a write holding a
collection gets no batch method — `executeBatch` defaults to off for it rather than failing the
build, so `update … where state in (:states)` needs nothing said about batching. Writing
`executeBatch: true` on such a statement is still an error: it asks for something impossible. An empty collection matches no row, which
is what `in` on an empty set means — but an empty collection in a `not in` throws, because that
should match every row and no list of placeholders can say so. A statement written once per vendor
cannot expand a collection at all.

## Result records

Name a record and the converter is written for you. The record must exist under
`files.sourceDirectory` before the build runs.

```java
package com.example.domain;

public record Tenant(UUID id, UUID accountId, String slug, Instant createdAt) {
}
```

- A component reads the column its own name implies, `camelCase` read as `snake_case`:
  `accountId` reads `account_id`.
- When the column is named something else, **alias it in the query** — `select amount_cents as
  minor_units` — rather than configuring an override.
- A component whose type is itself a record is built from the same flat row. Nesting groups values
  in Java; the query knows nothing about it, so a nested component claims the column matching its
  own name, not a prefixed one.
- A type the generator cannot otherwise read works if it declares a `static valueOf` taking one
  value the generator does know. That is how enums work, and it is what makes a one-component record
  a value wrapped around a column rather than a nesting.
- **A column no component claims, or a component no column supplies, fails the build.** Keep the
  select list and the record in step.

### Letting the schema write the record

A record whose components only repeat what the `select` list already says does not have to be
written at all. Add `generateResultRowType` and the schema writes it:

```sql
-- name: findTenantSummary
-- returning: multiple
-- resultRowType: com.example.domain.TenantSummary
-- generateResultRowType: true
select id, slug, created_at from tenant where account_id = :accountId
```

That produces `record TenantSummary(UUID id, String slug, Instant createdAt)` next to its converter.
Aliases decide the component names, and a nullable column gives the boxed type.

It is opt-in per statement on purpose: a `resultRowType` naming a record that is not there is far
more often a typo than a request. Reach for it when the record has no behaviour of its own and
exists only to carry a row; write the record by hand when it has a value object, an enum or a nested
record in it, because the schema can only describe columns.

The build stops if the schema cannot describe every column selected — a computed expression, a
subquery, a table no `create table` mentions. That is a different message from the missing-source
one, and it means *write this one yourself*.

### Where the generated types go

Repositories go in `repositories.basePackageName`. Converters do not: a converter generated from a
record is named `<recordConverterPrefix><Record><recordConverterSuffix>` and written to the package
of **`converter.mapConverterClass`**, which defaults to `com.example.persistence.converter`. Left
alone it stays there however the repositories are packaged, so set it alongside `basePackageName`:

```xml
<repositories>
  <basePackageName>com.example.store</basePackageName>
</repositories>
<converter>
  <mapConverterClass>com.example.store.converter.ToMapConverter</mapConverterClass>
</converter>
```

A record written by `generateResultRowType` goes to the package its `resultRowType` names, which is
under your control already.

## Aggregates and computed columns

An aggregate is not a column, so the schema cannot say what it holds: `generateResultRowType`
refuses a select list containing one, and no parameter type is inferred from it. Alias it and give
the type yourself.

Two things about aggregates cost real debugging time, and neither shows up at build time.

**The type widens.** In PostgreSQL `sum()` over `bigint` is `numeric`, `sum()` over `int` is
`bigint`, `count()` is `bigint`, and `avg()` is `numeric`. A component declared `Long` is read with
`getObject(column, Long.class)`, and the PostgreSQL driver will not convert a `numeric` to a `Long`
— `conversion to class java.lang.Long from numeric not supported`, on the first row. Cast in the
query so the column is what the component says it is:

```sql
-- name: findTotalByCurrency
-- returning: multiple
-- resultRowType: com.example.domain.CurrencyTotal
select currency, sum(amount_cents)::bigint as minor_units from ledger group by currency
```

**An aggregate over no rows still returns a row.** `select max(created_at) from tenant where ...`
matching nothing returns one row holding null, not zero rows — so `returning: single` gives a
*present* `Optional` wrapped around a record whose component is null. If that component is a
primitive, the generated converter throws `SQLException` naming the column, because a primitive
cannot hold the null. Where absence has to be distinguishable from a null result, ask for the row
instead of the aggregate:

```sql
select created_at from tenant where account_id = :accountId order by created_at desc limit 1
```

That returns no row when nothing matches, so the `Optional` is empty. `group by` has the same
effect: a grouped aggregate over no rows produces no groups.

## Writing statements

```sql
-- name: insertTenant
-- returning: none
-- parameters:
--   id: uuid
--   slug: string
insert into tenant (id, slug) values (:id, :slug)
```

- `returning: none` with `writesReturnUpdateCount` (the default) gives `int`.
- A write that returns rows — `insert ... returning *` on Postgres — uses `returning: single` or
  `multiple` with a `resultRowType`, like a read.
- Batch methods are generated alongside when `executeBatch` is on: `insertTenantBatch(UUID[] id,
  String[] slug)`.

## Transactions

By default a method opens its own connection from the `DataSource` and closes it. To run several
statements in one transaction, use the generated overload taking a `Connection`:

```java
try (final var connection = dataSource.getConnection()) {
    connection.setAutoCommit(false);
    try {
        tenants.insertTenant(connection, id, slug);
        ledger.insertLedgerEntry(connection, entryId, id, amount);
        connection.commit();
    } catch (final RuntimeException exception) {
        connection.rollback();
        throw exception;
    }
}
```

Under Spring, wrap the `DataSource` in a `TransactionAwareDataSourceProxy` and the `DataSource`-based
methods join the ambient `@Transactional` transaction with no `Connection` threading at all.

## What the generator will not do for you

Worth deciding early, because both change the shape of the code around the repositories.

**A mapper that needs state cannot be a generated converter.** Converters are constructed with no
arguments, so a mapper closing over a resolver, a tenant key or a clock has nowhere to take them
from. Two ways out:

- `injectConverter: true` in a statement's front matter makes *that* converter a constructor
  parameter of its repository. Every other repository is generated exactly as before, so one
  statement needing a stateful mapper costs one constructor. Statements sharing a converter share the
  field, so one of them asking is enough; the converter needs an alias, since that names the
  parameter.
- `repositories.injectConverters` does the same for every repository in the project at once. Reach
  for it when most converters need injecting, not when one does.
- Return a flat record straight from the schema and do the mapping in the caller. Nothing about the
  repositories changes, and the state stays in the service that owns it. This is the one that scales
  when only a few statements need it.

**Spring's exception translation goes with Spring's execution engine.** A generated method throws
`RuntimeException` wrapping the `SQLException` (`repositories.catchAndRethrow`, on by default), so
code catching `DuplicateKeyException`, `EmptyResultDataAccessException` or anything else from
`org.springframework.dao` stops firing — silently, because a catch block that never matches is not
an error. Match on the SQLState instead:

```java
} catch (final RuntimeException exception) {
    if (exception.getCause() instanceof SQLException sql && "23505".equals(sql.getSQLState())) {
        // unique violation
    }
    throw exception;
}
```

This is a behaviour change a migration will not notice unless a test already covered that branch.
Go looking for `org.springframework.dao` imports before converting the statements, not after.

## Checks worth running before you finish

- Every statement's name starts with a configured prefix, or sets `type` explicitly.
- Every `:parameter` has a type, from the front matter or a matching record component.
- Every column the select lists has a component, and every component has a column.
- Every `resultRowType` names a record that exists under `files.sourceDirectory`, unless the
  statement sets `generateResultRowType`.
- No parameter is named after one of the variables a generated method already declares.
- Every front matter value is on one line.
- Every aggregate is aliased, cast to the type its component declares, and — where an empty result
  has to be distinguishable — written as a `limit 1` row rather than a bare `max()`.
- Run the build. `YoSQL` reports these as build failures naming the file and the statement, so a
  green build is the check.

## Licence headers

A `.sql` file may open with a block comment and `YoSQL` drops it, however long it is:

```sql
/*
 * SPDX-FileCopyrightText: ...
 */

-- name: findTenant
select id from tenant where id = :id
```

Write it as `/* ... */`, never as `--` lines. A `--` line at the top of a file **is** front matter,
so a licence written that way is read as configuration. A block comment further down is left alone,
because that is where a vendor puts an optimizer hint.

## Anti-patterns

- **Do not edit generated code.** It lives under `target/generated-sources/yosql` (or the configured
  output directory) and is rewritten every build. Change the statement or the record instead.
- **Do not add `parameters` entries that repeat a record component's type, or a column's.** They are
  inferred from both.
- **Do not reach for `resultRowColumns` first.** Alias the column in the query — the mapping belongs
  next to the column being renamed.
- **Do not build SQL by string concatenation around a statement.** A statement is fixed at build
  time; write the variants out, or use a `where (:filter is null or column = :filter)` construction.
