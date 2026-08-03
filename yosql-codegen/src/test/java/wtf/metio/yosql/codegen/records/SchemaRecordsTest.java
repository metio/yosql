/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.palantir.javapoet.ClassName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.blocks.BlocksObjectMother;
import wtf.metio.yosql.codegen.schema.Schemas;
import wtf.metio.yosql.codegen.schema.Schemas.VendorStatement;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("SchemaRecords")
class SchemaRecordsTest {

    private static final String DDL = """
            create table tenant (
                id         uuid not null primary key,
                account_id uuid not null,
                slug       varchar(64) not null,
                nickname   varchar(32),
                created_at timestamp with time zone not null
            )""";

    private static final ClassName TENANT = ClassName.get("com.example.domain", "Tenant");

    private static SchemaRecords records(final String... ddl) {
        return new SchemaRecords(
                Schemas.of(List.of(ddl).stream()
                        .map(sql -> new VendorStatement(Optional.empty(), sql)).toList()),
                BlocksObjectMother.annotationGenerator());
    }

    private static SqlStatement statement(final String sql) {
        return SqlStatement.builder()
                .setSourcePath(Path.of("src", "main", "yosql", "tenant", "findTenant.sql"))
                .setConfiguration(SqlConfiguration.builder().setName("findTenant").build())
                .setRawStatement(sql)
                .build();
    }

    @Nested
    @DisplayName("the shape it works out")
    class Shape {

        @Test
        @DisplayName("one component per selected column, in the order they are selected")
        void shouldFollowTheSelectList() {
            final var shape = records(DDL)
                    .shapeOf(TENANT, statement("select id, slug, created_at from tenant"))
                    .orElseThrow();

            assertAll(
                    () -> assertEquals(3, shape.components().size()),
                    () -> assertEquals("id", shape.components().get(0).name()),
                    () -> assertEquals("java.util.UUID", shape.components().get(0).type().toString()),
                    () -> assertEquals("slug", shape.components().get(1).name()),
                    () -> assertEquals("java.time.Instant", shape.components().get(2).type().toString()));
        }

        @Test
        @DisplayName("a snake_case column becomes a camelCase component")
        void shouldRenameColumns() {
            final var shape = records(DDL)
                    .shapeOf(TENANT, statement("select account_id, created_at from tenant"))
                    .orElseThrow();

            assertAll(
                    () -> assertEquals("accountId", shape.components().get(0).name()),
                    () -> assertEquals("createdAt", shape.components().get(1).name()));
        }

        @Test
        @DisplayName("a nullable column becomes a type that can hold a null")
        void shouldBoxNullableColumns() {
            final var shape = records(DDL)
                    .shapeOf(TENANT, statement("select id, nickname from tenant"))
                    .orElseThrow();

            assertEquals("java.lang.String", shape.components().get(1).type().toString());
        }

        @Test
        @DisplayName("an alias names the component, and the column behind it gives the type")
        void shouldFollowAliases() {
            final var shape = records(DDL)
                    .shapeOf(TENANT, statement("select created_at as at from tenant"))
                    .orElseThrow();

            assertAll(
                    () -> assertEquals("at", shape.components().get(0).name()),
                    () -> assertEquals("java.time.Instant", shape.components().get(0).type().toString()));
        }

        @Test
        @DisplayName("a star selects everything the table declares")
        void shouldExpandAStar() {
            final var shape = records(DDL).shapeOf(TENANT, statement("select * from tenant")).orElseThrow();

            assertEquals(5, shape.components().size());
        }

    }

    @Nested
    @DisplayName("when it will not write one")
    class Refuses {

        @Test
        @DisplayName("a column the catalog does not describe")
        void shouldRefuseUnknownColumns() {
            assertTrue(records(DDL).shapeOf(TENANT, statement("select whatever from some_view")).isEmpty());
        }

        @Test
        @DisplayName("an expression no single column produced")
        void shouldRefuseComputedColumns() {
            assertTrue(records(DDL)
                    .shapeOf(TENANT, statement("select count(*) as total from tenant")).isEmpty());
        }

        @Test
        @DisplayName("a subquery, which can produce anything")
        void shouldRefuseSubqueries() {
            assertTrue(records(DDL)
                    .shapeOf(TENANT, statement("select id from (select id from tenant) x")).isEmpty());
        }

        @Test
        @DisplayName("with no schema at all")
        void shouldRefuseWithoutASchema() {
            assertTrue(records().shapeOf(TENANT, statement("select id from tenant")).isEmpty());
        }

    }

    @Test
    @DisplayName("the record it writes is an ordinary record")
    void shouldWriteARecord() {
        final var shape = records(DDL)
                .shapeOf(TENANT, statement("select id, nickname, created_at from tenant"))
                .orElseThrow();

        final var written = records(DDL).generateRecord(shape).getType().toString();

        assertAll(
                () -> assertTrue(written.contains("record Tenant("), written),
                () -> assertTrue(written.contains("java.util.UUID id"), written),
                () -> assertTrue(written.contains("java.lang.String nickname"), written),
                () -> assertTrue(written.contains("java.time.Instant createdAt"), written));
    }

}
