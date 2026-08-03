/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import wtf.metio.yosql.codegen.exceptions.SchemaMismatchException;
import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.codegen.records.JavaSourceParser;
import wtf.metio.yosql.codegen.records.RecordScanner;
import wtf.metio.yosql.codegen.schema.Schemas.VendorStatement;
import wtf.metio.yosql.models.configuration.SchemaValidation;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.SchemaConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("SchemaValidator")
class SchemaValidatorTest {

    private static final String TENANT_DDL = """
            create table tenant (
                id         uuid not null primary key,
                slug       varchar(64) not null,
                nickname   varchar(32),
                created_at timestamp with time zone not null
            )""";

    @TempDir
    Path sources;

    private SchemaValidator validator(final SchemaValidation mode, final String... ddl) {
        return new SchemaValidator(
                Schemas.of(List.of(ddl).stream().map(s -> new VendorStatement(Optional.empty(), s)).toList()),
                SchemaConfiguration.builder().setValidation(mode).build(),
                new RecordScanner(FilesConfiguration.builder().setSourceDirectory(sources).build(),
                        new JavaSourceParser()),
                LoggingObjectMother.logger());
    }

    private static SqlStatement statement(final String sql) {
        return statement(sql, SqlConfiguration.builder().setName("findTenant").build());
    }

    private static SqlStatement statement(final String sql, final SqlConfiguration configuration) {
        return SqlStatement.builder()
                .setSourcePath(Path.of("src", "main", "yosql", "tenant", "findTenant.sql"))
                .setConfiguration(configuration)
                .setRawStatement(sql)
                .build();
    }

