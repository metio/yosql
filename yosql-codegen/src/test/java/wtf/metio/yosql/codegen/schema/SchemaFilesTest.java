/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import com.palantir.javapoet.TypeName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import wtf.metio.yosql.internals.testing.configs.FilesConfigurations;
import wtf.metio.yosql.models.immutables.SchemaConfiguration;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("SchemaFiles")
class SchemaFilesTest {

    private static SchemaFiles reading(final Path directory) {
        return new SchemaFiles(
                FilesConfigurations.defaults(),
                SchemaConfiguration.builder()
                        .setSqlStatementsDirectory(directory.toAbsolutePath().toString())
                        .build());
    }

    private static void write(final Path directory, final String name, final String sql) {
        try {
            Files.writeString(directory.resolve(name), sql);
        } catch (final IOException cause) {
            throw new UncheckedIOException(cause);
        }
    }

    private static List<String> sqlOf(final Path directory) {
        return reading(directory).read().stream().map(Schemas.VendorStatement::sql).toList();
    }

    @Nested
    @DisplayName("reads a directory of migrations in the order they applied")
    class MigrationOrder {

        /**
         * The shape a Flyway project reaches on its tenth migration, and the one where name order
         * stops agreeing with version order: {@code V10__} and {@code V12__} sort in front of
         * {@code V2__} as text.
         */
        private void writeMigrations(final Path directory) {
            write(directory, "V1__create_account.sql", """
                    create table account (
                        id   uuid not null primary key,
                        name varchar(64) not null
                    );""");
            write(directory, "V2__create_tenant.sql", """
                    create table tenant (
                        id         uuid not null primary key,
                        account_id uuid not null
                    );""");
            write(directory, "V10__add_nickname.sql",
                    "alter table tenant add column nickname varchar(32);");
            write(directory, "V12__add_time_zone.sql",
                    "alter table tenant add column time_zone varchar(64) not null;");
        }

        @Test
        @DisplayName("a column a later migration adds to an earlier one's table is in the schema")
        void shouldApplyMigrationsInVersionOrder(@TempDir final Path directory) {
            writeMigrations(directory);

            final var tenant = Schemas.of(reading(directory).read()).forVendor(Optional.empty())
                    .table("tenant")
                    .orElseThrow(() -> new AssertionError("tenant is not in the catalog at all"));

            assertAll(
                    () -> assertTrue(tenant.column("nickname").isPresent(),
                            "V10 adds nickname to the tenant V2 creates"),
                    () -> assertTrue(tenant.column("time_zone").isPresent(),
                            "V12 adds time_zone to the tenant V2 creates"));
        }

        @Test
        @DisplayName("numeric segments compare as numbers, not as text")
        void shouldOrderByVersion(@TempDir final Path directory) {
            writeMigrations(directory);

            assertEquals(List.of("account", "tenant", "nickname", "time_zone"),
                    sqlOf(directory).stream().map(MigrationOrder::subject).toList());
        }

        @Test
        @DisplayName("a dotted version orders segment by segment")
        void shouldOrderDottedVersions(@TempDir final Path directory) {
            write(directory, "V1.1__b.sql", "create table b (id int);");
            write(directory, "V1.10__d.sql", "create table d (id int);");
            write(directory, "V1.2__c.sql", "create table c (id int);");
            write(directory, "V1__a.sql", "create table a (id int);");

            assertEquals(List.of("a", "b", "c", "d"),
                    sqlOf(directory).stream().map(MigrationOrder::subject).toList());
        }

        @Test
        @DisplayName("Flyway's underscore spelling of a dotted version reads the same way")
        void shouldOrderUnderscoreVersions(@TempDir final Path directory) {
            write(directory, "V1_10__c.sql", "create table c (id int);");
            write(directory, "V1_2__b.sql", "create table b (id int);");

            assertEquals(List.of("b", "c"), sqlOf(directory).stream().map(MigrationOrder::subject).toList());
        }

