/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.example.records;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.example.records.domain.OrderState;
import wtf.metio.yosql.example.records.domain.Reason;
import wtf.metio.yosql.example.records.persistence.LedgerRepository;
import wtf.metio.yosql.example.records.persistence.OrderRepository;
import wtf.metio.yosql.example.records.persistence.SchemaRepository;
import wtf.metio.yosql.example.records.persistence.TenantRepository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The whole point of the module, asserted: rows go in through generated repositories and come back
 * as records, with no converter written by hand anywhere in this module.
 */
class GeneratedConverterTest {

    private static final UUID ACCOUNT = UUID.fromString("2f1d5c6a-0000-4000-8000-000000000001");
    private static final UUID TENANT = UUID.fromString("2f1d5c6a-0000-4000-8000-000000000002");
    private static final UUID OTHER_TENANT = UUID.fromString("2f1d5c6a-0000-4000-8000-000000000003");
    private static final UUID DRAFT_ORDER = UUID.fromString("2f1d5c6a-0000-4000-8000-000000000004");
    private static final UUID ACTIVE_ORDER = UUID.fromString("2f1d5c6a-0000-4000-8000-000000000005");
    private static final Instant CREATED = Instant.parse("2026-03-04T09:15:30Z");
    private static final Instant ACTIVATED = Instant.parse("2026-03-05T11:00:00Z");

    private static TenantRepository tenants;
    private static LedgerRepository ledger;
    private static OrderRepository orders;

    @BeforeAll
    static void populate() {
        final var dataSource = dataSource();
        final var schema = new SchemaRepository(dataSource);
        tenants = new TenantRepository(dataSource);
        ledger = new LedgerRepository(dataSource);
        orders = new OrderRepository(dataSource);

        schema.createTenantTable();
        schema.createLedgerEntryTable();
        schema.createOrderTable();

        schema.insertTenant(TENANT, ACCOUNT, "acme", "ACME GmbH", "EUR", "Europe/Berlin", "de",
                Timestamp.from(CREATED));
        schema.insertTenant(OTHER_TENANT, ACCOUNT, "zenith", "Zenith Ltd", "GBP", "Europe/London", "en",
                Timestamp.from(CREATED));

        schema.insertLedgerEntry(1L, TENANT, 5_000L, "EUR", "TOP_UP", "invoice-1", Timestamp.from(CREATED));
        schema.insertLedgerEntry(2L, TENANT, -1_250L, "EUR", "USAGE", "usage-2026-03", Timestamp.from(CREATED));

        // A draft has never been activated and was never cancelled: both timestamps are NULL, and
        // that is the case a mapper gets silently wrong.
        schema.insertOrder(DRAFT_ORDER, TENANT, "DRAFT", new BigDecimal("19.99"),
                Timestamp.from(CREATED), null, null);
        schema.insertOrder(ACTIVE_ORDER, TENANT, "ACTIVE", new BigDecimal("49.00"),
                Timestamp.from(CREATED), Timestamp.from(ACTIVATED), null);
    }

    @Test
    @DisplayName("a record's components are read from the columns their names imply")
    void mapsColumnsByComponentName() {
        final var tenant = tenants.findTenant(TENANT).orElseThrow();
        assertAll(
                () -> assertEquals(TENANT, tenant.id()),
                () -> assertEquals(ACCOUNT, tenant.accountId(), "account_id reads into accountId"),
                () -> assertEquals("acme", tenant.slug()),
                () -> assertEquals("ACME GmbH", tenant.name()),
                () -> assertEquals(Currency.getInstance("EUR"), tenant.currency()),
                () -> assertEquals("Europe/Berlin", tenant.timeZone(), "time_zone reads into timeZone"),
                () -> assertEquals("de", tenant.language()),
                () -> assertEquals(CREATED, tenant.createdAt(), "created_at reads into createdAt"));
    }

    @Test
    @DisplayName("every row of a multi-row result is mapped")
    void mapsEveryRow() {
        final var found = tenants.findTenantsByAccount(ACCOUNT);
        assertAll(
                () -> assertEquals(2, found.size()),
                () -> assertEquals("acme", found.get(0).slug()),
                () -> assertEquals("zenith", found.get(1).slug()),
                () -> assertEquals(Currency.getInstance("GBP"), found.get(1).currency()));
    }

    @Test
    @DisplayName("a value object is assembled from several columns of the same row")
    void assemblesNestedValueObject() {
        final var entries = ledger.findLedgerEntries(TENANT);
        assertAll(
                () -> assertEquals(2, entries.size()),
                () -> assertEquals(5_000L, entries.get(0).amount().minorUnits()),
                () -> assertEquals(Currency.getInstance("EUR"), entries.get(0).amount().currency()),
                () -> assertEquals(Reason.TOP_UP, entries.get(0).reason()),
                () -> assertEquals("invoice-1", entries.get(0).reference()),
                () -> assertEquals(CREATED, entries.get(0).at()),
                () -> assertEquals(-1_250L, entries.get(1).amount().minorUnits()),
                () -> assertEquals(Reason.USAGE, entries.get(1).reason()));
    }

    @Test
    @DisplayName("a null timestamp arrives as null, not as the epoch")
    void keepsNullNull() {
        final var draft = orders.findOrder(DRAFT_ORDER).orElseThrow();
        assertAll(
                () -> assertEquals(OrderState.DRAFT, draft.state()),
                () -> assertNull(draft.activatedAt(), "a draft has never been activated"),
                () -> assertNull(draft.cancelledAt(), "a draft has never been cancelled"),
                () -> assertEquals(0, new BigDecimal("19.99").compareTo(draft.monthlyPrice())));
    }

    @Test
    @DisplayName("a timestamp that is present survives the round trip")
    void keepsPresentTimestamps() {
        final var active = orders.findOrder(ACTIVE_ORDER).orElseThrow();
        assertAll(
                () -> assertEquals(OrderState.ACTIVE, active.state()),
                () -> assertEquals(ACTIVATED, active.activatedAt()),
                () -> assertNull(active.cancelledAt()),
                () -> assertEquals(0, new BigDecimal("49.00").compareTo(active.monthlyPrice())));
    }

    @Test
    @DisplayName("the converters are generated, not written")
    void convertersAreGenerated() {
        // The repositories above only prove the mapping runs. This proves where it came from: the
        // module ships records and .sql files, and nothing that touches a ResultSet.
        final var handWritten = java.nio.file.Path.of("src", "main", "java");
        try (final var sources = java.nio.file.Files.walk(handWritten)) {
            final var offenders = sources
                    .filter(java.nio.file.Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .filter(GeneratedConverterTest::readsAResultSet)
                    .toList();
            assertTrue(offenders.isEmpty(), () -> "hand-written result set access in " + offenders);
        } catch (final java.io.IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private static boolean readsAResultSet(final java.nio.file.Path path) {
        try {
            return java.nio.file.Files.readString(path).contains("ResultSet");
        } catch (final java.io.IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private static DataSource dataSource() {
        final var dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:records;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        return dataSource;
    }

}
