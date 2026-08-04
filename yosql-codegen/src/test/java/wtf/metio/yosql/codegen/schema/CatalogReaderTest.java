/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CatalogReader")
class CatalogReaderTest {

    private static Catalog read(final String... statements) {
        return CatalogReader.read(List.of(statements));
    }

    @Nested
    @DisplayName("what it reads")
    class Reads {

        @Test
        void shouldReadColumnsInDeclarationOrder() {
            final var catalog = read("""
                    create table tenant (
                        id         uuid         not null primary key,
                        account_id uuid         not null,
                        slug       varchar(64)  not null,
                        created_at timestamp with time zone not null
                    )
                    """);

            final var tenant = catalog.table("tenant").orElseThrow();
            assertIterableEquals(List.of("id", "account_id", "slug", "created_at"), tenant.columnNames());
        }

        @Test
        @DisplayName("a type keeps the words that belong to it")
        void shouldKeepMultiWordTypes() {
            final var catalog = read("""
                    create table event (
                        at        timestamp with time zone not null,
                        magnitude double precision
                    )
                    """);

            final var event = catalog.table("event").orElseThrow();
            assertAll(
                    () -> assertEquals("timestamp with time zone", event.column("at").orElseThrow().sqlType()),
                    () -> assertEquals("double precision", event.column("magnitude").orElseThrow().sqlType()));
        }

        @Test
        @DisplayName("length and precision are not part of the type")
        void shouldStripPrecision() {
            final var catalog = read("create table t (slug varchar(64), amount numeric(12,2))");

            final var table = catalog.table("t").orElseThrow();
            assertAll(
                    () -> assertEquals("varchar", table.column("slug").orElseThrow().baseType()),
                    () -> assertEquals("numeric", table.column("amount").orElseThrow().baseType()),
                    () -> assertEquals("numeric(12,2)", table.column("amount").orElseThrow().sqlType()));
        }

        @Test
        @DisplayName("identifiers are matched whatever case they were written in")
        void shouldIgnoreCase() {
            final var catalog = read("CREATE TABLE Tenant (Id UUID NOT NULL)");

            assertAll(
                    () -> assertTrue(catalog.table("tenant").isPresent()),
                    () -> assertTrue(catalog.table("TENANT").isPresent()),
                    () -> assertTrue(catalog.table("tenant").orElseThrow().column("ID").isPresent()));
        }

        @Test
        @DisplayName("a schema-qualified table is known by its own name")
        void shouldStripSchemaQualifier() {
            final var catalog = read("create table public.tenant (id uuid not null)");

            assertTrue(catalog.table("tenant").isPresent());
        }

        @Test
        @DisplayName("if not exists does not become part of the name")
        void shouldReadIfNotExists() {
            final var catalog = read("CREATE TABLE IF NOT EXISTS companies (pid BIGINT, name VARCHAR(50))");

            assertAll(
                    () -> assertTrue(catalog.table("companies").isPresent()),
                    () -> assertIterableEquals(List.of("pid", "name"),
                            catalog.table("companies").orElseThrow().columnNames()));
        }

    }

    @Nested
    @DisplayName("nullability")
    class Nullability {

        @Test
        void shouldReadNotNull() {
            final var catalog = read("create table t (a uuid not null, b uuid)");

            final var table = catalog.table("t").orElseThrow();
            assertAll(
                    () -> assertFalse(table.column("a").orElseThrow().nullable()),
                    () -> assertTrue(table.column("b").orElseThrow().nullable()));
        }

        @Test
        @DisplayName("a primary key written beneath the table is not nullable either")
        void shouldTreatTableLevelPrimaryKeyAsNotNullable() {
            final var catalog = read("create table t (id bigint, name varchar(64), primary key (id))");

            final var table = catalog.table("t").orElseThrow();
            assertAll(
                    () -> assertFalse(table.column("id").orElseThrow().nullable(),
                            "the constraint beneath the table says as much as one beside the column"),
                    () -> assertTrue(table.column("name").orElseThrow().nullable()));
        }

