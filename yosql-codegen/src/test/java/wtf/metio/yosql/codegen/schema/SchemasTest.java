/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import com.palantir.javapoet.TypeName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.schema.Schemas.VendorStatement;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Schemas")
class SchemasTest {

    private static VendorStatement shared(final String sql) {
        return new VendorStatement(Optional.empty(), sql);
    }

    private static VendorStatement forVendor(final String vendor, final String sql) {
        return new VendorStatement(Optional.of(vendor), sql);
    }

    private static String javaTypeOf(final Catalog catalog, final String table, final String column,
            final String vendor) {
        return SqlTypes.javaType(catalog.table(table).orElseThrow().column(column).orElseThrow(),
                Optional.of(vendor)).map(Object::toString).orElse("<unknown>");
    }

    @Nested
    @DisplayName("one database per vendor, not one catalog for all of them")
    class PerVendor {

        @Test
        @DisplayName("the same table declared twice does not overwrite itself")
        void shouldKeepVendorSchemasApart() {
            final var schemas = Schemas.of(List.of(
                    forVendor("PostgreSQL", "create table tenant (id bigserial primary key, slug varchar(64))"),
                    forVendor("MySQL", "create table tenant (id bigint auto_increment primary key, slug varchar(64))")));

            assertAll(
                    () -> assertEquals("bigserial",
                            schemas.forVendor(Optional.of("PostgreSQL")).table("tenant").orElseThrow()
                                    .column("id").orElseThrow().sqlType()),
                    () -> assertEquals("bigint",
                            schemas.forVendor(Optional.of("MySQL")).table("tenant").orElseThrow()
                                    .column("id").orElseThrow().sqlType()));
        }

        @Test
        @DisplayName("dialects that spell a type differently still agree on the Java type")
        void shouldAgreeOnJavaTypeAcrossDialects() {
            final var schemas = Schemas.of(List.of(
                    forVendor("PostgreSQL", "create table tenant (id bigserial primary key)"),
                    forVendor("MySQL", "create table tenant (id bigint not null)")));

            assertAll(
                    () -> assertEquals("long",
                            javaTypeOf(schemas.forVendor(Optional.of("PostgreSQL")), "tenant", "id", "PostgreSQL")),
                    () -> assertEquals("long",
                            javaTypeOf(schemas.forVendor(Optional.of("MySQL")), "tenant", "id", "MySQL")));
        }

        @Test
        @DisplayName("DDL naming no vendor applies to every database")
        void shouldLayerVendorDdlOverShared() {
            final var schemas = Schemas.of(List.of(
                    shared("create table account (id uuid not null primary key)"),
                    forVendor("PostgreSQL", "create table tenant (id bigserial primary key)")));

            final var postgres = schemas.forVendor(Optional.of("PostgreSQL"));
            assertAll(
                    () -> assertTrue(postgres.table("account").isPresent(), "shared table"),
                    () -> assertTrue(postgres.table("tenant").isPresent(), "own table"));
        }

        @Test
        @DisplayName("a vendor with no DDL of its own still sees the shared tables")
        void shouldFallBackToShared() {
            final var schemas = Schemas.of(List.of(shared("create table account (id uuid not null)")));

            assertTrue(schemas.forVendor(Optional.of("Oracle")).table("account").isPresent());
        }

    }

    @Nested
    @DisplayName("which catalogs a statement has to hold against")
    class Applicable {

        @Test
        @DisplayName("a statement naming a vendor answers to that vendor alone")
        void shouldCheckOneVendor() {
            final var schemas = Schemas.of(List.of(
                    forVendor("PostgreSQL", "create table tenant (id bigserial)"),
                    forVendor("MySQL", "create table tenant (id bigint)")));

            assertEquals(1, schemas.applicableTo(Optional.of("PostgreSQL")).size());
        }

        @Test
        @DisplayName("a statement naming no vendor is the fallback, so it answers to all of them")
        void shouldCheckEveryVendor() {
            final var schemas = Schemas.of(List.of(
                    forVendor("PostgreSQL", "create table tenant (id bigserial)"),
                    forVendor("MySQL", "create table tenant (id bigint)")));

            assertEquals(2, schemas.applicableTo(Optional.empty()).size());
        }

