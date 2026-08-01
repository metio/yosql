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

When the query is not yours to change — a view you do not own, or SQL generated elsewhere — name
the column in the front matter instead. Keys are component paths from the root of the result row
type:

```sql
-- name: findLedgerEntries
-- returning: multiple
-- resultRowType: com.example.domain.LedgerEntry
-- resultRowColumns:
--   amount.minorUnits: amount_cents
--   at: created_at
```

A column override belongs to the type rather than to one query, so every statement naming the same
result row type shares one set: overrides declared on any of them apply to all of them, and two
statements mapping the same component to different columns fail the build.

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
- a component whose type nothing can read from a result set — the error names the `valueOf`
  factory that would fix it;
- a type declaring more than one `valueOf` the generator could use.

Where the select list cannot be enumerated — `select *`, or an expression without an alias — the
first two checks are skipped rather than guessed at. Aliasing your expressions gets them back.

### Types that build themselves

A type nothing above can read still works, if it says how. Give it a `static` factory called
`valueOf` taking one value the generator does know, and a component of that type is read from a
single column and handed to it:

```java
public record TenantId(UUID value) {
    public static TenantId valueOf(final UUID value) {
        return new TenantId(value);
    }
}
```

```java
final UUID tenantIdValue = resultSet.getObject("tenant_id", UUID.class);
final TenantId tenantId = tenantIdValue == null ? null : TenantId.valueOf(tenantIdValue);
```

This is the same convention enums already follow — `OrderState.valueOf(String)` — extended to your
own types, and it is the extension point for anything the built-in list cannot cover: an identifier
wrapped around a `UUID`, a normalising short name, a column holding JSON that your factory parses.
The mapping lives on the type it belongs to, written in Java, and the emitted call is still a
direct one, so nothing about it needs a native-image hint.

Every rule above still holds, because the column is read exactly as a bare value of the parameter's
type would be: a primitive parameter refuses NULL, an object parameter accepts it, and the factory
is only called for a value that is actually there.

The factory also settles what a one-component record means. With one, `TenantId` is a value wrapped
around a column and reads the column its *component in the outer record* names. Without one, it is
an ordinary nested record and its component reads a column called `value`. The type says which by
declaring the factory or not.

Two `valueOf` overloads that both take a readable type is a build error rather than a coin flip —
leave a single one taking the type the column holds.

### Types a component can have

`String`, `UUID`, `Instant`, `LocalDate`, `LocalDateTime`, `LocalTime`, `OffsetDateTime`,
`BigDecimal`, `Currency`, `byte[]`, the primitives and their wrappers, any enum, any record built
from those, and any type with a `valueOf` factory taking one of them.

### Writes that answer with a row

`insert … returning id` is a write that produces a result set, and Postgres will only hand it back
through `executeQuery`. Say so with `type`, because the method name decides otherwise — anything
starting with `insert`, `update`, `delete` and the rest of
[allowedWritePrefixes](../../configuration/repositories/allowedwriteprefixes/) is taken for a write,
and a write runs `executeUpdate` and throws the row away:

```sql
-- name: insertSignIn
-- type: reading
-- returning: single
-- resultRowType: com.example.domain.SignInId
-- parameters:
--   - name: accountId
--     type: java.util.UUID
insert into sign_in (account_id, created_at)
values (:accountId, now())
returning id
```

```java
public record SignInId(UUID id) {
}
```

The record's component is named after the column the statement returns, so a single returned value
needs a one-component record rather than a bare `UUID` — a result row type is always a row.

## Parameters, on the way in

The same types travel back. A parameter declared as a value type is unwrapped through the accessor
its `valueOf` takes, so a repository accepts what it hands back:

```sql
-- name: insertTenant
-- parameters:
--   - name: tenantId
--     type: com.example.domain.TenantId
--   - name: registeredAt
--     type: java.time.Instant
insert into tenant (id, registered_at)
values (:tenantId, :registeredAt)
```

```java
final UUID tenantIdParameter = tenantId == null ? null : tenantId.value();
final Timestamp registeredAtParameter = registeredAt == null ? null : Timestamp.from(registeredAt);
```

`Instant` is there for a reason of its own: PostgreSQL refuses `setObject` with one — *"Can't infer
the SQL type to use for an instance of java.time.Instant"* — so a statement naming the type its
domain actually uses would compile and fail at run time. `Instant`, `Currency` and enums are
converted; everything a driver already takes is passed through untouched, so a statement whose
parameters need nothing generates exactly what it always generated.

The conversion happens once, in front of the loop that binds the parameter, so a name used twice in
one statement is unwrapped once. Passing `null` writes SQL NULL rather than throwing.

Two things this refuses, both at build time. A record that is not one value — `Money(long
minorUnits, Currency currency)` — because a statement binds one value per parameter; declare a
parameter per component. And a value type that is not a record, because reading one needs only the
`valueOf` factory while writing one needs the accessor it came from, and a class does not say which
of its methods that is.

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
decides where they all go. What they are called comes from
[recordConverterPrefix](../../configuration/converter/recordconverterprefix/) and
[recordConverterSuffix](../../configuration/converter/recordconvertersuffix/) — `To` and `Converter`
by default, giving `ToTenantConverter` and a repository field named `tenantConverter`. The method
each one declares is [recordConverterMethod](../../configuration/converter/recordconvertermethod/),
`asUserType` by default. Set them to whatever the hand-written converters in your project already
use, so a repository reads the same whichever kind it calls.

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
