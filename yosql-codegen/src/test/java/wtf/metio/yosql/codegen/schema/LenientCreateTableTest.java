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
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("a create table the parser refused")
class LenientCreateTableTest {

    @Nested
    @DisplayName("is read structurally rather than lost")
    class ReadsWhatTheParserWillNot {

        /**
         * Ordinary PostgreSQL that JSqlParser 5.3 stops at. {@code cascade} is the only column-level
         * referential action it accepts, so this shape costs a project whole tables at a time.
         */
        @Test
        @DisplayName("a column-level referential action other than cascade")
        void shouldReadPastAReferentialAction() {
            final var catalog = CatalogReader.read(List.of("""
                    create table tenant_member (
                        tenant_id  uuid not null,
                        invited_by uuid references account (id) on delete set null,
                        joined_at  timestamptz not null
                    )"""));

            final var table = catalog.table("tenant_member").orElseThrow(
                    () -> new AssertionError("one clause of one column cost the whole table"));
            assertAll(
                    () -> assertEquals(List.of("tenant_id", "invited_by", "joined_at"),
                            List.copyOf(table.columnNames())),
                    () -> assertEquals("uuid", table.column("invited_by").orElseThrow().sqlType()),
                    () -> assertTrue(table.column("invited_by").orElseThrow().nullable()),
                    () -> assertTrue(catalog.unparsedMentioning("tenant_member").isEmpty(),
                            "it was read, so there is nothing to report as passed over"));
        }

        @Test
        @DisplayName("on delete restrict, and on update too")
        void shouldReadPastEveryReferentialAction() {
            assertAll(
                    () -> assertTrue(CatalogReader.read(List.of(
                            "create table t (a uuid not null, b uuid references x (id) on delete restrict)"))
                            .table("t").isPresent()),
                    () -> assertTrue(CatalogReader.read(List.of(
                            "create table t (a uuid not null, b uuid references x (id) on update set null)"))
                            .table("t").isPresent()));
        }

        @Test
        @DisplayName("a type of several words, a size holding a comma, and a check holding literals")
        void shouldReadTheAwkwardParts() {
            final var table = CatalogReader.read(List.of("""
                    create table tenant_member (
                        role      text not null check (role in ('owner', 'admin', 'viewer')),
                        -- when they joined
                        joined_at timestamp with time zone not null,
                        share     numeric(6, 5) references x (id) on delete set null,
                        primary key (role, joined_at)
                    )""")).table("tenant_member").orElseThrow();

            assertAll(
                    () -> assertEquals(List.of("role", "joined_at", "share"), List.copyOf(table.columnNames())),
                    () -> assertEquals("timestamp with time zone",
                            table.column("joined_at").orElseThrow().sqlType()),
                    () -> assertEquals("numeric", table.column("share").orElseThrow().baseType()),
                    () -> assertTrue(table.column("share").orElseThrow().nullable()));
        }

        @Test
        @DisplayName("a table-level primary key still decides nullability")
        void shouldApplyATableLevelPrimaryKey() {
            final var table = CatalogReader.read(List.of("""
                    create table tenant_member (
                        tenant_id  uuid,
                        invited_by uuid references account (id) on delete set null,
                        primary key (tenant_id)
                    )""")).table("tenant_member").orElseThrow();

            assertAll(
                    () -> assertTrue(table.column("tenant_id").orElseThrow().nullable() == false,
                            "a primary key cannot hold a null"),
                    () -> assertTrue(table.column("invited_by").orElseThrow().nullable()));
        }

    }

    @Nested
    @DisplayName("refuses rather than guesses")
    class Refuses {

        /**
         * A table missing one column is worse than a table nobody has: the column that went missing
         * is reported as a mistake in every statement selecting it.
         */
        @Test
        @DisplayName("an item it cannot read as a column leaves the whole table unread")
        void shouldRefuseAnUnreadableItem() {
            assertTrue(CatalogReader.read(List.of(
                    "create table t (a uuid references x (id) on delete set null, 'not a column')"))
                    .table("t").isEmpty());
        }

        @Test
        @DisplayName("a body it cannot find the end of")
        void shouldRefuseAnUnbalancedBody() {
            assertTrue(CatalogReader.read(List.of("create table t (a uuid on delete set null"))
                    .table("t").isEmpty());
        }

        @Test
        @DisplayName("anything that is not a create table")
        void shouldRefuseOtherStatements() {
            final var catalog = CatalogReader.read(List.of("insert into t (a) values ('unclosed"));

            assertAll(
                    () -> assertTrue(catalog.tableNames().isEmpty()),
                    () -> assertEquals(1, catalog.unparsedMentioning("t").size(),
                            "and it is still reported as something the reader passed over"));
        }

    }

    @Nested
    @DisplayName("agrees with the parser wherever both can read")
    class Agrees {

        private static void shouldAgree(final String parseable, final String unparsable) {
            final var parsed = CatalogReader.read(List.of(parseable)).table("t").orElseThrow();
            final var lenient = CatalogReader.read(List.of(unparsable)).table("t").orElseThrow();

            assertAll(
                    () -> assertEquals(List.copyOf(parsed.columnNames()), List.copyOf(lenient.columnNames())),
                    () -> parsed.columns().forEach((name, column) -> assertAll(
                            () -> assertEquals(column.baseType(),
                                    lenient.column(name).orElseThrow().baseType(), name),
                            () -> assertEquals(column.nullable(),
                                    lenient.column(name).orElseThrow().nullable(), name))));
        }

        @Test
        @DisplayName("the same table, with the one clause the parser cannot take")
        void shouldReadTheSameColumns() {
            shouldAgree(
                    """
                            create table t (
                                id        uuid not null primary key,
                                owner     uuid references x (id) on delete cascade,
                                label     varchar(64) not null,
                                amount    numeric(6, 5),
                                happened  timestamp with time zone not null
                            )""",
                    """
                            create table t (
                                id        uuid not null primary key,
                                owner     uuid references x (id) on delete set null,
                                label     varchar(64) not null,
                                amount    numeric(6, 5),
                                happened  timestamp with time zone not null
                            )""");
        }

    }

}