        @Test
        @DisplayName("every column of a composite key, which has no other spelling")
        void shouldTreatCompositePrimaryKeyAsNotNullable() {
            final var catalog = read(
                    "create table t (tenant_id uuid, slug varchar(64), payload text, primary key (tenant_id, slug))");

            final var table = catalog.table("t").orElseThrow();
            assertAll(
                    () -> assertFalse(table.column("tenant_id").orElseThrow().nullable()),
                    () -> assertFalse(table.column("slug").orElseThrow().nullable()),
                    () -> assertTrue(table.column("payload").orElseThrow().nullable()));
        }

        @Test
        @DisplayName("a primary key is not nullable, whether or not it says so")
        void shouldTreatPrimaryKeyAsNotNullable() {
            final var catalog = read("create table t (id bigint primary key, other bigint)");

            assertFalse(catalog.table("t").orElseThrow().column("id").orElseThrow().nullable());
        }

    }

    @Nested
    @DisplayName("what it skips rather than guesses")
    class Skips {

        @Test
        @DisplayName("a table constraint declares no column")
        void shouldSkipTableConstraints() {
            final var catalog = read("""
                    create table employees (
                        pid            bigint,
                        department_pid bigint references departments(pid),
                        name           varchar(50),
                        CONSTRAINT employees_pk PRIMARY KEY(pid, department_pid)
                    )
                    """);

            assertIterableEquals(List.of("pid", "department_pid", "name"),
                    catalog.table("employees").orElseThrow().columnNames());
        }

        @Test
        @DisplayName("a comma inside a check constraint does not split a column")
        void shouldNotSplitInsideParentheses() {
            final var catalog = read("""
                    create table t (
                        state varchar(32) check (state in ('DRAFT', 'ACTIVE')),
                        rank  numeric(12,2)
                    )
                    """);

            assertIterableEquals(List.of("state", "rank"), catalog.table("t").orElseThrow().columnNames());
        }

        @Test
        @DisplayName("a table created from a select declares no columns this reader can see")
        void shouldSkipCreateTableAsSelect() {
            final var catalog = read("create table summary as select id, slug from tenant");

            assertTrue(catalog.table("summary").isEmpty());
        }

        @Test
        @DisplayName("a statement that is not DDL contributes nothing")
        void shouldIgnoreQueries() {
            final var catalog = read("select id from tenant where id = ?");

            assertTrue(catalog.isEmpty());
        }

        @Test
        @DisplayName("an unknown table is absent rather than empty, so checks are skipped not failed")
        void shouldLeaveUnknownTablesAbsent() {
            final var catalog = read("create table known (id uuid not null)");

            assertAll(
                    () -> assertTrue(catalog.table("known").isPresent()),
                    () -> assertTrue(catalog.table("unknown").isEmpty()));
        }

    }

    @Nested
    @DisplayName("migrations applied in order")
    class Migrations {

        @Test
        void shouldApplyAlterTableAddColumn() {
            final var catalog = read(
                    "create table tenant (id uuid not null primary key)",
                    "alter table tenant add column slug varchar(64) not null");

            final var tenant = catalog.table("tenant").orElseThrow();
            assertAll(
                    () -> assertIterableEquals(List.of("id", "slug"), tenant.columnNames()),
                    () -> assertFalse(tenant.column("slug").orElseThrow().nullable()));
        }

        @Test
        @DisplayName("the column keyword is optional")
        void shouldApplyAlterTableAddWithoutColumnKeyword() {
            final var catalog = read(
                    "create table tenant (id uuid not null)",
                    "alter table tenant add slug varchar(64)");

            assertTrue(catalog.table("tenant").orElseThrow().column("slug").isPresent());
        }

        @Test
        @DisplayName("altering a table the reader never understood adds nothing")
        void shouldIgnoreAlterOfUnknownTable() {
            final var catalog = read("alter table nowhere add column slug varchar(64)");

            assertTrue(catalog.table("nowhere").isEmpty());
        }

    }

}
