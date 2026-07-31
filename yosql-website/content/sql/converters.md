---
title: Converters
date: 2019-07-07T14:27:54+02:00
menu:
  main:
    parent: SQL
categories:
  - SQL
tags:
  - converters
---

The JDBC API has no object mapping. Something has to turn a `java.sql.ResultSet` row into a value
of your domain, and in `YoSQL` that something is a **converter**: a plain Java class with a method
taking a `ResultSet` and returning your type.

Name a record and the converter is written for you. Write one by hand when the mapping is doing
something a record cannot express.

## From a record

Point a statement at a record and `YoSQL` reads that record's canonical constructor and emits the
converter:

```sql
-- name: findTenant
-- returning: single
-- resultRowType: com.example.domain.Tenant
select id,
       account_id,
       slug,
       currency,
       created_at
from tenant
where id = :id
```

```java
package com.example.domain;

public record Tenant(UUID id, UUID accountId, String slug, Currency currency, Instant createdAt) {
}
```

What comes out is one `resultSet.getX(...)` call per component and then the constructor — the same
code you would have written:

```java
public final class ToTenantConverter {
  public Tenant asUserType(final ResultSet resultSet) throws SQLException {
    final UUID id = resultSet.getObject("id", UUID.class);
    final UUID accountId = resultSet.getObject("account_id", UUID.class);
    final String slug = resultSet.getString("slug");
    final String currencyCode = resultSet.getString("currency");
    final Currency currency = currencyCode == null ? null : Currency.getInstance(currencyCode);
    final Timestamp createdAtTimestamp = resultSet.getTimestamp("created_at");
    final Instant createdAt = createdAtTimestamp == null ? null : createdAtTimestamp.toInstant();
    return new Tenant(id, accountId, slug, currency, createdAt);
  }
}
```

Nothing is looked up by name at runtime, so the result survives a GraalVM native image without a
reflection hint — which is the reason to reach for generated JDBC over an ORM in the first place.

The record is read from its **source**, under [sourceDirectory](../../configuration/files/sourcedirectory/),
because code generation runs before compilation and the record usually lives in the module being
generated for. Nothing is loaded or executed while reading it.

### Which column a component reads

A component's own name, read as `snake_case`: `tenantId` reads `tenant_id`, `createdAt` reads
`created_at`. Where the column is named something else, alias it in the query:

```sql
select amount_cents as minor_units,
       currency
from ledger_entry
```

That keeps the one place the two naming schemes meet next to the column being renamed, rather than
in configuration you would have to go and find.

### Value objects spanning several columns

A component whose type is itself a record is built from the same flat row. Nesting groups values on
the Java side and the query knows nothing about it, so a nested component claims the column matching
its own name — no prefix:

```java
public record Money(long minorUnits, Currency currency) {
}

public record LedgerEntry(long id, Money amount, Reason reason, String reference, Instant at) {
}
```

```sql
-- name: findLedgerEntries
-- returning: multiple
-- resultRowType: com.example.domain.LedgerEntry
select id,
       amount_cents as minor_units,
       currency,
       reason,
       reference,
       created_at as at
from ledger_entry
where tenant_id = :tenantId
```

`LedgerEntry` arrives with its `Money` assembled from `minor_units` and `currency`.

### Null, and what it must not become

`getLong` answers `0` for SQL NULL and `getInt` answers `0`, which is how a nullable column quietly
becomes a wrong number. So:

- a **primitive** component reads the value and then checks `wasNull()`, failing with the column's
  name — a `long` cannot represent NULL and will not pretend to;
- a **boxed or object** component arrives as `null`. A `cancelled_at` that was never set is `null`,
  not the epoch.

Make a component nullable by giving it a reference type: `Instant activatedAt` rather than a
primitive.

### Enums

An enum component is read as text and passed to `valueOf`. A value the enum does not know raises
`IllegalArgumentException` naming the type and the value. Failing is the point: a persistence layer
handed a state it cannot represent should stop rather than invent a default.

### What fails the build

A mismatch between the query and the record is visible before anything is compiled, so it stops the
build and names the file, the statement and the component:

- a component no selected column supplies;
- a selected column no component claims;
- two records that would need converters of the same name;
- a record that contains itself;
- a component whose type nothing can read from a result set.

Where the select list cannot be enumerated — `select *`, or an expression without an alias — the
first two checks are skipped rather than guessed at. Aliasing your expressions gets them back.

### Types a component can have

`String`, `UUID`, `Instant`, `LocalDate`, `LocalDateTime`, `LocalTime`, `OffsetDateTime`,
`BigDecimal`, `Currency`, `byte[]`, the primitives and their wrappers, any enum, and any record
built from those.

## Map converter

Without a `resultRowType` or a converter of your own, generated code returns
`Map<String, Object>`. That converter is the [defaultConverter](../../configuration/converter/defaultconverter/)
unless you say otherwise, so freshly generated code returns maps to begin with. Turn it off with
[generateMapConverter](../../configuration/converter/generatemapconverter/), move it with
[mapConverterClass](../../configuration/converter/mapconverterclass/), rename its method with
[mapConverterMethod](../../configuration/converter/mapconvertermethod/) and its alias with
[mapConverterAlias](../../configuration/converter/mapconverteralias/). Methods using it look like:

```java
Optional<Map<String, Object>> someMethod()
List<Map<String, Object>> someMethod()
Stream<Map<String, Object>> someMethod()
```

Generated record converters live in the same package as the map converter, so `mapConverterClass`
decides where they all go.

## Default converter

Every statement that does not say otherwise uses the default converter. Set
[defaultConverter](../../configuration/converter/defaultconverter/) to change what that is for all
of them at once. It points at the map converter above until you change it.

## Writing one by hand

Some mappings a record cannot express: a discriminator column choosing between subtypes, a column
holding JSON you want parsed, a legacy shape you do not want in your domain. Write the converter
yourself and register it with [rowConverters](../../configuration/converter/rowconverters/):

```java
package my.own;

import java.sql.ResultSet;
import java.sql.SQLException;

import my.own.User;

public class UserConverter {

    public User apply(ResultSet resultSet) throws SQLException {
        User pojo = new User();
        pojo.setId(resultSet.getInt("id"));
        pojo.setName(resultSet.getString("name"));
        return pojo;
    }

}
```

Package, class name and method name are yours to choose. Use it as the default converter, or name
it on a single statement as a [resultRowConverter](../../configuration/sql/resultrowconverter/) by
alias or fully qualified name:

```sql
-- resultRowConverter: my.own.UserConverter
select id, name
from users
```

A statement naming both a `resultRowConverter` and a `resultRowType` keeps the converter: naming a
converter names the exact code to call, and there is nothing left to infer.

Either way, generated methods return the converter's result type:

```java
Optional<User> someMethod()
List<User> someMethod()
Stream<User> someMethod()
```
