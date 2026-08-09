/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.nativeimage;

import org.postgresql.ds.PGSimpleDataSource;
import wtf.metio.yosql.example.nativeimage.domain.Level;
import wtf.metio.yosql.example.nativeimage.domain.Reading;
import wtf.metio.yosql.example.nativeimage.domain.ReadingId;
import wtf.metio.yosql.example.nativeimage.persistence.DocumentRepository;
import wtf.metio.yosql.example.nativeimage.persistence.ReadingRepository;

import java.math.BigDecimal;
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
    /** Keys out of order and no space after the colon, so that normalisation is visible. */
    private static final String WRITTEN = "{\"b\":2,\"a\":1}";

    public static void main(final String[] arguments) {
        final var repository = new ReadingRepository(dataSource());

        repository.createReadingTable();
        repository.deleteReadings();
        repository.insertReading(CLEARED, "boiler-1", "WARNING", new BigDecimal("81.500"), "celsius",
                RECORDED, CLEARED_AT);
        repository.insertReading(OPEN, "boiler-1", "CRITICAL", new BigDecimal("97.250"), "celsius",
                RECORDED.plusSeconds(60), null);

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

        // `insert … returning id` is a write that answers with a row, so it runs through the read
        // path: executeQuery, then the generated converter. Postgres is what makes it meaningful —
        // the statement is only legal there.
        final var returned = UUID.fromString("8c1f0b44-0000-4000-8000-000000000003");
        final var inserted = repository.insertReadingReturningId(returned, "boiler-2", "INFO",
                new BigDecimal("21.000"), "celsius", RECORDED).orElseThrow(
                () -> new AssertionError("insert … returning id produced no row"));
        check("returning id", new ReadingId(returned), inserted);

        documents();

        final var bySensor = repository.findReadingsBySensor("boiler-1");
        check("row count", 2, bySensor.size());
        check("first row", CLEARED, bySensor.getFirst().id());
        check("second row", OPEN, bySensor.get(1).id());

        System.out.println("generated mapping produced " + bySensor.size()
                + " rows with no reflection registered");
    }

    /**
     * What a {@code jsonb} column arrives as.
     *
     * <p>Postgres has no JDBC type of its own for JSON: the driver hands back a
     * {@code PGobject} from {@code getObject}, and refuses {@code getObject(column, String.class)}
     * outright. The catalog maps {@code json} and {@code jsonb} to {@code String} and the generated
     * reader takes the {@code getString} branch, which is the accessor the driver does answer — so
     * the record holds the JSON as text. The record here is written by the generator, not by hand,
     * so the field type is the catalog's choice rather than a guess repeated back at it.</p>
     *
     * <p>The two columns hold the same text written the same way and read back differently:
     * {@code jsonb} is stored parsed, so it comes back with its keys ordered and its whitespace
     * normalised, while {@code json} keeps the bytes it was given. Asserting both is what
     * distinguishes reading the database's rendering from echoing the input.</p>
     *
     * <p>Going the other way needs the cast the statement carries. A {@code String} parameter is
     * bound as {@code varchar}, and Postgres will not compare or assign that to {@code jsonb} — it
     * fails with "column is of type jsonb but expression is of type character varying". The cast is
     * the statement author's job; nothing in the generated code can add it, because the type the
     * parameter needs to arrive as is a property of the SQL.</p>
     */
    private static void documents() {
        final var repository = new DocumentRepository(dataSource());
        repository.createDocumentTable();
        repository.deleteDocuments();

        final var id = UUID.fromString("8c1f0b44-0000-4000-8000-000000000004");
        repository.insertDocument(id, WRITTEN, WRITTEN);

        final var document = repository.findDocument(id).orElseThrow(
                () -> new AssertionError("the document that was just inserted was not found"));
        check("jsonb", "{\"a\": 1, \"b\": 2}", document.payload());
        check("json", WRITTEN, document.plain());

        // A statement with no result row type reads through the map converter, and that one asks
        // for getObject because it has no type to read towards. Postgres answers a jsonb column
        // with a PGobject, so the same column is a String in a record and a driver type in a map —
        // and code reading the map has to expect that rather than cast to String. Asserting the
        // class name rather than the value is the point: the value's toString is the JSON either
        // way, which is exactly what makes the difference easy to miss.
        final var columns = repository.findDocumentColumns(id).orElseThrow(
                () -> new AssertionError("the document was not found through the map converter"));
        check("map jsonb", "org.postgresql.util.PGobject", columns.get("payload").getClass().getName());
        check("map uuid", UUID.class, columns.get("id").getClass());
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
