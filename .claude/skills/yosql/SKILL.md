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

### The keys that matter

| Key | What it does |
| --- | --- |
| `name` | The method name. Defaults to the file name; required when a file holds several statements. |
| `returning` | `none`, `single` (`Optional<T>`), `multiple` (`List<T>`) or `cursor` (lazy `Stream<T>`). |
| `resultRowType` | A record to build each row into. The converter is generated from it. |
| `resultRowConverter` | A converter class you wrote yourself. Use instead of `resultRowType`. |
| `parameters` | Parameter types, when they cannot be inferred. |
| `type` | `reading`, `writing` or `calling`. Usually inferred from the name prefix. |
| `resultRowColumns` | Which column a component reads, when aliasing in SQL is not possible. |
| `vendor` | Marks a statement as being for one database only. |

### Statement type comes from the name

A statement whose name starts with a read prefix (`find`, `get`, `select`, `read`, `query`, …) is a
read; a write prefix (`insert`, `update`, `delete`, `write`, `create`, `drop`, …) is a write; a call
prefix (`call`, `execute`) calls a procedure. **A name matching none of them generates nothing at
all** — name it accordingly, or set `type` explicitly.

## Parameters

Named parameters are written `:name` and become method parameters in the order they first appear.

The generator needs a Java type for each one. It takes it from the front matter, or from the
component of the same name on the `resultRowType` record:

```sql
-- name: findTenant
-- returning: single
-- resultRowType: com.example.domain.Tenant
select id, slug from tenant where id = :id
```

`Tenant` declares `UUID id`, so `:id` is a `UUID` and nothing needs saying. Where there is no such
component — every write statement, for a start — name the types:

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

## Checks worth running before you finish

- Every statement's name starts with a configured prefix, or sets `type` explicitly.
- Every `:parameter` has a type, from the front matter or a matching record component.
- Every column the select lists has a component, and every component has a column.
- Every `resultRowType` names a record that exists under `files.sourceDirectory`.
- Run the build. `YoSQL` reports these as build failures naming the file and the statement, so a
  green build is the check.

## Anti-patterns

- **Do not edit generated code.** It lives under `target/generated-sources/yosql` (or the configured
  output directory) and is rewritten every build. Change the statement or the record instead.
- **Do not add `parameters` entries that repeat a record component's type.** They are inferred.
- **Do not reach for `resultRowColumns` first.** Alias the column in the query — the mapping belongs
  next to the column being renamed.
- **Do not build SQL by string concatenation around a statement.** A statement is fixed at build
  time; write the variants out, or use a `where (:filter is null or column = :filter)` construction.
