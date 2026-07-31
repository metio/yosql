/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.nativeimage;

import org.postgresql.ds.PGSimpleDataSource;
import wtf.metio.yosql.example.nativeimage.domain.Level;
import wtf.metio.yosql.example.nativeimage.domain.Reading;
import wtf.metio.yosql.example.nativeimage.persistence.ReadingRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Runs generated mapping code against a real database, and is built into a native image.
 *
 * <p>A missing reflection registration does not stop an image from linking — it stops the code path
 * that needed it, the first time it runs, in front of whoever ran it. So this asserts the values,
 * not merely that the binary started: reading a UUID, an enum out of text, a value object built
 * from two columns, and a timestamp that is NULL are each a separate way the mapping could have
 * been resolved at runtime instead of at build time.</p>
 *
 * <p>No reflection configuration is supplied for anything in this module. If the generated
 * converters ever needed one, this would fail — which is the whole point of running it.</p>
 */
public final class ReflectionFreeProof {

    private static final UUID CLEARED = UUID.fromString("8c1f0b44-0000-4000-8000-000000000001");
    private static final UUID OPEN = UUID.fromString("8c1f0b44-0000-4000-8000-000000000002");
    private static final Instant RECORDED = Instant.parse("2026-04-01T06:30:00Z");
    private static final Instant CLEARED_AT = Instant.parse("2026-04-01T07:45:00Z");

    public static void main(final String[] arguments) {
        final var repository = new ReadingRepository(dataSource());

        repository.createReadingTable();
        repository.deleteReadings();
        repository.insertReading(CLEARED, "boiler-1", "WARNING", new BigDecimal("81.500"), "celsius",
                Timestamp.from(RECORDED), Timestamp.from(CLEARED_AT));
        repository.insertReading(OPEN, "boiler-1", "CRITICAL", new BigDecimal("97.250"), "celsius",
                Timestamp.from(RECORDED.plusSeconds(60)), null);

        final var cleared = repository.findReading(CLEARED).orElseThrow(
                () -> new AssertionError("the row that was just inserted was not found"));
        check("id", CLEARED, cleared.id());
        check("sensorId", "boiler-1", cleared.sensorId());
        check("level", Level.WARNING, cleared.level());
        check("unit", "celsius", cleared.measurement().unit());
        check("amount", 0, new BigDecimal("81.500").compareTo(cleared.measurement().amount()));
        check("recordedAt", RECORDED, cleared.recordedAt());
        check("clearedAt", CLEARED_AT, cleared.clearedAt());

        final var open = repository.findReading(OPEN).orElseThrow(
                () -> new AssertionError("the second row was not found"));
        check("level", Level.CRITICAL, open.level());
        check("clearedAt", null, open.clearedAt());

        final var bySensor = repository.findReadingsBySensor("boiler-1");
        check("row count", 2, bySensor.size());
        check("first row", CLEARED, bySensor.get(0).id());
        check("second row", OPEN, bySensor.get(1).id());

        System.out.println("generated mapping produced " + bySensor.size()
                + " rows with no reflection registered");
    }

    private static void check(final String what, final Object expected, final Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("%s: expected %s but was %s".formatted(what, expected, actual));
        }
    }

    /**
     * Built directly rather than looked up through {@code DriverManager}, so the binary needs no
     * service-loader registration to reach the database.
     */
    private static PGSimpleDataSource dataSource() {
        final var dataSource = new PGSimpleDataSource();
        dataSource.setUrl(required("DATABASE_URL"));
        dataSource.setUser(required("DATABASE_USER"));
        dataSource.setPassword(System.getenv().getOrDefault("DATABASE_PASSWORD", ""));
        return dataSource;
    }

    private static String required(final String variable) {
        final var value = System.getenv(variable);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(variable + " must be set");
        }
        return value;
    }

    private ReflectionFreeProof() {
        // main class
    }

}