        @Test
        @DisplayName("a timestamp version does not overflow into the wrong order")
        void shouldOrderTimestampVersions(@TempDir final Path directory) {
            write(directory, "V20260808120000__b.sql", "create table b (id int);");
            write(directory, "V9999999999999999999999__c.sql", "create table c (id int);");
            write(directory, "V20260101000000__a.sql", "create table a (id int);");

            assertEquals(List.of("a", "b", "c"), sqlOf(directory).stream().map(MigrationOrder::subject).toList());
        }

        @Test
        @DisplayName("a file that is not a versioned migration keeps its place in name order, after them")
        void shouldFallBackToNameOrder(@TempDir final Path directory) {
            write(directory, "V2__b.sql", "create table b (id int);");
            write(directory, "V10__c.sql", "create table c (id int);");
            write(directory, "R__views.sql", "create table z_repeatable (id int);");
            write(directory, "aaa_schema.sql", "create table a_plain (id int);");

            assertEquals(List.of("b", "c", "z_repeatable", "a_plain"),
                    sqlOf(directory).stream().map(MigrationOrder::subject).toList());
        }

        @Test
        @DisplayName("a directory with no versioned migrations is read in name order, as before")
        void shouldKeepNameOrderWithoutVersions(@TempDir final Path directory) {
            write(directory, "20_second.sql", "create table second (id int);");
            write(directory, "10_first.sql", "create table first (id int);");

            assertEquals(List.of("first", "second"),
                    sqlOf(directory).stream().map(MigrationOrder::subject).toList());
        }

        /**
         * The table a {@code create} names, or the column an {@code alter} adds — enough to tell one
         * of these statements from another without pinning their whitespace.
         */
        private static String subject(final String sql) {
            final var words = sql.strip().split("\\s+");
            return words[0].equalsIgnoreCase("alter") ? words[5] : words[2];
        }

    }

    @Nested
    @DisplayName("a configured vendor names the database a schema does not name itself")
    class ConfiguredVendor {

        private static SchemaFiles readingAs(final Path directory, final String vendor) {
            return new SchemaFiles(
                    FilesConfigurations.defaults(),
                    SchemaConfiguration.builder()
                            .setSqlStatementsDirectory(directory.toAbsolutePath().toString())
                            .setVendor(vendor)
                            .build());
        }

        private static Optional<TypeName> typeOf(final SchemaFiles files, final String column) {
            final var catalog = Schemas.of(files.read()).applicableTo(Optional.empty()).getFirst();
            return SqlTypes.javaType(
                    catalog.table("attachment").orElseThrow().column(column).orElseThrow(),
                    catalog.dialect(Optional.empty()));
        }

        /**
         * A Flyway directory: the files are checksummed by the tool that applied them, so a
         * {@code -- vendor:} comment added to one already run is not a change a project can make.
         */
        private void writeMigration(final Path directory) {
            write(directory, "V1__attachment.sql", """
                    create table attachment (
                        id      bigserial primary key,
                        payload bytea not null,
                        at      timestamptz not null
                    );""");
        }

        @Test
        @DisplayName("its spellings reach a schema whose files say nothing")
        void shouldReadTheConfiguredVendorsSpellings(@TempDir final Path directory) {
            writeMigration(directory);

            assertAll(
                    () -> assertEquals(Optional.of("byte[]"),
                            typeOf(readingAs(directory, "PostgreSQL"), "payload").map(Object::toString)),
                    () -> assertEquals(Optional.of("java.time.Instant"),
                            typeOf(readingAs(directory, "PostgreSQL"), "at").map(Object::toString)),
                    () -> assertEquals(Optional.empty(),
                            typeOf(reading(directory), "payload"),
                            "without it the same column is a spelling nobody claimed"));
        }

        @Test
        @DisplayName("a file naming its own vendor keeps it")
        void shouldNotOverrideAFilesOwnVendor(@TempDir final Path directory) {
            write(directory, "V1__attachment.sql", """
                    -- vendor: mysql
                    create table attachment (id bigint auto_increment primary key);""");

            final var read = readingAs(directory, "PostgreSQL").read();

            assertEquals(List.of(Optional.of("mysql")),
                    read.stream().map(Schemas.VendorStatement::vendor).toList());
        }

    }

}
