/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.squareup.javapoet.CodeBlock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import wtf.metio.yosql.codegen.exceptions.NonRecordValueTypeException;
import wtf.metio.yosql.codegen.exceptions.UnbindableParameterException;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.immutables.FilesConfiguration;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ParameterConversions")
class ParameterConversionsTest {

    private static final String DOMAIN = "com.example.domain";

    @TempDir
    Path sources;

    private void write(final String simpleName, final String body) {
        try {
            final var directory = sources.resolve(DOMAIN.replace('.', '/'));
            Files.createDirectories(directory);
            Files.writeString(directory.resolve(simpleName + ".java"), body);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private ParameterConversions conversions() {
        return new ParameterConversions(
                new RecordScanner(
                        FilesConfiguration.builder().setSourceDirectory(sources).build(),
                        new JavaSourceParser()),
                new StatementBinders());
    }

    private static SqlParameter parameter(final String name, final String type) {
        return SqlParameter.builder().setName(name).setType(type).build();
    }

    private String convert(final String name, final String type) {
        return conversions().convert(parameter(name, type), CodeBlock.of("$N", name))
                .map(ParameterConversions.Conversion::declarations)
                .map(CodeBlock::toString)
                .orElse("");
    }

    private void writeTenantId() {
        write("TenantId", """
                package com.example.domain;

                import java.util.UUID;

                public record TenantId(UUID value) {
                    public static TenantId valueOf(final UUID value) {
                        return new TenantId(value);
                    }
                }
                """);
    }

    @Nested
    @DisplayName("types a driver already takes")
    class Unchanged {

        @Test
        @DisplayName("leaves a String alone")
        void string() {
            assertTrue(conversions().convert(parameter("slug", "java.lang.String"),
                    CodeBlock.of("slug")).isEmpty());
        }

        @Test
        @DisplayName("leaves a UUID alone")
        void uuid() {
            assertTrue(conversions().convert(parameter("id", "java.util.UUID"),
                    CodeBlock.of("id")).isEmpty());
        }

        @Test
        @DisplayName("leaves a Timestamp alone, so existing statements generate what they always did")
        void timestamp() {
            assertTrue(conversions().convert(parameter("at", "java.sql.Timestamp"),
                    CodeBlock.of("at")).isEmpty());
        }

        @Test
        @DisplayName("leaves a primitive alone")
        void primitive() {
            assertTrue(conversions().convert(parameter("count", "long"),
                    CodeBlock.of("count")).isEmpty());
        }

        @Test
        @DisplayName("leaves a type it cannot see alone rather than refusing it")
        void unknownType() {
            assertTrue(conversions().convert(parameter("thing", "com.elsewhere.Thing"),
                    CodeBlock.of("thing")).isEmpty());
        }

    }

    @Nested
    @DisplayName("types a driver refuses")
    class BuiltIn {

        @Test
        @DisplayName("turns an Instant into a Timestamp, which is what PostgreSQL will take")
        void instant() {
            assertEquals("final java.sql.Timestamp atParameter = at == null ? null : "
                    + "java.sql.Timestamp.from(at);\n", convert("at", "java.time.Instant"));
        }

        @Test
        @DisplayName("writes a Currency as its code")
        void currency() {
            assertEquals("final java.lang.String currencyParameter = currency == null ? null : "
                    + "currency.getCurrencyCode();\n", convert("currency", "java.util.Currency"));
        }

        @Test
        @DisplayName("writes an enum as its name")
        void enums() {
            write("OrderState", """
                    package com.example.domain;

                    public enum OrderState {
                        DRAFT,
                        ACTIVE
                    }
                    """);
            assertEquals("final java.lang.String stateParameter = state == null ? null : "
                    + "state.name();\n", convert("state", DOMAIN + ".OrderState"));
        }

    }

    @Nested
    @DisplayName("value types")
    class ValueTypes {

        @Test
        @DisplayName("binds the accessor the type's own valueOf takes")
        void unwraps() {
            writeTenantId();
            assertEquals("final java.util.UUID tenantIdParameter = tenantId == null ? null : "
                    + "tenantId.value();\n", convert("tenantId", DOMAIN + ".TenantId"));
        }

        @Test
        @DisplayName("boxes a primitive accessor so null stays possible")
        void boxesPrimitives() {
            write("Cents", """
                    package com.example.domain;

                    public record Cents(long value) {
                        public static Cents valueOf(long value) {
                            return new Cents(value);
                        }
                    }
                    """);
            assertEquals("final java.lang.Long balanceParameter = balance == null ? null : "
                    + "balance.value();\n", convert("balance", DOMAIN + ".Cents"));
        }

        @Test
        @DisplayName("keeps converting when the value inside also needs it")
        void composesWithTheBuiltInTable() {
            write("At", """
                    package com.example.domain;

                    import java.time.Instant;

                    public record At(Instant value) {
                        public static At valueOf(final Instant value) {
                            return new At(value);
                        }
                    }
                    """);
            final var code = convert("at", DOMAIN + ".At");
            assertTrue(code.contains("final java.time.Instant atParameter = at == null ? null : at.value();"), code);
            assertTrue(code.contains("final java.sql.Timestamp atParameter2 = atParameter == null ? null : "
                    + "java.sql.Timestamp.from(atParameter);"), code);
        }

        @Test
        @DisplayName("picks the component whose type the factory takes")
        void picksTheRightAccessor() {
            write("Tagged", """
                    package com.example.domain;

                    import java.util.UUID;

                    public record Tagged(String label, UUID value) {
                        public static Tagged valueOf(final UUID value) {
                            return new Tagged("", value);
                        }
                    }
                    """);
            assertTrue(convert("tag", DOMAIN + ".Tagged").contains("tag.value()"));
        }

        @Test
        @DisplayName("reads a batch element rather than the array")
        void batchSource() {
            writeTenantId();
            final var conversion = conversions().convert(parameter("tenantId", DOMAIN + ".TenantId"),
                    CodeBlock.of("$N[$N]", "tenantId", "batch")).orElseThrow();
            assertEquals("final java.util.UUID tenantIdParameter = tenantId[batch] == null ? null : "
                    + "tenantId[batch].value();\n", conversion.declarations().toString());
            assertEquals("tenantIdParameter", conversion.boundName());
        }

    }

    @Nested
    @DisplayName("what it refuses")
    class Refusals {

        @Test
        @DisplayName("refuses a record that is not one value")
        void multiComponentRecord() {
            write("Money", """
                    package com.example.domain;

                    import java.util.Currency;

                    public record Money(long minorUnits, Currency currency) {
                    }
                    """);
            final var exception = assertThrows(UnbindableParameterException.class,
                    () -> convert("amount", DOMAIN + ".Money"));
            assertTrue(exception.getMessage().contains("minorUnits, currency"), exception.getMessage());
            assertTrue(exception.getMessage().contains("a parameter for each component"), exception.getMessage());
        }

        @Test
        @DisplayName("refuses a value type that is not a record, and says why")
        void nonRecordValueType() {
            write("Ident", """
                    package com.example.domain;

                    public final class Ident {
                        public static Ident valueOf(String value) {
                            return new Ident();
                        }
                    }
                    """);
            final var exception = assertThrows(NonRecordValueTypeException.class,
                    () -> convert("ident", DOMAIN + ".Ident"));
            assertTrue(exception.getMessage().contains("not a record"), exception.getMessage());
        }

        @Test
        @DisplayName("refuses a factory taking something the record does not hold")
        void factoryWithoutMatchingComponent() {
            write("Odd", """
                    package com.example.domain;

                    public record Odd(String label) {
                        public static Odd valueOf(long value) {
                            return new Odd(Long.toString(value));
                        }
                    }
                    """);
            assertThrows(UnbindableParameterException.class, () -> convert("odd", DOMAIN + ".Odd"));
        }

    }

}
