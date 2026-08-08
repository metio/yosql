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
        @DisplayName("a quoted identifier is known by the name inside the quotes")
        void shouldReadQuotedIdentifiers() {
            final var catalog = read("""
                    create table "user" (
                        "firstName" varchar(64) not null,
                        id          bigint      primary key
                    )
                    """);

            final var table = catalog.table("user").orElseThrow();
            assertAll(
                    () -> assertIterableEquals(List.of("firstname", "id"), table.columnNames()),
                    () -> assertTrue(table.column("firstName").isPresent(),
                            "a query spelling it bare reads the same column"),
                    () -> assertTrue(table.column("\"firstName\"").isPresent(),
                            "as does a query quoting it"),
                    () -> assertEquals("firstName", table.column("firstName").orElseThrow().name(),
                            "the reported name is the identifier, which is what a record component is named after"));
        }

        @Test
        @DisplayName("a backticked table is known the same way, so mysqldump DDL is not skipped")
        void shouldReadBacktickedIdentifiers() {
            final var catalog = read("create table `orders` (`id` bigint not null, `total` int)");

            final var orders = catalog.table("orders").orElseThrow();
            assertAll(
                    () -> assertIterableEquals(List.of("id", "total"), orders.columnNames()),
                    () -> assertTrue(orders.column("total").isPresent()));
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
        @DisplayName("a comment mentioning not null does not make a column primitive")
        void shouldNotReadConstraintsOutOfAComment() {
            final var catalog = read(
                    "create table tenant (slug varchar(64) comment 'left not null on purpose')");

            assertTrue(catalog.table("tenant").orElseThrow().column("slug").orElseThrow().nullable(),
                    "reading it as NOT NULL makes the component a primitive, and a NULL row then "
                            + "arrives as a silent zero — the failure this class exists to prevent");
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

        @Test
        @DisplayName("a dropped column is gone")
        void shouldApplyDropColumn() {
            final var catalog = read(
                    "create table tenant (id uuid not null primary key, slug varchar(64))",
                    "alter table tenant drop column slug");

            final var tenant = catalog.table("tenant").orElseThrow();
            assertAll(
                    () -> assertIterableEquals(List.of("id"), tenant.columnNames()),
                    () -> assertTrue(tenant.column("slug").isEmpty()));
        }

        @Test
        @DisplayName("a renamed column is known by its new name and not its old one")
        void shouldApplyRenameColumn() {
            final var catalog = read(
                    "create table tenant (id uuid not null primary key, slug varchar(64))",
                    "alter table tenant rename column slug to handle");

            final var tenant = catalog.table("tenant").orElseThrow();
            assertAll(
                    () -> assertIterableEquals(List.of("id", "handle"), tenant.columnNames()),
                    () -> assertTrue(tenant.column("slug").isEmpty()),
                    () -> assertEquals("varchar(64)", tenant.column("handle").orElseThrow().sqlType()));
        }

        @Test
        @DisplayName("set not null lands, and keeps the type it was declared with")
        void shouldApplySetNotNull() {
            final var catalog = read(
                    "create table tenant (id uuid not null primary key, slug varchar(64))",
                    "alter table tenant alter column slug set not null");

            final var slug = catalog.table("tenant").orElseThrow().column("slug").orElseThrow();
            assertAll(
                    () -> assertFalse(slug.nullable()),
                    () -> assertEquals("varchar(64)", slug.sqlType()));
        }

        @Test
        @DisplayName("drop not null lands too")
        void shouldApplyDropNotNull() {
            final var catalog = read(
                    "create table tenant (id uuid not null primary key, slug varchar(64) not null)",
                    "alter table tenant alter column slug drop not null");

            assertTrue(catalog.table("tenant").orElseThrow().column("slug").orElseThrow().nullable());
        }

        @Test
        @DisplayName("a new type lands, and keeps the nullability it was declared with")
        void shouldApplyAlterColumnType() {
            final var catalog = read(
                    "create table tenant (id uuid not null primary key, counter int not null)",
                    "alter table tenant alter column counter type bigint");

            final var counter = catalog.table("tenant").orElseThrow().column("counter").orElseThrow();
            assertAll(
                    () -> assertEquals("bigint", counter.sqlType()),
                    () -> assertFalse(counter.nullable()));
        }

        @Test
        @DisplayName("MySQL's modify restates a column in full")
        void shouldApplyModify() {
            final var catalog = read(
                    "create table tenant (id uuid not null primary key, counter int)",
                    "alter table tenant modify counter bigint not null");

            final var counter = catalog.table("tenant").orElseThrow().column("counter").orElseThrow();
            assertAll(
                    () -> assertEquals("bigint", counter.sqlType()),
                    () -> assertFalse(counter.nullable()));
        }

        @Test
        @DisplayName("MySQL's change renames and restates at once")
        void shouldApplyChange() {
            final var catalog = read(
                    "create table tenant (id uuid not null primary key, counter int)",
                    "alter table tenant change counter total bigint");

            final var tenant = catalog.table("tenant").orElseThrow();
            assertAll(
                    () -> assertTrue(tenant.column("counter").isEmpty()),
                    () -> assertEquals("bigint", tenant.column("total").orElseThrow().sqlType()));
        }

        @Test
        @DisplayName("a constraint says nothing about the columns and leaves the table alone")
        void shouldIgnoreConstraints() {
            final var catalog = read(
                    "create table tenant (id uuid not null primary key, slug varchar(64))",
                    "alter table tenant add constraint tenant_slug unique (slug)",
                    "alter table tenant drop constraint tenant_slug");

            assertIterableEquals(List.of("id", "slug"), catalog.table("tenant").orElseThrow().columnNames());
        }

        @Test
        @DisplayName("a renamed table takes what was known about the old name with it")
        void shouldForgetARenamedTable() {
            final var catalog = read(
                    "create table tenant (id uuid not null primary key)",
                    "alter table tenant rename to account");

            assertAll(
                    () -> assertTrue(catalog.table("tenant").isEmpty(),
                            "the old name describes nothing now"),
                    () -> assertTrue(catalog.table("account").isEmpty(),
                            "and the new one was never described"));
        }

    }

    @Nested
    @DisplayName("says what it passed over")
    class Unfollowed {

        @Test
        @DisplayName("an alter reaching a table no create table declared")
        void shouldRecordAnAlterWithoutItsTable() {
            final var catalog = CatalogReader.read(List.of(
                    "create table tenant (id uuid not null primary key)",
                    "alter table missing_table add column whatever text"));

            assertAll(
                    () -> assertTrue(catalog.unfollowedFor("tenant").isEmpty(),
                            "tenant was read exactly as its DDL says"),
                    () -> assertEquals(1, catalog.unfollowedFor("missing_table").size()),
                    () -> assertTrue(catalog.unfollowedFor("missing_table").getFirst()
                            .contains("alter table missing_table"),
                            () -> catalog.unfollowedFor("missing_table").toString()));
        }

        @Test
        @DisplayName("an alter that takes its table out of the catalog says which one did it")
        void shouldRecordAnAlterThatDropsTheTable() {
            final var catalog = CatalogReader.read(List.of(
                    "create table tenant (id uuid not null primary key, slug varchar(64))",
                    "alter table tenant rename to renter"));

            assertAll(
                    () -> assertTrue(catalog.table("tenant").isEmpty(), "the table is gone, as designed"),
                    () -> assertEquals(1, catalog.unfollowedFor("tenant").size()),
                    () -> assertTrue(catalog.unfollowedFor("tenant").getFirst().contains("rename to renter"),
                            () -> catalog.unfollowedFor("tenant").toString()));
        }

        @Test
        @DisplayName("a schema it read whole has nothing to report")
        void shouldRecordNothingForAReadableSchema() {
            final var catalog = CatalogReader.read(List.of(
                    "create table tenant (id uuid not null primary key)",
                    "alter table tenant add column slug varchar(64) not null",
                    "comment on column tenant.slug is 'the short name'"));

            assertAll(
                    () -> assertTrue(catalog.unfollowedFor("tenant").isEmpty(),
                            () -> catalog.unfollowedFor("tenant").toString()),
                    () -> assertTrue(catalog.table("tenant").orElseThrow().column("slug").isPresent()));
        }

    }

}
