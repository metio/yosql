/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.records;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.example.records.domain.Cents;
import wtf.metio.yosql.example.records.domain.Slug;
import wtf.metio.yosql.example.records.domain.TenantId;
import wtf.metio.yosql.example.records.persistence.RegistrationRepository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A row whose every component is a type that knows how to build itself from one column.
 */
class ValueTypeTest {

    private static final UUID TENANT = UUID.fromString("5d0c9e11-0000-4000-8000-000000000001");
    private static final Instant REGISTERED = Instant.parse("2026-02-11T08:00:00Z");

    private static RegistrationRepository registrations;

    @BeforeAll
    static void populate() {
        registrations = new RegistrationRepository(dataSource());
        registrations.createRegistrationTable();
        // Stored with capitals and padding, so the factory's normalisation is observable rather
        // than merely executed.
        registrations.insertRegistration(TENANT, "  ACME-GmbH  ", 12_345L, Timestamp.from(REGISTERED));
    }

    @Test
    @DisplayName("each column is read and handed to its type's own factory")
    void buildsValueTypesFromColumns() {
        final var registration = registrations.findRegistration(TENANT).orElseThrow();
        assertAll(
                () -> assertEquals(new TenantId(TENANT), registration.tenantId(), "tenant_id wraps a UUID"),
                () -> assertEquals(new Cents(12_345L), registration.balance(), "balance wraps a primitive"),
                () -> assertEquals(REGISTERED, registration.registeredAt(), "and a plain type still reads plainly"));
    }

    @Test
    @DisplayName("a value read from the database went through the factory, not around it")
    void theFactoryActuallyRuns() {
        // Slug.valueOf strips and lower-cases. The stored text does neither, so an equal value here
        // proves the generated converter called the factory rather than the canonical constructor.
        assertEquals(Slug.valueOf("acme-gmbh"), registrations.findRegistration(TENANT).orElseThrow().slug());
        assertEquals("acme-gmbh", registrations.findRegistration(TENANT).orElseThrow().slug().value());
    }

    private static DataSource dataSource() {
        final var dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:valuetypes;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        return dataSource;
    }

}
