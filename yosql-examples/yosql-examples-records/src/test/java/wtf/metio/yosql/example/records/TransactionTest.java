/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.records;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.example.records.persistence.LedgerRepository;
import wtf.metio.yosql.example.records.persistence.SchemaRepository;
import wtf.metio.yosql.example.records.persistence.TenantRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Several statements on one connection, committed or rolled back together.
 *
 * <p>Every statement generates a method that opens its own connection and one that takes a
 * connection, and only the second can be part of a unit of work somebody else controls. What is
 * asserted here is that it really is: a rollback has to undo the writes of both repositories, which
 * it can only do if both ran on the same connection.</p>
 */
@DisplayName("statements sharing a connection share a transaction")
class TransactionTest {

    private static final AtomicLong ENTRY_ID = new AtomicLong(1_000L);

    private DataSource dataSource;
    private SchemaRepository schema;
    private TenantRepository tenants;
    private LedgerRepository ledger;

    @BeforeEach
    void createSchema() {
        // A database per test, so a rolled-back write cannot be confused with one another test made.
        final var source = new JdbcDataSource();
        source.setURL("jdbc:h2:mem:transactions-" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1");
        dataSource = source;
        schema = new SchemaRepository(dataSource);
        tenants = new TenantRepository(dataSource);
        ledger = new LedgerRepository(dataSource);

        schema.createTenantTable();
        schema.createLedgerEntryTable();
    }

    @Test
    @DisplayName("a commit keeps the writes of every repository that took part")
    void shouldCommitTogether() throws SQLException {
        final var account = UUID.randomUUID();
        final var tenant = UUID.randomUUID();

        try (final var connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            insertTenant(connection, tenant, account);
            insertLedgerEntry(connection, tenant, 5_000L);
            connection.commit();
        }

        assertAll(
                () -> assertTrue(tenants.findTenant(tenant).isPresent()),
                () -> assertEquals(1, ledger.findLedgerEntries(tenant).size()));
    }

    @Test
    @DisplayName("a rollback undoes them all, which only works if they shared the connection")
    void shouldRollBackTogether() throws SQLException {
        final var account = UUID.randomUUID();
        final var tenant = UUID.randomUUID();

        try (final var connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            insertTenant(connection, tenant, account);
            insertLedgerEntry(connection, tenant, 5_000L);
            connection.rollback();
        }

        assertAll(
                () -> assertTrue(tenants.findTenant(tenant).isEmpty()),
                () -> assertTrue(ledger.findLedgerEntries(tenant).isEmpty()));
    }

    @Test
    @DisplayName("a failure part way through leaves nothing behind")
    void shouldRollBackWhatWasWrittenBeforeTheFailure() throws SQLException {
        final var account = UUID.randomUUID();
        final var tenant = UUID.randomUUID();

        try (final var connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                insertTenant(connection, tenant, account);
                insertLedgerEntry(connection, tenant, 5_000L);
                // The primary key is already taken by the tenant written above.
                assertThrows(RuntimeException.class, () -> insertTenant(connection, tenant, account));
                throw new IllegalStateException("the unit of work failed");
            } catch (final IllegalStateException _) {
                connection.rollback();
            }
        }

        assertAll(
                () -> assertTrue(tenants.findTenant(tenant).isEmpty()),
                () -> assertTrue(ledger.findLedgerEntries(tenant).isEmpty()));
    }

    @Test
    @DisplayName("the DataSource methods commit on their own, which is why a transaction needs the others")
    void shouldNotJoinATransactionWithoutBeingGivenTheConnection() throws SQLException {
        final var account = UUID.randomUUID();
        final var tenant = UUID.randomUUID();

        try (final var connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            // Takes its own connection from the DataSource, so it is committed before the rollback.
            schema.insertTenant(tenant, account, "acme", "ACME GmbH", "EUR", "Europe/Berlin", "de",
                    Timestamp.from(Instant.now()));
            connection.rollback();
        }

        assertTrue(tenants.findTenant(tenant).isPresent());
    }

    private void insertTenant(final Connection connection, final UUID tenant, final UUID account) {
        schema.insertTenant(connection, tenant, account, "acme", "ACME GmbH", "EUR", "Europe/Berlin", "de",
                Timestamp.from(Instant.now()));
    }

    private void insertLedgerEntry(final Connection connection, final UUID tenant, final long amount) {
        schema.insertLedgerEntry(connection, ENTRY_ID.incrementAndGet(), tenant, amount, "EUR", "TOP_UP",
                "invoice-1", Timestamp.from(Instant.now()));
    }

}
