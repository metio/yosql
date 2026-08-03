/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import wtf.metio.yosql.codegen.exceptions.UntypedParameterException;
import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.codegen.orchestration.OrchestrationObjectMother;
import wtf.metio.yosql.codegen.records.JavaSourceParser;
import wtf.metio.yosql.codegen.records.RecordScanner;
import wtf.metio.yosql.codegen.exceptions.ConflictingColumnTypeException;
import wtf.metio.yosql.codegen.schema.Schemas;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DefaultMethodParameterConfigurer")
class DefaultMethodParameterConfigurerTest {

    private static final String DOMAIN = "com.example.domain";
    /** These tests are about the front matter and the record, not about reading the statement. */
    private static final String NO_SQL = "";

    private static final Path SOURCE = Path.of("src", "main", "yosql", "tenant", "findTenant.sql");

    @TempDir
    Path sources;

    private DefaultMethodParameterConfigurer configurer;

    @BeforeEach
    void setUp() {
        configurer = new DefaultMethodParameterConfigurer(
                LoggingObjectMother.logger(),
                OrchestrationObjectMother.executionErrors(),
                LoggingObjectMother.messages(),
                new RecordScanner(
                        FilesConfiguration.builder().setSourceDirectory(sources).build(),
                        new JavaSourceParser()),
                Schemas.empty());
    }

