---
title: Transactions
date: 2026-08-02
menu:
  main:
    parent: SQL
categories:
  - SQL
tags:
  - transactions
  - connections
---

`YoSQL` generates no transaction management, opens no transaction of its own, and has no opinion
about where yours come from. What it generates is the one thing a transaction needs: every statement
is reachable on a connection you supply.

## Two shapes for every statement

Each statement generates two methods with the same name:

```java
Optional<Tenant> findTenant(UUID id);
Optional<Tenant> findTenant(Connection connection, UUID id);
```

The first takes a connection from the repository's `DataSource` and closes it. The second runs on
the connection it is given and leaves it open, so several statements can share one — which is all a
transaction is.

Which one a call site uses is its own business, and that is why both exist. Turning them into a
choice made per statement is what
[generateConnectionOverloads](../../configuration/repositories/generateconnectionoverloads/) is
for, and there is rarely a reason to.

## Plain JDBC

Open a connection, turn off auto-commit, and pass it to every statement in the unit of work:

```java
public void placeOrder(final UUID tenantId, final PlacedOrder order) {
    try (final var connection = dataSource.getConnection()) {
        connection.setAutoCommit(false);
        try {
            orders.insertOrder(connection, order.id(), tenantId, order.state(), order.monthlyPrice());
            ledger.insertLedgerEntry(connection, nextId(), tenantId, order.monthlyPrice());
            connection.commit();
        } catch (final RuntimeException | SQLException failure) {
            connection.rollback();
            throw failure;
        }
    }
}
```

Both repositories run on the same connection, so both statements are in the same transaction, and
neither closes it — the `try`-with-resources does, once.

Nothing about that is specific to `YoSQL`. It is the JDBC you would have written, which is the
point.

## Spring

Under Spring you do not thread a `Connection` at all. Wrap the `DataSource` in a
`TransactionAwareDataSourceProxy` and hand *that* to the repositories: `getConnection()` then returns
the connection bound to the current transaction, and the `DataSource`-based methods join whatever
`@Transactional` opened.

```java
@Configuration
public class PersistenceConfiguration {

    @Bean
    public DataSource transactionAwareDataSource(final DataSource dataSource) {
        return new TransactionAwareDataSourceProxy(dataSource);
    }

    @Bean
    public TenantRepository tenantRepository(final DataSource transactionAwareDataSource) {
        return new TenantRepository(transactionAwareDataSource);
    }

}
```

```java
@Service
public class OrderService {

    private final OrderRepository orders;
    private final LedgerRepository ledger;

    // constructor omitted

    @Transactional
    public void placeOrder(final UUID tenantId, final PlacedOrder order) {
        orders.insertOrder(order.id(), tenantId, order.state(), order.monthlyPrice());
        ledger.insertLedgerEntry(nextId(), tenantId, order.monthlyPrice());
    }

}
```

Both statements run on the transaction's connection, rollback works, and the repositories know
nothing about any of it. Use the `Connection` overloads under Spring only where you want a unit of
work that is explicitly *not* the ambient one.

Give the `PlatformTransactionManager` the **underlying** `DataSource`, not the proxy — the proxy is
for the code that consumes connections, not for the manager that binds them.

## Quarkus and Jakarta EE

An injected `DataSource` under a JTA transaction is already transaction-aware: `getConnection()`
returns the connection enlisted in the current transaction. Inject it, construct the repository with
it, and annotate the service method with `@Transactional`. As with Spring, the `Connection`
overloads are for the cases you want to keep out of the ambient transaction.

## Things worth knowing

**A `cursor` result outlives the method that returned it.** A statement with `returning: cursor`
gives you a lazy `Stream`, and the connection stays open until the stream is closed. The
`DataSource` form closes it for you when the stream closes; the `Connection` form does not close a
connection it did not open, so close the stream inside the transaction that owns it.

**Batch methods take a connection too.** `insertTenantBatch(Connection, UUID[], String[])` exists
alongside `insertTenantBatch(UUID[], String[])`, so a batch belongs in a transaction like anything
else.

**Isolation, savepoints and read-only flags are yours.** They are properties of the connection, so
set them on the connection before passing it in. `YoSQL` does not touch them.
