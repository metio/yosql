/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("SqlTypes")
class SqlTypesTest {

    private static String javaType(final String sqlType, final boolean nullable) {
        return SqlTypes.javaType(new Column("c", sqlType, nullable), Optional.empty())
                .map(Object::toString).orElse("<unknown>");
    }

    private static String javaType(final String sqlType, final boolean nullable, final String vendor) {
        return SqlTypes.javaType(new Column("c", sqlType, nullable), Optional.of(vendor))
                .map(Object::toString).orElse("<unknown>");
    }

    @Nested
    @DisplayName("standard spellings")
    class Standard {

        @Test
        void shouldMapTheCommonTypes() {
            assertAll(
                    () -> assertEquals("java.lang.String", javaType("varchar(64)", false)),
                    () -> assertEquals("java.lang.String", javaType("text", false)),
                    () -> assertEquals("long", javaType("bigint", false)),
                    () -> assertEquals("int", javaType("integer", false)),
                    () -> assertEquals("boolean", javaType("boolean", false)),
                    () -> assertEquals("java.math.BigDecimal", javaType("numeric(12,2)", false)),
                    () -> assertEquals("java.util.UUID", javaType("uuid", false)),
                    () -> assertEquals("byte[]", javaType("blob", false)));
        }

        @Test
        @DisplayName("a zoned timestamp is an Instant, a plain one is not")
        void shouldDistinguishTimestamps() {
            assertAll(
                    () -> assertEquals("java.time.Instant", javaType("timestamp with time zone", false)),
                    () -> assertEquals("java.time.LocalDateTime", javaType("timestamp", false)),
                    () -> assertEquals("java.time.LocalDate", javaType("date", false)));
        }

        @Test
        @DisplayName("a precision does not change what the type is, wherever it is written")
        void shouldIgnorePrecisionInTheMiddle() {
            assertAll(
                    () -> assertEquals("java.time.Instant", javaType("timestamp(6) with time zone", false),
                            "the spelling pg_dump writes"),
                    () -> assertEquals("java.time.LocalDateTime", javaType("timestamp(3) without time zone", false)),
                    () -> assertEquals("java.time.LocalTime", javaType("time(3)", false)),
                    () -> assertEquals("java.math.BigDecimal", javaType("numeric(12,2)", false)),
                    () -> assertEquals("<unknown>", javaType("time(3) with time zone", false),
                            "no mapping for a zoned time, and reading it as a local one would drop the zone"));
        }

    }

    @Nested
    @DisplayName("nullability decides boxing")
    class Boxing {

        @Test
        @DisplayName("a not null column reads into a primitive")
        void shouldUsePrimitivesForNotNull() {
            assertAll(
                    () -> assertEquals("long", javaType("bigint", false)),
                    () -> assertEquals("boolean", javaType("boolean", false)));
        }

        @Test
        @DisplayName("a nullable column reads into the boxed type, or the first null row throws")
        void shouldBoxNullable() {
            assertAll(
                    () -> assertEquals("java.lang.Long", javaType("bigint", true)),
                    () -> assertEquals("java.lang.Boolean", javaType("boolean", true)));
        }

        @Test
        @DisplayName("a reference type is unaffected either way")
        void shouldLeaveReferenceTypesAlone() {
            assertEquals("java.lang.String", javaType("varchar(10)", true));
        }

    }

    @Nested
    @DisplayName("vendor spellings")
    class Vendors {

        @Test
        void shouldMapPostgres() {
            assertAll(
                    () -> assertEquals("long", javaType("bigserial", false, "PostgreSQL")),
                    () -> assertEquals("java.time.Instant", javaType("timestamptz", false, "PostgreSQL")),
                    () -> assertEquals("byte[]", javaType("bytea", false, "PostgreSQL")),
                    () -> assertEquals("java.lang.String", javaType("jsonb", false, "PostgreSQL")));
        }

        @Test
        void shouldMapOracleAndSqlServer() {
            assertAll(
                    () -> assertEquals("java.lang.String", javaType("varchar2(64)", false, "Oracle")),
                    () -> assertEquals("java.math.BigDecimal", javaType("number(19)", false, "Oracle")),
                    () -> assertEquals("java.util.UUID",
                            javaType("uniqueidentifier", false, "Microsoft SQL Server")),
                    () -> assertEquals("boolean", javaType("bit", false, "Microsoft SQL Server")));
        }

        @Test
        @DisplayName("a vendor falls back to the standard spellings it shares")
        void shouldFallBackToStandard() {
            assertEquals("java.lang.String", javaType("varchar(64)", false, "PostgreSQL"));
        }

        @Test
        @DisplayName("the vendor name is matched however it is cased")
        void shouldIgnoreVendorCase() {
            assertEquals("java.time.Instant", javaType("timestamptz", false, "postgresql"));
        }

    }

    @Nested
    @DisplayName("what it will not guess")
    class Unknown {

        @Test
        @DisplayName("an unrecognised type is unknown, never Object")
        void shouldNotFallBackToObject() {
            assertAll(
                    () -> assertTrue(SqlTypes.javaType(new Column("c", "hstore", true), Optional.empty()).isEmpty()),
                    () -> assertTrue(SqlTypes.javaType(new Column("c", "geometry", true), Optional.empty()).isEmpty()));
        }

        @Test
        @DisplayName("a vendor type is not assumed to mean the same elsewhere")
        void shouldNotLeakVendorTypesAcrossVendors() {
            assertTrue(SqlTypes.javaType(new Column("c", "bytea", false), Optional.of("Oracle")).isEmpty());
        }

    }

}