    private void write(final String simpleName, final String body) {
        try {
            final var directory = sources.resolve("com/example/domain");
            Files.createDirectories(directory);
            Files.writeString(directory.resolve(simpleName + ".java"), body);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    @Nested
    @DisplayName("a column no table declares")
    class UnknownColumns {

        @Test
        void shouldFailOnATypo() {
            final var exception = assertThrows(SchemaMismatchException.class, () ->
                    validator(SchemaValidation.ERROR, TENANT_DDL)
                            .validate(List.of(statement("select id, slgu from tenant"))));

            assertAll(
                    () -> assertTrue(exception.getMessage().contains("slgu")),
                    () -> assertTrue(exception.getMessage().contains("findTenant")));
        }

        @Test
        void shouldAcceptColumnsThatExist() {
            assertDoesNotThrow(() -> validator(SchemaValidation.ERROR, TENANT_DDL)
                    .validate(List.of(statement("select id, slug, created_at from tenant"))));
        }

        @Test
        @DisplayName("a star expands, so a record built from one is checked like any other")
        void shouldCheckRecordsAgainstAnExpandedStar() {
            write("Tenant", """
                    package com.example.domain;

                    public record Tenant(java.util.UUID id, String slug, int nickname,
                            java.time.Instant createdAt) {
                    }
                    """);
            final var intoRecord = SqlConfiguration.builder()
                    .setName("findTenant")
                    .setResultRowType("com.example.domain.Tenant")
                    .build();

            final var exception = assertThrows(SchemaMismatchException.class, () ->
                    validator(SchemaValidation.ERROR, TENANT_DDL)
                            .validate(List.of(statement("select * from tenant", intoRecord))));

            assertTrue(exception.getMessage().contains("nullable"), exception.getMessage());
        }

        @Test
        @DisplayName("an alias is a new name for a column, not a claim that the table has one")
        void shouldCheckTheColumnAnItemReadsNotTheNameItGivesIt() {
            assertDoesNotThrow(() -> validator(SchemaValidation.ERROR, TENANT_DDL)
                    .validate(List.of(statement("select slug as handle, created_at as at from tenant"))));
        }

        @Test
        @DisplayName("the column behind an alias is still checked")
        void shouldStillCheckTheSourceOfAnAlias() {
            final var exception = assertThrows(SchemaMismatchException.class, () ->
                    validator(SchemaValidation.ERROR, TENANT_DDL)
                            .validate(List.of(statement("select slgu as handle from tenant"))));

            assertTrue(exception.getMessage().contains("slgu"));
        }

        @Test
        @DisplayName("a component matches the row's name, and is checked against the column behind it")
        void shouldResolveComponentsThroughAliases() {
            write("Tenant", """
                    package com.example.domain;

                    public record Tenant(java.util.UUID id, int at) {
                    }
                    """);
            final var intoRecord = SqlConfiguration.builder()
                    .setName("findTenant")
                    .setResultRowType("com.example.domain.Tenant")
                    .build();

            // `at` is created_at, which is `not null`, so a primitive is fine — but it is a
            // timestamp, and an int cannot hold one.
            final var exception = assertThrows(SchemaMismatchException.class, () ->
                    validator(SchemaValidation.ERROR, TENANT_DDL).validate(List.of(
                            statement("select id, created_at as at from tenant", intoRecord))));

            assertTrue(exception.getMessage().contains("java.time.Instant"), exception.getMessage());
        }

        @Test
        @DisplayName("a qualified column is checked against the table its alias names")
        void shouldResolveAliases() {
            assertThrows(SchemaMismatchException.class, () ->
                    validator(SchemaValidation.ERROR, TENANT_DDL)
                            .validate(List.of(statement("select t.slgu from tenant t"))));
        }

    }

    @Nested
    @DisplayName("what it stays quiet about")
    class Quiet {

        @Test
        @DisplayName("a star over a table the catalog never saw could be anything")
        void shouldSkipStarOverUnknownTables() {
            assertDoesNotThrow(() -> validator(SchemaValidation.ERROR, TENANT_DDL)
                    .validate(List.of(statement("select * from some_view"))));
        }

        @Test
        @DisplayName("a subquery can produce columns the catalog never saw")
        void shouldSkipSubqueries() {
            assertDoesNotThrow(() -> validator(SchemaValidation.ERROR, TENANT_DDL)
                    .validate(List.of(statement("select anything from (select id as anything from tenant) x"))));
        }

        @Test
        @DisplayName("a table the catalog never saw could hold any column")
        void shouldSkipUnknownTables() {
            assertDoesNotThrow(() -> validator(SchemaValidation.ERROR, TENANT_DDL)
                    .validate(List.of(statement("select whatever from some_view"))));
        }

        @Test
        @DisplayName("SQL it cannot parse")
        void shouldSkipUnparsableSql() {
            assertDoesNotThrow(() -> validator(SchemaValidation.ERROR, TENANT_DDL)
                    .validate(List.of(statement("CREATE ALIAS x AS 'not sql at all'"))));
        }

        @Test
        @DisplayName("OFF says nothing at all")
        void shouldSayNothingWhenOff() {
            assertDoesNotThrow(() -> validator(SchemaValidation.OFF, TENANT_DDL)
                    .validate(List.of(statement("select slgu from tenant"))));
        }

        @Test
        @DisplayName("WARN reports without stopping the build")
        void shouldWarnWithoutFailing() {
            assertDoesNotThrow(() -> validator(SchemaValidation.WARN, TENANT_DDL)
                    .validate(List.of(statement("select slgu from tenant"))));
        }

        @Test
        @DisplayName("a statement can opt out")
        void shouldHonourTheEscapeHatch() {
            final var opted = SqlConfiguration.builder().setName("findTenant").setValidateSchema(false).build();

            assertDoesNotThrow(() -> validator(SchemaValidation.ERROR, TENANT_DDL)
                    .validate(List.of(statement("select slgu from tenant", opted))));
        }

        @Test
        @DisplayName("with no DDL read there is nothing to check against")
        void shouldSkipWithoutASchema() {
            assertDoesNotThrow(() -> validator(SchemaValidation.ERROR)
                    .validate(List.of(statement("select slgu from tenant"))));
        }

    }

    @Nested
    @DisplayName("a parameter that disagrees with its column")
    class Parameters {

        private SqlConfiguration withParameter(final String name, final String type) {
            return SqlConfiguration.builder()
                    .setName("findTenant")
                    .addParameters(SqlParameter.builder().setName(name).setType(type).build())
                    .build();
        }

        @Test
        void shouldFailOnADisagreement() {
            final var exception = assertThrows(SchemaMismatchException.class, () ->
                    validator(SchemaValidation.ERROR, TENANT_DDL).validate(List.of(
                            statement("select id from tenant where id = :id",
                                    withParameter("id", "java.lang.Long")))));

            assertTrue(exception.getMessage().contains("java.util.UUID"));
        }

        @Test
        void shouldAcceptTheRightType() {
            assertDoesNotThrow(() -> validator(SchemaValidation.ERROR, TENANT_DDL).validate(List.of(
                    statement("select id from tenant where id = :id",
                            withParameter("id", "java.util.UUID")))));
        }

        @Test
        @DisplayName("a type of your own is what converters are for, so it is left alone")
        void shouldAcceptValueTypes() {
            assertDoesNotThrow(() -> validator(SchemaValidation.ERROR, TENANT_DDL).validate(List.of(
                    statement("select id from tenant where id = :id",
                            withParameter("id", "com.example.domain.TenantId")))));
        }

    }

    @Nested
    @DisplayName("a result row component that cannot hold its column")
    class Components {

        private SqlConfiguration intoRecord() {
            return SqlConfiguration.builder()
                    .setName("findTenant")
                    .setResultRowType("com.example.domain.Tenant")
                    .build();
        }

        @Test
        @DisplayName("a nullable column read into a primitive throws on the first null row")
        void shouldFailOnPrimitiveForNullableColumn() {
            write("Tenant", """
                    package com.example.domain;

                    public record Tenant(java.util.UUID id, int nickname) {
                    }
                    """);

            final var exception = assertThrows(SchemaMismatchException.class, () ->
                    validator(SchemaValidation.ERROR, TENANT_DDL)
                            .validate(List.of(statement("select id, nickname from tenant", intoRecord()))));

            assertTrue(exception.getMessage().contains("nullable"));
        }

        @Test
        void shouldFailOnAMismatchedComponentType() {
            write("Tenant", """
                    package com.example.domain;

                    public record Tenant(java.lang.Long id, String slug) {
                    }
                    """);

            final var exception = assertThrows(SchemaMismatchException.class, () ->
                    validator(SchemaValidation.ERROR, TENANT_DDL)
                            .validate(List.of(statement("select id, slug from tenant", intoRecord()))));

            assertTrue(exception.getMessage().contains("java.util.UUID"));
        }

        @Test
        void shouldAcceptAMatchingRecord() {
            write("Tenant", """
                    package com.example.domain;

                    public record Tenant(java.util.UUID id, String slug) {
                    }
                    """);

            assertDoesNotThrow(() -> validator(SchemaValidation.ERROR, TENANT_DDL)
                    .validate(List.of(statement("select id, slug from tenant", intoRecord()))));
        }

    }

}
