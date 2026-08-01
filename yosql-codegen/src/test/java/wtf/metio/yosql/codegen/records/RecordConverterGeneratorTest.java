/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import wtf.metio.yosql.codegen.blocks.BlocksObjectMother;
import wtf.metio.yosql.codegen.dao.DaoObjectMother;
import wtf.metio.yosql.codegen.exceptions.AmbiguousValueOfException;
import wtf.metio.yosql.codegen.exceptions.ConflictingColumnOverrideException;
import wtf.metio.yosql.codegen.exceptions.DuplicateConverterNameException;
import wtf.metio.yosql.codegen.exceptions.MissingRecordSourceException;
import wtf.metio.yosql.codegen.exceptions.RecursiveRecordException;
import wtf.metio.yosql.codegen.exceptions.ScalarResultColumnsException;
import wtf.metio.yosql.codegen.exceptions.UnreadableResultRowTypeException;
import wtf.metio.yosql.codegen.exceptions.UnmappedColumnsException;
import wtf.metio.yosql.codegen.exceptions.UnparsableRecordException;
import wtf.metio.yosql.codegen.exceptions.UnsupportedComponentTypeException;
import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.internals.testing.configs.ConverterConfigurations;
import wtf.metio.yosql.internals.testing.configs.JavaConfigurations;
import wtf.metio.yosql.internals.testing.configs.NamesConfigurations;
import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.configuration.SqlStatementType;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("RecordConverterGenerator")
class RecordConverterGeneratorTest {

    private static final String DOMAIN = "com.example.domain";

    @TempDir
    Path sources;

