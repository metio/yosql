/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

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

}
