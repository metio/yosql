---
title: Cookbook
date: 2026-08-02
menu:
  main:
    parent: SQL
categories:
  - SQL
tags:
  - cookbook
  - recipes
---

Queries that need more than a name and a return mode. Each one is a whole statement you can paste
and adapt.

## A list of values in an IN clause

Declare the parameter as a collection and the query is built around it — one placeholder per value
the caller passed:

```sql
-- name: findTenantsByIds
-- returning: multiple
-- resultRowType: com.example.domain.Tenant
-- parameters:
--   - name: ids
--     type: java.util.List<java.util.UUID>
select id, slug, created_at
from tenant
where id in (:ids)
```

```java
final var found = tenants.findTenantsByIds(List.of(first, second, third));
```

`List`, `Set`, `Collection` and `Iterable` all work. An empty one matches no row, which is what `in`
on an empty set means — but an empty collection in a `not in` throws, because that should match
every row and no list of placeholders can say so. See
[lists of values](../sql-files/#lists-of-values) for the rest of the rules.

On a database with array types there is a second option, and on a busy statement it is the better
one: comparing against an array keeps the SQL the same length whatever the caller passes, so the
database can reuse one plan rather than preparing a new statement per size.

```sql
-- name: findTenantsByIdArray
-- returning: multiple
-- resultRowType: com.example.domain.Tenant
-- parameters:
--   ids: array
select id, slug, created_at
from tenant
where id = any(:ids)
```

`java.sql.Array` values are made by the connection, which is exactly what the
[connection overload](../transactions/) hands you:

```java
try (final var connection = dataSource.getConnection()) {
    final var ids = connection.createArrayOf("uuid", tenantIds.toArray());
    return tenants.findTenantsByIdArray(connection, ids);
}
```

## Optional filters

A statement is fixed at build time, so a filter that is sometimes applied has to be expressed in
SQL rather than by building a different query:

```sql
-- name: findTenants
-- returning: multiple
-- resultRowType: com.example.domain.Tenant
-- parameters:
--   accountId: uuid
--   slug: string
select id, slug, created_at
from tenant
where account_id = :accountId
  and (:slug is null or slug = :slug)
order by slug
```

Passing `null` for `slug` drops the condition. Watch the query plan: some optimisers handle this
well and some do not, and where it matters, two statements beat one clever one.

## Pagination

Keyset pagination is one statement, and it is the one to prefer — `offset` makes the database count
rows it then discards:

```sql
-- name: findTenantsAfter
-- returning: multiple
-- resultRowType: com.example.domain.Tenant
-- parameters:
--   accountId: uuid
--   afterSlug: string
--   pageSize: int
select id, slug, created_at
from tenant
where account_id = :accountId
  and slug > :afterSlug
order by slug
fetch first :pageSize rows only
```

For the first page, pass an empty string — or write a second statement without the `slug >`
condition, which keeps both plans simple.

Not every database takes a placeholder in the row-count clause. Where yours does not, write the
limit into the statement and have a statement per page size, or use its own syntax — one statement
per `vendor`, as below.

## Streaming a large result

`returning: cursor` gives a lazy `Stream` rather than a `List`, so rows are read as you consume
them and the whole result never has to fit in memory:

```sql
-- name: findAllLedgerEntries
-- returning: cursor
-- resultRowType: com.example.domain.LedgerEntry
select id, amount_cents as minor_units, currency, created_at as at
from ledger_entry
order by id
```

```java
try (final var entries = ledger.findAllLedgerEntries()) {
    entries.filter(entry -> entry.amount().minorUnits() > 0)
           .forEach(this::report);
}
```

**Close the stream.** It holds the `ResultSet`, the statement and — for the `DataSource` form — the
connection, and none of them are returned until it is closed. Most drivers also need a
non-default fetch size and auto-commit off before they stream rather than buffer; that is a
connection setting, so use the [connection overload](../transactions/) and set it yourself.

## Rows from a write

A write that returns rows is a read as far as the front matter is concerned — give it a
`returning` mode and a result row type:

```sql
-- name: insertTenant
-- returning: single
-- resultRowType: com.example.domain.Tenant
-- parameters:
--   accountId: uuid
--   slug: string
insert into tenant (id, account_id, slug, created_at)
values (gen_random_uuid(), :accountId, :slug, now())
returning id, account_id, slug, created_at
```

That is how to get a generated key back: let the database return the row and map it like any other.

## Nested records

A component whose type is itself a record is built from the same flat row — nesting groups values in
Java and the query knows nothing about it:

```sql
-- name: findLedgerEntries
-- returning: multiple
-- resultRowType: com.example.domain.LedgerEntry
-- parameters:
--   tenantId: uuid
select id,
       amount_cents as minor_units,
       currency,
       created_at as at
from ledger_entry
where tenant_id = :tenantId
order by id
```

```java
public record Money(long minorUnits, Currency currency) {
}

public record LedgerEntry(long id, Money amount, Instant at) {
}
```

`Money` claims `minor_units` and `currency` from the row it shares with `LedgerEntry`. A nested
component claims the column matching **its own** name, not a prefixed one, which is why the query
aliases `amount_cents` to `minor_units` rather than to `amount_minor_units`.

This maps one row to one object. It does not turn several rows into one object with a collection in
it — a query joining a tenant to its orders returns one row per order, and folding those into one
`Tenant` with a `List<Order>` is work for the caller.

## Enums

An enum is read from a column by the `valueOf` its own declaration gives it, so nothing is needed
beyond naming it:

```java
public enum OrderState {
    PLACED, ACTIVE, CANCELLED
}

public record PlacedOrder(UUID id, OrderState state, BigDecimal monthlyPrice) {
}
```

```sql
-- name: findOrder
-- returning: single
-- resultRowType: com.example.domain.PlacedOrder
-- parameters:
--   id: uuid
select id, state, monthly_price
from placed_order
where id = :id
```

The column is read as a `String` and passed to `OrderState.valueOf`. Going the other way, bind the
name: `-- parameters:` `state: string`, and pass `order.state().name()`.

## A type of your own around one column

Any type with a `static valueOf` taking a value the generator knows is built from a single column,
and its matching accessor is what gets bound on the way in. So a wrapper travels in both
directions:

```java
public record TenantId(UUID value) {

    public static TenantId valueOf(final UUID value) {
        return new TenantId(value);
    }

}
```

```sql
-- name: findTenantBySlug
-- returning: single
-- resultRowType: com.example.domain.Tenant
-- parameters:
--   tenantId: com.example.domain.TenantId
select id, slug
from tenant
where id = :tenantId
```

That is also what settles what a one-component record means: with a `valueOf` it is a value wrapped
around a column, without one it is a record whose component reads a column of its own.

## JSON columns

Drivers hand JSON back as text, so read it as a `String` and parse it where you would parse
anything else. Wrapping it in a value type keeps the parsing in one place:

```sql
-- name: findDocument
-- returning: single
-- resultRowType: com.example.domain.Document
-- parameters:
--   id: uuid
select id, payload::text as payload
from document
where id = :id
```

The `::text` cast matters on PostgreSQL: without it the driver hands back a `PGobject`, which is a
driver type the generated code will not read. Bind on the way in with an explicit
`sqlType`, so the driver knows what it is being given:

```sql
-- parameters:
--   - name: payload
--     type: java.lang.String
--     sqlType: 1111
```

## Batch inserts

Batch methods are generated alongside the single-row ones and take arrays:

```sql
-- name: insertLedgerEntry
-- returning: none
-- parameters:
--   id: long
--   tenantId: uuid
--   amountCents: long
insert into ledger_entry (id, tenant_id, amount_cents)
values (:id, :tenantId, :amountCents)
```

```java
ledger.insertLedgerEntryBatch(
        new long[]{1L, 2L, 3L},
        new UUID[]{tenantId, tenantId, tenantId},
        new long[]{500L, -200L, 1_000L});
```

The return is the `int[]` the driver gives back, one entry per row. Turn batch methods off for a
statement with `executeBatch: false` where they make no sense.

## One statement per database

Statements sharing a name but naming different vendors are one method, picking the statement at
runtime from the connection's database product name:

```sql
-- name: findTenants
-- vendor: PostgreSQL
-- returning: multiple
-- resultRowType: com.example.domain.Tenant
select id, slug from tenant order by slug fetch first 100 rows only
;

-- name: findTenants
-- vendor: MySQL
-- returning: multiple
-- resultRowType: com.example.domain.Tenant
select id, slug from tenant order by slug limit 100
;

-- name: findTenants
-- returning: multiple
-- resultRowType: com.example.domain.Tenant
select id, slug from tenant order by slug
```

The statement without a vendor is the fallback for every database not named.