        @Test
        @DisplayName("with no vendor-specific DDL there is one catalog to answer to")
        void shouldCheckSharedWhenNoVendorsExist() {
            final var schemas = Schemas.of(List.of(shared("create table tenant (id uuid)")));

            assertEquals(1, schemas.applicableTo(Optional.empty()).size());
        }

    }

    @Test
    @DisplayName("nothing read means nothing to check against")
    void shouldBeEmptyWithoutDdl() {
        assertAll(
                () -> assertTrue(Schemas.empty().isEmpty()),
                () -> assertTrue(Schemas.of(List.of(shared("select id from tenant"))).isEmpty()));
    }

    @Nested
    @DisplayName("a column is read in the spelling its own DDL was written in")
    class Dialect {

        private static Optional<TypeName> typeOf(
                final Schemas schemas,
                final String column,
                final Optional<String> statementVendor) {
            final var catalog = schemas.applicableTo(statementVendor).getFirst();
            return SqlTypes.javaType(
                    catalog.table("attachment").orElseThrow().column(column).orElseThrow(),
                    catalog.dialect(statementVendor));
        }

        private static final String POSTGRES_DDL = """
                create table attachment (
                    id      bigserial primary key,
                    payload bytea not null,
                    at      timestamptz not null,
                    slug    varchar(64) not null
                )""";

        @Test
        @DisplayName("DDL marked with a vendor gives up its spellings to a statement that names none")
        void shouldReadVendorSpellingsWithoutAStatementVendor() {
            final var schemas = Schemas.of(List.of(forVendor("PostgreSQL", POSTGRES_DDL)));

            assertAll(
                    () -> assertEquals(Optional.of("byte[]"),
                            typeOf(schemas, "payload", Optional.empty()).map(Object::toString)),
                    () -> assertEquals(Optional.of("java.time.Instant"),
                            typeOf(schemas, "at", Optional.empty()).map(Object::toString)),
                    () -> assertEquals(Optional.of("long"),
                            typeOf(schemas, "id", Optional.empty()).map(Object::toString)));
        }

        @Test
        @DisplayName("a statement naming the same vendor reads them as it always did")
        void shouldKeepReadingWithAStatementVendor() {
            final var schemas = Schemas.of(List.of(forVendor("PostgreSQL", POSTGRES_DDL)));

            assertEquals(Optional.of("byte[]"),
                    typeOf(schemas, "payload", Optional.of("PostgreSQL")).map(Object::toString));
        }

        @Test
        @DisplayName("the statement's vendor still answers for DDL that names none")
        void shouldFallBackToTheStatementVendor() {
            final var schemas = Schemas.of(List.of(shared("""
                    create table attachment (
                        id bigint not null primary key,
                        at datetime not null
                    )""")));

            assertAll(
                    () -> assertEquals(Optional.of("java.time.LocalDateTime"),
                            typeOf(schemas, "at", Optional.of("MySQL")).map(Object::toString),
                            "datetime is MySQL's, and only the statement says so"),
                    () -> assertEquals(Optional.empty(),
                            typeOf(schemas, "at", Optional.empty()),
                            "with nothing naming a database there is no dialect to read it in"));
        }

        @Test
        @DisplayName("each vendor's catalog answers in its own spelling, so the two can be compared")
        void shouldReadEachCatalogInItsOwnDialect() {
            final var schemas = Schemas.of(List.of(
                    forVendor("PostgreSQL", "create table attachment (id bigserial primary key)"),
                    forVendor("MySQL", "create table attachment (id bigint auto_increment primary key)")));

            // Both catalogs answer, rather than PostgreSQL's dropping out unread and MySQL's
            // deciding alone — which is what makes the agreement between them evidence of anything.
            final var answers = schemas.applicableTo(Optional.empty()).stream()
                    .map(catalog -> SqlTypes.javaType(
                            catalog.table("attachment").orElseThrow().column("id").orElseThrow(),
                            catalog.dialect(Optional.empty())))
                    .toList();

            assertAll(
                    () -> assertEquals(2, answers.size()),
                    () -> assertTrue(answers.stream().allMatch(Optional::isPresent), answers::toString),
                    () -> assertEquals(1, answers.stream().distinct().count(), answers::toString));
        }

    }

}