    private void write(final String simpleName, final String body) {
        try {
            final var directory = sources.resolve(DOMAIN.replace('.', '/'));
            Files.createDirectories(directory);
            Files.writeString(directory.resolve(simpleName + ".java"), body);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private static Map<String, List<Integer>> indices(final String... names) {
        final var indices = new LinkedHashMap<String, List<Integer>>();
        for (var index = 0; index < names.length; index++) {
            indices.put(names[index], List.of(index + 1));
        }
        return indices;
    }

    private static SqlConfiguration statement(final SqlParameter... parameters) {
        return SqlConfiguration.builder()
                .setName("findTenant")
                .addParameters(parameters)
                .build();
    }

    private static SqlParameter parameter(final String name, final String type) {
        return SqlParameter.builder().setName(name).setType(type).build();
    }

    private static SqlParameter untyped(final String name) {
        return SqlParameter.builder().setName(name).build();
    }

    @Nested
    @DisplayName("takes the type the front matter names")
    class Declared {

        @Test
        void shouldKeepDeclaredTypes() {
            final var configured = configurer.configureParameters(
                    statement(parameter("id", "java.util.UUID"), parameter("slug", "java.lang.String")), SOURCE, NO_SQL, indices("id", "slug"));

            assertAll(
                    () -> assertEquals(2, configured.parameters().size()),
                    () -> assertEquals("java.util.UUID", configured.parameters().get(0).type().orElseThrow()),
                    () -> assertEquals("java.lang.String", configured.parameters().get(1).type().orElseThrow()));
        }

        @Test
        @DisplayName("the indices a parameter is bound at come from the statement")
        void shouldAssignIndices() {
            final var configured = configurer.configureParameters(
                    statement(parameter("id", "java.util.UUID")), SOURCE, NO_SQL, indices("id"));

            assertAll(
                    () -> assertTrue(configured.parameters().get(0).hasIndices()),
                    () -> assertEquals(1, configured.parameters().get(0).indices().orElseThrow()[0]));
        }

    }

    @Nested
    @DisplayName("takes the type of the matching component of the result row type")
    class Inferred {

        @Test
        void shouldInferFromRecordComponent() {
            write("Tenant", """
                    package com.example.domain;

                    import java.util.UUID;

                    public record Tenant(UUID id, String slug) {
                    }
                    """);

            final var configured = configurer.configureParameters(
                    SqlConfiguration.copyOf(statement()).withResultRowType(DOMAIN + ".Tenant"), SOURCE, NO_SQL, indices("id"));

            assertAll(
                    () -> assertEquals(1, configured.parameters().size()),
                    () -> assertEquals("id", configured.parameters().get(0).name().orElseThrow()),
                    () -> assertEquals("java.util.UUID", configured.parameters().get(0).type().orElseThrow()));
        }

        @Test
        @DisplayName("a parameter named in the front matter without a type is inferred as well")
        void shouldInferForDeclaredParameterWithoutType() {
            write("Tenant", """
                    package com.example.domain;

                    import java.util.UUID;

                    public record Tenant(UUID id, String slug) {
                    }
                    """);

            final var configured = configurer.configureParameters(
                    SqlConfiguration.copyOf(statement(untyped("slug"))).withResultRowType(DOMAIN + ".Tenant"), SOURCE, NO_SQL, indices("slug"));

            assertEquals("java.lang.String", configured.parameters().get(0).type().orElseThrow());
        }

        @Test
        @DisplayName("what the front matter names wins over the component of the same name")
        void shouldPreferDeclaredTypeOverComponent() {
            write("Tenant", """
                    package com.example.domain;

                    import java.util.UUID;

                    public record Tenant(UUID id, String slug) {
                    }
                    """);

            final var configured = configurer.configureParameters(
                    SqlConfiguration.copyOf(statement(parameter("id", "java.lang.String")))
                            .withResultRowType(DOMAIN + ".Tenant"), SOURCE, NO_SQL, indices("id"));

            assertEquals("java.lang.String", configured.parameters().get(0).type().orElseThrow());
        }

        @Test
        @DisplayName("a component of a nested record is not a candidate for a plain parameter name")
        void shouldNotDescendIntoNestedRecords() {
            write("Money", """
                    package com.example.domain;

                    public record Money(long minorUnits, String currency) {
                    }
                    """);
            write("Entry", """
                    package com.example.domain;

                    public record Entry(long id, Money amount) {
                    }
                    """);

            final var exception = assertThrows(UntypedParameterException.class,
                    () -> configurer.configureParameters(
                            SqlConfiguration.copyOf(statement()).withResultRowType(DOMAIN + ".Entry"), SOURCE, NO_SQL, indices("minorUnits")));

            assertTrue(exception.getMessage().contains("minorUnits"));
        }

    }

    @Nested
    @DisplayName("takes the type of the column the parameter is named after")
    class FromSchema {

        private DefaultMethodParameterConfigurer withSchema(final String... ddl) {
            return new DefaultMethodParameterConfigurer(
                    LoggingObjectMother.logger(),
                    OrchestrationObjectMother.executionErrors(),
                    LoggingObjectMother.messages(),
                    new RecordScanner(
                            FilesConfiguration.builder().setSourceDirectory(sources).build(),
                            new JavaSourceParser()),
                    Schemas.of(java.util.Arrays.stream(ddl)
                            .map(sql -> new Schemas.VendorStatement(java.util.Optional.empty(), sql))
                            .toList()));
        }

        private DefaultMethodParameterConfigurer withVendorSchemas(
                final String postgresDdl, final String mysqlDdl) {
            return new DefaultMethodParameterConfigurer(
                    LoggingObjectMother.logger(),
                    OrchestrationObjectMother.executionErrors(),
                    LoggingObjectMother.messages(),
                    new RecordScanner(
                            FilesConfiguration.builder().setSourceDirectory(sources).build(),
                            new JavaSourceParser()),
                    Schemas.of(List.of(
                            new Schemas.VendorStatement(java.util.Optional.of("PostgreSQL"), postgresDdl),
                            new Schemas.VendorStatement(java.util.Optional.of("MySQL"), mysqlDdl))));
        }

        @Test
        @DisplayName("a write statement needs no parameters block at all")
        void shouldTypeAWriteFromTheSchema() {
            final var configured = withSchema("""
                    create table tenant (id uuid not null primary key, slug varchar(64) not null)""")
                    .configureParameters(statement(), SOURCE,
                            "insert into tenant (id, slug) values (:id, :slug)", indices("id", "slug"));

            assertAll(
                    () -> assertEquals("java.util.UUID", configured.parameters().get(0).type().orElseThrow()),
                    () -> assertEquals("java.lang.String", configured.parameters().get(1).type().orElseThrow()));
        }

        @Test
        @DisplayName("a nullable column gives a type that can hold a null")
        void shouldBoxNullableColumns() {
            final var configured = withSchema("create table tenant (id uuid not null, rank bigint)")
                    .configureParameters(statement(), SOURCE,
                            "update tenant set rank = :rank where id = :id", indices("rank", "id"));

            assertEquals("java.lang.Long", configured.parameters().get(0).type().orElseThrow());
        }

        @Test
        @DisplayName("what the front matter names still wins")
        void shouldPreferTheDeclaredType() {
            final var configured = withSchema("create table tenant (id uuid not null primary key)")
                    .configureParameters(statement(parameter("id", "com.example.domain.TenantId")), SOURCE,
                            "select id from tenant where id = :id", indices("id"));

            assertEquals("com.example.domain.TenantId", configured.parameters().get(0).type().orElseThrow());
        }

        @Test
        @DisplayName("dialects that spell a type differently still agree, so nothing is reported")
        void shouldAcceptDialectsThatMeetInJava() {
            final var configured = withVendorSchemas(
                    "create table tenant (id bigserial primary key)",
                    "create table tenant (id bigint not null)")
                    .configureParameters(statement(), SOURCE,
                            "select id from tenant where id = :id", indices("id"));

            assertEquals("long", configured.parameters().get(0).type().orElseThrow());
        }

        @Test
        @DisplayName("dialects that genuinely disagree cannot both be the one signature")
        void shouldReportADisagreementAcrossVendors() {
            final var exception = assertThrows(ConflictingColumnTypeException.class, () ->
                    withVendorSchemas(
                            "create table tenant (id uuid not null primary key)",
                            "create table tenant (id varchar(36) not null primary key)")
                            .configureParameters(statement(), SOURCE,
                                    "select id from tenant where id = :id", indices("id")));

            assertAll(
                    () -> assertTrue(exception.getMessage().contains("java.util.UUID")),
                    () -> assertTrue(exception.getMessage().contains("java.lang.String")),
                    () -> assertTrue(exception.getMessage().contains("front matter")));
        }

        @Test
        @DisplayName("a statement naming a vendor answers to that vendor alone")
        void shouldNotReportADisagreementForAVendorStatement() {
            final var forPostgres = SqlConfiguration.copyOf(statement()).withVendor("PostgreSQL");

            final var configured = withVendorSchemas(
                    "create table tenant (id uuid not null primary key)",
                    "create table tenant (id varchar(36) not null primary key)")
                    .configureParameters(forPostgres, SOURCE,
                            "select id from tenant where id = :id", indices("id"));

            assertEquals("java.util.UUID", configured.parameters().get(0).type().orElseThrow());
        }

    }

    @Nested
    @DisplayName("fails rather than binding a parameter as Object")
    class Untyped {

        @Test
        void shouldFailForUndeclaredParameter() {
            final var exception = assertThrows(UntypedParameterException.class,
                    () -> configurer.configureParameters(statement(), SOURCE, NO_SQL, indices("id")));

            assertAll(
                    () -> assertTrue(exception.getMessage().contains("findTenant")),
                    () -> assertTrue(exception.getMessage().contains(SOURCE.toString())),
                    () -> assertTrue(exception.getMessage().contains("id")));
        }

        @Test
        @DisplayName("every parameter without a type is named at once")
        void shouldNameEveryUntypedParameter() {
            final var exception = assertThrows(UntypedParameterException.class,
                    () -> configurer.configureParameters(statement(), SOURCE, NO_SQL, indices("id", "slug", "accountId")));

            assertAll(
                    () -> assertTrue(exception.getMessage().contains("id")),
                    () -> assertTrue(exception.getMessage().contains("slug")),
                    () -> assertTrue(exception.getMessage().contains("accountId")));
        }

        @Test
        @DisplayName("a result row type that is not a record settles nothing")
        void shouldFailWhenResultRowTypeIsNotARecord() {
            write("Tenant", """
                    package com.example.domain;

                    public class Tenant {
                    }
                    """);

            assertThrows(UntypedParameterException.class,
                    () -> configurer.configureParameters(
                            SqlConfiguration.copyOf(statement()).withResultRowType(DOMAIN + ".Tenant"), SOURCE, NO_SQL, indices("id")));
        }

        @Test
        @DisplayName("a statement binding nothing needs no types")
        void shouldAcceptStatementWithoutParameters() {
            final var configured = configurer.configureParameters(statement(), SOURCE, NO_SQL, Map.of());

            assertTrue(configured.parameters().isEmpty());
        }

    }

}