    private void write(final String simpleName, final String body) {
        try {
            final var directory = sources.resolve(DOMAIN.replace('.', '/'));
            Files.createDirectories(directory);
            Files.writeString(directory.resolve(simpleName + ".java"), body);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private RecordConverterGenerator generator() {
        final var java = JavaConfigurations.defaults();
        final var converters = ConverterConfigurations.withConverters();
        return new RecordConverterGenerator(
                LoggingObjectMother.logger(),
                new RecordScanner(
                        FilesConfiguration.builder().setSourceDirectory(sources).build(),
                        new JavaSourceParser()),
                new RecordConverterNames(converters),
                NamesConfigurations.defaults(),
                BlocksObjectMother.annotationGenerator(),
                BlocksObjectMother.classes(java),
                BlocksObjectMother.methods(java),
                DaoObjectMother.jdbcParameter(java),
                DaoObjectMother.jdbcMethodExceptionHandler());
    }

    private static SqlStatement statement(final String name, final String resultRowType, final String sql) {
        return SqlStatement.builder()
                .setSourcePath(Path.of("src", "main", "yosql", "tenant", name + ".sql"))
                .setConfiguration(SqlConfiguration.builder()
                        .setName(name)
                        .setType(SqlStatementType.READING)
                        .setReturningMode(ReturningMode.MULTIPLE)
                        .setRepository("com.example.persistence.TenantRepository")
                        .setResultRowType(resultRowType)
                        .build())
                .setRawStatement(sql)
                .build();
    }

    private static SqlStatement statement(
            final String name, final String resultRowType, final String sql, final Map<String, String> columns) {
        return SqlStatement.builder()
                .setSourcePath(Path.of("src", "main", "yosql", "tenant", name + ".sql"))
                .setConfiguration(SqlConfiguration.builder()
                        .setName(name)
                        .setType(SqlStatementType.READING)
                        .setReturningMode(ReturningMode.MULTIPLE)
                        .setRepository("com.example.persistence.TenantRepository")
                        .setResultRowType(resultRowType)
                        .setResultRowColumns(columns)
                        .build())
                .setRawStatement(sql)
                .build();
    }

    private static SqlStatement withoutResultRowType() {
        return SqlStatement.builder()
                .setSourcePath(Path.of("src", "main", "yosql", "tenant", "count.sql"))
                .setConfiguration(SqlConfiguration.builder()
                        .setName("countTenants")
                        .setType(SqlStatementType.READING)
                        .setReturningMode(ReturningMode.SINGLE)
                        .setRepository("com.example.persistence.TenantRepository")
                        .build())
                .setRawStatement("select count(*) as total from tenant")
                .build();
    }

    private String generateOne(final List<SqlStatement> statements) {
        final var generated = generator().generateConverterClasses(statements).toList();
        assertEquals(1, generated.size(), () -> "expected exactly one converter, got " + generated);
        return render(generated.get(0));
    }

    private static String render(final PackagedTypeSpec spec) {
        // TypeSpec renders every name fully qualified; a JavaFile would shorten them through
        // imports and make these assertions depend on what else the file imports.
        return spec.getType().toString();
    }

    private void writeTenant() {
        write("Tenant", """
                package com.example.domain;

                import java.time.Instant;
                import java.util.UUID;

                public record Tenant(UUID id, String slug, Instant createdAt) {
                }
                """);
    }

    private void writeLedger() {
        write("Money", """
                package com.example.domain;

                import java.util.Currency;

                public record Money(long minorUnits, Currency currency) {
                }
                """);
        write("Reason", """
                package com.example.domain;

                public enum Reason {
                    TOP_UP,
                    USAGE
                }
                """);
        write("LedgerEntry", """
                package com.example.domain;

                import java.time.Instant;

                public record LedgerEntry(long id, Money amount, Reason reason, Instant at) {
                }
                """);
    }

    @Nested
    @DisplayName("what it generates")
    class Output {

        @Test
        @DisplayName("writes one converter per result row type")
        void oneConverterPerType() {
            writeTenant();
            writeLedger();
            final var generated = generator().generateConverterClasses(List.of(
                    statement("findTenant", DOMAIN + ".Tenant",
                            "select id, slug, created_at from tenant where id = ?"),
                    statement("findTenants", DOMAIN + ".Tenant",
                            "select id, slug, created_at from tenant"),
                    statement("findEntries", DOMAIN + ".LedgerEntry",
                            "select id, amount_cents as minor_units, currency, reason, created_at as at from ledger")
            )).toList();
            assertEquals(2, generated.size());
            assertEquals(List.of("ToTenantConverter", "ToLedgerEntryConverter"),
                    generated.stream().map(spec -> spec.getType().name()).toList());
        }

        @Test
        @DisplayName("ignores statements that name no result row type")
        void ignoresOtherStatements() {
            assertEquals(0, generator().generateConverterClasses(List.of(withoutResultRowType())).count());
        }

        @Test
        @DisplayName("reads each component from the column its name implies")
        void mapsByName() {
            writeTenant();
            final var code = generateOne(List.of(statement("findTenant", DOMAIN + ".Tenant",
                    "select id, slug, created_at from tenant where id = ?")));
            assertTrue(code.contains("getObject(\"id\", java.util.UUID.class)"), code);
            assertTrue(code.contains("getString(\"slug\")"), code);
            assertTrue(code.contains("getTimestamp(\"created_at\")"), code);
            assertTrue(code.contains("return new com.example.domain.Tenant("), code);
        }

        @Test
        @DisplayName("builds a nested record from the same flat row")
        void nestedRecord() {
            writeLedger();
            final var code = generateOne(List.of(statement("findEntries", DOMAIN + ".LedgerEntry",
                    "select id, amount_cents as minor_units, currency, reason, created_at as at from ledger")));
            assertTrue(code.contains("getLong(\"minor_units\")"), code);
            assertTrue(code.contains("getString(\"currency\")"), code);
            assertTrue(code.contains("new com.example.domain.Money(amountMinorUnits, amountCurrency)"), code);
            assertTrue(code.contains("new com.example.domain.LedgerEntry(id, "
                    + "new com.example.domain.Money(amountMinorUnits, amountCurrency), reason, at)"), code);
        }

        @Test
        @DisplayName("reads an enum component as text")
        void enumComponent() {
            writeLedger();
            final var code = generateOne(List.of(statement("findEntries", DOMAIN + ".LedgerEntry",
                    "select id, amount_cents as minor_units, currency, reason, created_at as at from ledger")));
            assertTrue(code.contains("com.example.domain.Reason.valueOf(reasonName)"), code);
        }

        @Test
        @DisplayName("leaves nothing for a native image to resolve at runtime")
        void carriesNoReflection() {
            writeLedger();
            final var code = generateOne(List.of(statement("findEntries", DOMAIN + ".LedgerEntry",
                    "select id, amount_cents as minor_units, currency, reason, created_at as at from ledger")));
            assertFalse(code.contains("java.lang.reflect"), code);
            assertFalse(code.contains("Class.forName"), code);
            assertFalse(code.contains(".getDeclaredConstructor"), code);
            assertFalse(code.contains("MethodHandles"), code);
            assertFalse(code.contains("getObject(\"reason\")"), code);
        }

        @Test
        @DisplayName("names each leaf's local after its path through the record")
        void variablesAreNamedByPath() {
            write("Inner", """
                    package com.example.domain;

                    public record Inner(long weight) {
                    }
                    """);
            write("Outer", """
                    package com.example.domain;

                    public record Outer(long id, Inner inner) {
                    }
                    """);
            final var code = generateOne(List.of(statement("findOuter", DOMAIN + ".Outer",
                    "select id, weight from outer")));
            assertTrue(code.contains("final long id = "), code);
            assertTrue(code.contains("final long innerWeight = "), code);
            assertTrue(code.contains("new com.example.domain.Outer(id, new com.example.domain.Inner(innerWeight))"),
                    code);
        }

    }

    @Nested
    @DisplayName("types that build themselves")
    class ValueFactories {

        private void writeTenantId() {
            write("TenantId", """
                    package com.example.domain;

                    import java.util.UUID;

                    public record TenantId(UUID value) {
                        public static TenantId valueOf(final UUID value) {
                            return new TenantId(value);
                        }
                    }
                    """);
        }

        @Test
        @DisplayName("reads one column and hands it to the type's own valueOf")
        void wrapsAColumn() {
            writeTenantId();
            write("Tenant", """
                    package com.example.domain;

                    public record Tenant(TenantId id, String slug) {
                    }
                    """);
            final var code = generateOne(List.of(statement("findTenant", DOMAIN + ".Tenant",
                    "select id, slug from tenant")));
            assertTrue(code.contains("getObject(\"id\", java.util.UUID.class)"), code);
            assertTrue(code.contains("com.example.domain.TenantId.valueOf(idValue)"), code);
            assertTrue(code.contains("new com.example.domain.Tenant(id, slug)"), code);
        }

        @Test
        @DisplayName("does not call the factory for a column that is NULL")
        void nullSkipsTheFactory() {
            writeTenantId();
            write("Tenant", """
                    package com.example.domain;

                    public record Tenant(TenantId id, String slug) {
                    }
                    """);
            final var code = generateOne(List.of(statement("findTenant", DOMAIN + ".Tenant",
                    "select id, slug from tenant")));
            assertTrue(code.contains("idValue == null ? null : com.example.domain.TenantId.valueOf(idValue)"), code);
        }

        @Test
        @DisplayName("a primitive parameter still refuses NULL rather than wrapping a zero")
        void primitiveParameterRefusesNull() {
            write("Cents", """
                    package com.example.domain;

                    public record Cents(long value) {
                        public static Cents valueOf(long value) {
                            return new Cents(value);
                        }
                    }
                    """);
            write("Tenant", """
                    package com.example.domain;

                    public record Tenant(Cents balance) {
                    }
                    """);
            final var code = generateOne(List.of(statement("findTenant", DOMAIN + ".Tenant",
                    "select balance from tenant")));
            assertTrue(code.contains("getLong(\"balance\")"), code);
            assertTrue(code.contains("wasNull()"), code);
            assertTrue(code.contains("com.example.domain.Cents.valueOf(balanceValue)"), code);
            assertFalse(code.contains("balanceValue == null"), code);
        }

        @Test
        @DisplayName("the factory decides that a one-component record is a value, not a nesting")
        void factoryWinsOverNesting() {
            writeTenantId();
            write("Tenant", """
                    package com.example.domain;

                    public record Tenant(TenantId id) {
                    }
                    """);
            final var code = generateOne(List.of(statement("findTenant", DOMAIN + ".Tenant",
                    "select id from tenant")));
            assertTrue(code.contains("getObject(\"id\""), code);
            assertFalse(code.contains("getObject(\"value\""), "without a factory this would read the inner name");
        }

        @Test
        @DisplayName("without a factory the same shape is still a nested record")
        void withoutFactoryItNests() {
            write("TenantId", """
                    package com.example.domain;

                    import java.util.UUID;

                    public record TenantId(UUID value) {
                    }
                    """);
            write("Tenant", """
                    package com.example.domain;

                    public record Tenant(TenantId id) {
                    }
                    """);
            final var code = generateOne(List.of(statement("findTenant", DOMAIN + ".Tenant",
                    "select value from tenant")));
            assertTrue(code.contains("getObject(\"value\""), code);
        }

        @Test
        @DisplayName("validates the column the factory reads, not the one inside the type")
        void validatesTheOuterColumn() {
            writeTenantId();
            write("Tenant", """
                    package com.example.domain;

                    public record Tenant(TenantId id) {
                    }
                    """);
            final var exception = assertThrows(UnmappedColumnsException.class, () ->
                    generator().generateConverterClasses(List.of(statement("findTenant", DOMAIN + ".Tenant",
                            "select value from tenant"))).toList());
            assertTrue(exception.getMessage().contains("id"), exception.getMessage());
        }

        @Test
        @DisplayName("an override names the column the factory reads")
        void honoursColumnOverrides() {
            writeTenantId();
            write("Tenant", """
                    package com.example.domain;

                    public record Tenant(TenantId id) {
                    }
                    """);
            final var code = generateOne(List.of(statement("findTenant", DOMAIN + ".Tenant",
                    "select tenant_uuid from tenant", Map.of("id", "tenant_uuid"))));
            assertTrue(code.contains("getObject(\"tenant_uuid\""), code);
        }

        @Test
        @DisplayName("refuses a type offering two ways to be built from a column")
        void ambiguousFactories() {
            write("Ident", """
                    package com.example.domain;

                    import java.util.UUID;

                    public record Ident(String value) {
                        public static Ident valueOf(String value) {
                            return new Ident(value);
                        }

                        public static Ident valueOf(UUID value) {
                            return new Ident(value.toString());
                        }
                    }
                    """);
            write("Tenant", """
                    package com.example.domain;

                    public record Tenant(Ident id) {
                    }
                    """);
            final var exception = assertThrows(AmbiguousValueOfException.class, () ->
                    generator().generateConverterClasses(List.of(statement("findTenant", DOMAIN + ".Tenant",
                            "select id from tenant"))).toList());
            assertTrue(exception.getMessage().contains("java.lang.String"), exception.getMessage());
            assertTrue(exception.getMessage().contains("java.util.UUID"), exception.getMessage());
        }

        @Test
        @DisplayName("a factory taking something unreadable is no factory at all")
        void unreadableParameterIsIgnored() {
            write("Address", """
                    package com.example.domain;

                    public final class Address {
                    }
                    """);
            write("Ident", """
                    package com.example.domain;

                    public final class Ident {
                        public static Ident valueOf(Address address) {
                            return new Ident();
                        }
                    }
                    """);
            write("Tenant", """
                    package com.example.domain;

                    public record Tenant(Ident id) {
                    }
                    """);
            final var exception = assertThrows(UnsupportedComponentTypeException.class, () ->
                    generator().generateConverterClasses(List.of(statement("findTenant", DOMAIN + ".Tenant",
                            "select id from tenant"))).toList());
            assertTrue(exception.getMessage().contains("valueOf"), exception.getMessage());
        }

        @Test
        @DisplayName("names valueOf when it cannot read a component's type")
        void diagnosticSuggestsTheFactory() {
            write("Address", """
                    package com.example.domain;

                    public final class Address {
                    }
                    """);
            write("Tenant", """
                    package com.example.domain;

                    public record Tenant(Address address) {
                    }
                    """);
            final var exception = assertThrows(UnsupportedComponentTypeException.class, () ->
                    generator().generateConverterClasses(List.of(statement("findTenant", DOMAIN + ".Tenant",
                            "select address from tenant"))).toList());
            assertTrue(exception.getMessage().contains("static Address valueOf"), exception.getMessage());
        }

    }

    @Nested
    @DisplayName("results that are one value")
    class Scalars {

        @Test
        @DisplayName("reads a boxed primitive from the first column")
        void boxedPrimitive() {
            final var code = generateOne(List.of(statement("countTenants", "java.lang.Long",
                    "select count(*) from tenant")));
            assertTrue(code.contains("resultSet.getObject(1, java.lang.Long.class)"), code);
            assertTrue(code.contains("java.lang.Long asUserType"), code);
        }

        @Test
        @DisplayName("reads a String, a UUID and a BigDecimal the same way")
        void otherSupportedTypes() {
            assertTrue(generateOne(List.of(statement("findSlug", "java.lang.String",
                    "select slug from tenant"))).contains("resultSet.getString(1)"));
            assertTrue(generateOne(List.of(statement("findId", "java.util.UUID",
                    "select id from tenant"))).contains("resultSet.getObject(1, java.util.UUID.class)"));
            assertTrue(generateOne(List.of(statement("findPrice", "java.math.BigDecimal",
                    "select price from tenant"))).contains("resultSet.getBigDecimal(1)"));
        }

        @Test
        @DisplayName("reads an Instant from the first column, null-safely")
        void instant() {
            final var code = generateOne(List.of(statement("findCreatedAt", "java.time.Instant",
                    "select created_at from tenant")));
            assertTrue(code.contains("resultSet.getTimestamp(1)"), code);
            assertTrue(code.contains("== null ? null :"), code);
        }

        @Test
        @DisplayName("reads an enum from the first column")
        void enums() {
            write("OrderState", """
                    package com.example.domain;

                    public enum OrderState {
                        DRAFT,
                        ACTIVE
                    }
                    """);
            final var code = generateOne(List.of(statement("findState", DOMAIN + ".OrderState",
                    "select state from placed_order")));
            assertTrue(code.contains("resultSet.getString(1)"), code);
            assertTrue(code.contains("com.example.domain.OrderState.valueOf"), code);
        }

        @Test
        @DisplayName("builds a value type from the first column instead of treating it as a row")
        void valueType() {
            write("TenantId", """
                    package com.example.domain;

                    import java.util.UUID;

                    public record TenantId(UUID value) {
                        public static TenantId valueOf(final UUID value) {
                            return new TenantId(value);
                        }
                    }
                    """);
            final var code = generateOne(List.of(statement("findId", DOMAIN + ".TenantId",
                    "select id from tenant")));
            assertTrue(code.contains("resultSet.getObject(1, java.util.UUID.class)"), code);
            assertTrue(code.contains("com.example.domain.TenantId.valueOf"), code);
            assertFalse(code.contains("getObject(\"value\""), "a factory means one column, not a row");
        }

        @Test
        @DisplayName("still treats a one-component record without a factory as a row")
        void recordWithoutFactoryStaysARow() {
            write("ReadingId", """
                    package com.example.domain;

                    import java.util.UUID;

                    public record ReadingId(UUID id) {
                    }
                    """);
            assertTrue(generateOne(List.of(statement("findId", DOMAIN + ".ReadingId",
                    "select id from reading"))).contains("getObject(\"id\""));
        }

        @Test
        @DisplayName("refuses a statement selecting more than one column")
        void tooManyColumns() {
            final var exception = assertThrows(ScalarResultColumnsException.class, () ->
                    generator().generateConverterClasses(List.of(statement("findBoth", "java.lang.Long",
                            "select id, tenant_id from tenant"))).toList());
            assertTrue(exception.getMessage().contains("selects 2 columns"), exception.getMessage());
            assertTrue(exception.getMessage().contains("id, tenant_id"), exception.getMessage());
        }

        @Test
        @DisplayName("says nothing about a select list it cannot enumerate")
        void unenumerableSelectList() {
            assertTrue(generateOne(List.of(statement("countTenants", "java.lang.Long",
                    "select count(*) from tenant"))).contains("getObject(1"));
        }

        @Test
        @DisplayName("refuses a primitive, which cannot answer an absent row")
        void primitivesAreRefused() {
            final var exception = assertThrows(UnreadableResultRowTypeException.class, () ->
                    generator().generateConverterClasses(List.of(statement("countTenants", "long",
                            "select count(*) from tenant"))).toList());
            assertTrue(exception.getMessage().contains("wrapper"), exception.getMessage());
        }

        @Test
        @DisplayName("writes one converter per scalar type, however many statements use it")
        void oneConverterPerType() {
            final var generated = generator().generateConverterClasses(List.of(
                    statement("countTenants", "java.lang.Long", "select count(*) from tenant"),
                    statement("countOrders", "java.lang.Long", "select count(*) from placed_order")
            )).toList();
            assertEquals(1, generated.size());
        }

    }

    @Nested
    @DisplayName("column overrides")
    class Overrides {

        @Test
        @DisplayName("reads the named column instead of the one the component name implies")
        void overridesAColumn() {
            writeTenant();
            final var code = generateOne(List.of(statement("findTenant", DOMAIN + ".Tenant",
                    "select id, slug, inserted_at from tenant",
                    Map.of("createdAt", "inserted_at"))));
            assertTrue(code.contains("getTimestamp(\"inserted_at\")"), code);
            assertFalse(code.contains("getTimestamp(\"created_at\")"), code);
        }

        @Test
        @DisplayName("addresses a nested component through the component holding it")
        void overridesANestedColumn() {
            writeLedger();
            final var code = generateOne(List.of(statement("findEntries", DOMAIN + ".LedgerEntry",
                    "select id, amount_cents, currency, reason, created_at",
                    Map.of("amount.minorUnits", "amount_cents", "at", "created_at"))));
            assertTrue(code.contains("getLong(\"amount_cents\")"), code);
            assertTrue(code.contains("getTimestamp(\"created_at\")"), code);
        }

        @Test
        @DisplayName("checks the overridden column, not the derived one")
        void validatesAgainstTheOverride() {
            writeTenant();
            final var exception = assertThrows(UnmappedColumnsException.class, () ->
                    generator().generateConverterClasses(List.of(statement("findTenant", DOMAIN + ".Tenant",
                            "select id, slug, created_at from tenant",
                            Map.of("createdAt", "inserted_at")))).toList());
            assertTrue(exception.getMessage().contains("inserted_at"), exception.getMessage());
        }

        @Test
        @DisplayName("applies to every statement naming the type, wherever it was declared")
        void sharedAcrossStatements() {
            writeTenant();
            final var generated = generator().generateConverterClasses(List.of(
                    statement("findTenant", DOMAIN + ".Tenant",
                            "select id, slug, inserted_at from tenant",
                            Map.of("createdAt", "inserted_at")),
                    // declares nothing, and still reads inserted_at
                    statement("findTenants", DOMAIN + ".Tenant",
                            "select id, slug, inserted_at from tenant")
            )).toList();
            assertEquals(1, generated.size());
            assertTrue(render(generated.get(0)).contains("getTimestamp(\"inserted_at\")"));
        }

        @Test
        @DisplayName("refuses two statements that map one component to different columns")
        void conflictingOverrides() {
            writeTenant();
            final var exception = assertThrows(ConflictingColumnOverrideException.class, () ->
                    generator().generateConverterClasses(List.of(
                            statement("findTenant", DOMAIN + ".Tenant", "select id, slug, inserted_at from tenant",
                                    Map.of("createdAt", "inserted_at")),
                            statement("findTenants", DOMAIN + ".Tenant", "select id, slug, added_at from tenant",
                                    Map.of("createdAt", "added_at"))
                    )).toList());
            assertTrue(exception.getMessage().contains("createdAt"), exception.getMessage());
            assertTrue(exception.getMessage().contains("inserted_at"), exception.getMessage());
            assertTrue(exception.getMessage().contains("added_at"), exception.getMessage());
            assertTrue(exception.getMessage().contains("findTenants"), exception.getMessage());
        }

        @Test
        @DisplayName("accepts the same override declared twice")
        void repeatedIdenticalOverrides() {
            writeTenant();
            final var generated = generator().generateConverterClasses(List.of(
                    statement("findTenant", DOMAIN + ".Tenant", "select id, slug, inserted_at from tenant",
                            Map.of("createdAt", "inserted_at")),
                    statement("findTenants", DOMAIN + ".Tenant", "select id, slug, inserted_at from tenant",
                            Map.of("createdAt", "inserted_at"))
            )).toList();
            assertEquals(1, generated.size());
        }

    }

    @Nested
    @DisplayName("what it refuses")
    class Refusals {

        @Test
        @DisplayName("names the file and the statement when a component has no column")
        void componentWithoutColumn() {
            writeTenant();
            final var exception = assertThrows(UnmappedColumnsException.class, () ->
                    generator().generateConverterClasses(List.of(statement("findTenant", DOMAIN + ".Tenant",
                            "select id, slug from tenant"))).toList());
            assertTrue(exception.getMessage().contains("findTenant"), exception.getMessage());
            assertTrue(exception.getMessage().contains("findTenant.sql"), exception.getMessage());
            assertTrue(exception.getMessage().contains("createdAt"), exception.getMessage());
            assertTrue(exception.getMessage().contains("created_at"), exception.getMessage());
        }

        @Test
        @DisplayName("names a selected column nothing claims")
        void columnWithoutComponent() {
            writeTenant();
            final var exception = assertThrows(UnmappedColumnsException.class, () ->
                    generator().generateConverterClasses(List.of(statement("findTenant", DOMAIN + ".Tenant",
                            "select id, slug, created_at, language from tenant"))).toList());
            assertTrue(exception.getMessage().contains("language"), exception.getMessage());
            assertTrue(exception.getMessage().contains("No component claims"), exception.getMessage());
        }

        @Test
        @DisplayName("checks a nested record's columns as well")
        void nestedComponentWithoutColumn() {
            writeLedger();
            final var exception = assertThrows(UnmappedColumnsException.class, () ->
                    generator().generateConverterClasses(List.of(statement("findEntries", DOMAIN + ".LedgerEntry",
                            "select id, currency, reason, created_at as at from ledger"))).toList());
            assertTrue(exception.getMessage().contains("amount.minorUnits"), exception.getMessage());
        }

        @Test
        @DisplayName("says nothing about a select list it cannot enumerate")
        void starSelectSkipsTheCheck() {
            writeTenant();
            final var code = generateOne(List.of(statement("findTenant", DOMAIN + ".Tenant",
                    "select * from tenant where id = ?")));
            assertTrue(code.contains("getString(\"slug\")"), code);
        }

        @Test
        @DisplayName("reports a column left over when two components want the same one")
        void twoComponentsOneColumn() {
            // Nesting adds no prefix, so an `id` inside a nested record reads the same column as an
            // `id` beside it. Both are supplied, whatever else the query selects is not claimed,
            // and that is what surfaces.
            write("Inner", """
                    package com.example.domain;

                    public record Inner(long id) {
                    }
                    """);
            write("Outer", """
                    package com.example.domain;

                    public record Outer(long id, Inner inner) {
                    }
                    """);
            final var exception = assertThrows(UnmappedColumnsException.class, () ->
                    generator().generateConverterClasses(List.of(statement("findOuter", DOMAIN + ".Outer",
                            "select id, id as inner_id from outer"))).toList());
            assertTrue(exception.getMessage().contains("inner_id"), exception.getMessage());
        }

        @Test
        @DisplayName("names the path it looked in when the record has no source")
        void missingSource() {
            final var exception = assertThrows(MissingRecordSourceException.class, () ->
                    generator().generateConverterClasses(List.of(statement("findTenant", DOMAIN + ".Tenant",
                            "select id from tenant"))).toList());
            assertTrue(exception.getMessage().contains("findTenant"), exception.getMessage());
            assertTrue(exception.getMessage().contains("com.example.domain.Tenant"), exception.getMessage());
            assertTrue(exception.getMessage().contains("Tenant.java"), exception.getMessage());
            assertTrue(exception.getMessage().contains("sourceDirectory"), exception.getMessage());
        }

        @Test
        @DisplayName("refuses a result row type that is not a record")
        void notARecord() {
            write("Tenant", """
                    package com.example.domain;

                    public class Tenant {
                        private String slug;
                    }
                    """);
            final var exception = assertThrows(UnparsableRecordException.class, () ->
                    generator().generateConverterClasses(List.of(statement("findTenant", DOMAIN + ".Tenant",
                            "select slug from tenant"))).toList());
            assertTrue(exception.getMessage().contains("not a record"), exception.getMessage());
        }

        @Test
        @DisplayName("refuses a component whose type nothing can read")
        void unsupportedComponent() {
            write("Address", """
                    package com.example.domain;

                    public class Address {
                    }
                    """);
            write("Tenant", """
                    package com.example.domain;

                    public record Tenant(String slug, Address address) {
                    }
                    """);
            final var exception = assertThrows(UnsupportedComponentTypeException.class, () ->
                    generator().generateConverterClasses(List.of(statement("findTenant", DOMAIN + ".Tenant",
                            "select slug, address from tenant"))).toList());
            assertTrue(exception.getMessage().contains("address"), exception.getMessage());
            assertTrue(exception.getMessage().contains("com.example.domain.Address"), exception.getMessage());
        }

        @Test
        @DisplayName("refuses two records that would share one converter name")
        void collidingConverterNames() {
            write("Money", """
                    package com.example.domain;

                    public record Money(long minorUnits) {
                    }
                    """);
            final var directory = sources.resolve("com/example/billing");
            try {
                Files.createDirectories(directory);
                Files.writeString(directory.resolve("Money.java"), """
                        package com.example.billing;

                        public record Money(long minorUnits) {
                        }
                        """);
            } catch (final IOException exception) {
                throw new UncheckedIOException(exception);
            }
            final var exception = assertThrows(DuplicateConverterNameException.class, () ->
                    generator().generateConverterClasses(List.of(
                            statement("findOne", DOMAIN + ".Money", "select minor_units from a"),
                            statement("findTwo", "com.example.billing.Money", "select minor_units from b")
                    )).toList());
            assertTrue(exception.getMessage().contains("ToMoneyConverter"), exception.getMessage());
            assertTrue(exception.getMessage().contains("com.example.domain.Money"), exception.getMessage());
            assertTrue(exception.getMessage().contains("com.example.billing.Money"), exception.getMessage());
        }

        @Test
        @DisplayName("refuses a record that contains itself")
        void selfReferentialRecord() {
            write("Node", """
                    package com.example.domain;

                    public record Node(long id, Node parent) {
                    }
                    """);
            final var exception = assertThrows(RecursiveRecordException.class, () ->
                    generator().generateConverterClasses(List.of(statement("findNode", DOMAIN + ".Node",
                            "select id from node"))).toList());
            assertTrue(exception.getMessage().contains("com.example.domain.Node"), exception.getMessage());
            assertTrue(exception.getMessage().contains("parent"), exception.getMessage());
        }

    }

}
