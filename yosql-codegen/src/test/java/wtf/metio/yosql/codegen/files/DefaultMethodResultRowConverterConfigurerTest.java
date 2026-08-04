/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import wtf.metio.yosql.codegen.exceptions.MissingConverterSourceException;
import wtf.metio.yosql.codegen.exceptions.UnreadableResultRowTypeException;
import wtf.metio.yosql.codegen.exceptions.UnusableConverterException;
import wtf.metio.yosql.codegen.records.JavaSourceParser;
import wtf.metio.yosql.codegen.records.RecordConverterNames;
import wtf.metio.yosql.codegen.records.RecordScanner;
import wtf.metio.yosql.internals.testing.configs.ConverterConfigurations;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DefaultMethodResultRowConverterConfigurer")
class DefaultMethodResultRowConverterConfigurerTest {

    private static final String CONVERTER = "com.example.persistence.converter.ToItemConverter";

    @TempDir
    Path sources;

    private DefaultMethodResultRowConverterConfigurer configurer() {
        return configurer(ConverterConfigurations.withConverters());
    }

    private DefaultMethodResultRowConverterConfigurer configurer(final ConverterConfiguration converters) {
        return new DefaultMethodResultRowConverterConfigurer(
                converters,
                new RecordConverterNames(converters),
                new RecordScanner(
                        FilesConfiguration.builder().setSourceDirectory(sources).build(),
                        new JavaSourceParser()));
    }

    private void write(final String packageName, final String simpleName, final String body) throws IOException {
        final var directory = sources.resolve(packageName.replace('.', '/'));
        Files.createDirectories(directory);
        Files.writeString(directory.resolve(simpleName + ".java"), body);
    }

    private void writeItemConverter() throws IOException {
        write("com.example.persistence.converter", "ToItemConverter", """
                package com.example.persistence.converter;

                import com.example.domain.Item;

                import java.sql.ResultSet;
                import java.sql.SQLException;

                public final class ToItemConverter {

                    public Item asItem(final ResultSet resultSet) throws SQLException {
                        return new Item(resultSet.getString("name"));
                    }

                }
                """);
    }

    private ResultRowConverter resolve(final String converterClass) {
        return configurer().configureResultRowConverter(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withResultRowConverter(ResultRowConverter.fromClassName(converterClass)))
                .resultRowConverter()
                .orElseThrow();
    }

    @Nested
    @DisplayName("reading the converter's own source")
    class FromSource {

        @Test
        @DisplayName("naming the class is enough to find the method, its result and the field")
        void resolvesEveryPartFromTheClass() throws IOException {
            writeItemConverter();

            final var converter = resolve(CONVERTER);

            assertAll(
                    () -> assertEquals("toItemConverter", converter.alias().orElseThrow()),
                    () -> assertEquals(CONVERTER, converter.converterType().orElseThrow()),
                    () -> assertEquals("asItem", converter.methodName().orElseThrow()),
                    () -> assertEquals("com.example.domain.Item", converter.resultType().orElseThrow())
            );
        }

        @Test
        @DisplayName("the field is the class name with a lower-case first letter")
        void aliasFollowsTheClassName() throws IOException {
            write("com.example", "RowMapper", """
                    package com.example;

                    import java.sql.ResultSet;

                    public class RowMapper {
                        public String read(ResultSet resultSet) throws java.sql.SQLException {
                            return resultSet.getString(1);
                        }
                    }
                    """);

            assertEquals("rowMapper", resolve("com.example.RowMapper").alias().orElseThrow());
        }

        @Test
        @DisplayName("a fully-qualified ResultSet parameter is the same parameter")
        void acceptsQualifiedResultSet() throws IOException {
            write("com.example", "Qualified", """
                    package com.example;

                    public class Qualified {
                        public java.util.UUID read(final java.sql.ResultSet resultSet) {
                            return null;
                        }
                    }
                    """);

            final var converter = resolve("com.example.Qualified");

            assertAll(
                    () -> assertEquals("read", converter.methodName().orElseThrow()),
                    () -> assertEquals("java.util.UUID", converter.resultType().orElseThrow())
            );
        }

        @Test
        @DisplayName("a generic result type keeps its type arguments")
        void keepsTypeArguments() throws IOException {
            write("com.example", "ToMap", """
                    package com.example;

                    import java.sql.ResultSet;
                    import java.util.Map;

                    public class ToMap {
                        public Map<String, Object> apply(ResultSet resultSet) {
                            return Map.of();
                        }
                    }
                    """);

            assertEquals("java.util.Map<java.lang.String, java.lang.Object>",
                    resolve("com.example.ToMap").resultType().orElseThrow());
        }

        @Test
        @DisplayName("only public instance methods are candidates")
        void ignoresStaticAndNonPublicMethods() throws IOException {
            write("com.example", "Mixed", """
                    package com.example;

                    import java.sql.ResultSet;

                    public class Mixed {
                        private String hidden(ResultSet resultSet) { return null; }
                        static String factory(ResultSet resultSet) { return null; }
                        public static String staticFactory(ResultSet resultSet) { return null; }
                        protected String shielded(ResultSet resultSet) { return null; }
                        public String read(ResultSet resultSet) { return null; }
                    }
                    """);

            assertEquals("read", resolve("com.example.Mixed").methodName().orElseThrow());
        }

        @Test
        @DisplayName("surrounding whitespace is not part of the class name")
        void trimsTheClassName() throws IOException {
            writeItemConverter();

            assertEquals(CONVERTER, resolve("  " + CONVERTER + "  ").converterType().orElseThrow());
        }

    }

    @Nested
    @DisplayName("failing the build rather than the compile")
    class Failures {

        @Test
        @DisplayName("a resultRowType that is not a class name names the statement that declared it")
        void unreadableResultRowType() {
            final var configuration = SqlConfiguration.builder()
                    .setName("findTenant")
                    .setResultRowType("com.example.domain.Tenant<T>")
                    .build();

            final var thrown = assertThrows(UnreadableResultRowTypeException.class,
                    () -> configurer().configureResultRowConverter(configuration));
            assertAll(
                    () -> assertTrue(thrown.getMessage().contains("findTenant"), thrown::getMessage),
                    () -> assertTrue(thrown.getMessage().contains("com.example.domain.Tenant<T>"),
                            thrown::getMessage));
        }

        @Test
        @DisplayName("a converter with no source cannot be read")
        void missingSource() {
            final var exception = assertThrows(MissingConverterSourceException.class, () -> resolve(CONVERTER));

            assertAll(
                    () -> assertTrue(exception.getMessage().contains(CONVERTER)),
                    () -> assertTrue(exception.getMessage().contains("sourceDirectory"))
            );
        }

        @Test
        @DisplayName("a class with no ResultSet method has nothing to call")
        void noResultSetMethod() throws IOException {
            write("com.example", "Empty", """
                    package com.example;

                    public class Empty {
                        public String read(String notAResultSet) { return null; }
                    }
                    """);

            final var exception = assertThrows(UnusableConverterException.class, () -> resolve("com.example.Empty"));

            assertTrue(exception.getMessage().contains("declares no public method"));
        }

        @Test
        @DisplayName("a class with several ResultSet methods leaves the choice ambiguous")
        void severalResultSetMethods() throws IOException {
            write("com.example", "Ambiguous", """
                    package com.example;

                    import java.sql.ResultSet;

                    public class Ambiguous {
                        public String readName(ResultSet resultSet) { return null; }
                        public Integer readAge(ResultSet resultSet) { return null; }
                    }
                    """);

            final var exception = assertThrows(UnusableConverterException.class, () -> resolve("com.example.Ambiguous"));

            assertAll(
                    () -> assertTrue(exception.getMessage().contains("readName(ResultSet)")),
                    () -> assertTrue(exception.getMessage().contains("readAge(ResultSet)"))
            );
        }

        @Test
        @DisplayName("something that is not a class name is not a converter")
        void notAClassName() {
            final var exception = assertThrows(UnusableConverterException.class, () -> resolve("not a class"));

            assertTrue(exception.getMessage().contains("is not a class name"));
        }

        @Test
        @DisplayName("the default converter names the option that has to change")
        void namesTheConfigurationOption() {
            final var configurer = configurer(ConverterConfigurations.namingConverter(CONVERTER));

            final var exception = assertThrows(MissingConverterSourceException.class,
                    () -> configurer.configureResultRowConverter(SqlConfigurations.sqlConfiguration()));

            assertTrue(exception.getMessage().contains("defaultConverter"));
        }

    }

    @Nested
    @DisplayName("choosing which converter applies")
    class Precedence {

        @Test
        @DisplayName("a statement that names neither takes the build's default")
        void fallsBackToTheDefault() {
            final var converter = configurer().configureResultRowConverter(SqlConfigurations.sqlConfiguration())
                    .resultRowConverter().orElseThrow();

            assertEquals("toMap", converter.alias().orElseThrow());
        }

        @Test
        @DisplayName("the generated ToMap converter is used as configured, not read from source")
        void doesNotScanAResolvedDefault() {
            final var converter = configurer().configureResultRowConverter(SqlConfigurations.sqlConfiguration())
                    .resultRowConverter().orElseThrow();

            assertAll(
                    () -> assertEquals("com.example.persistence.converter.ToMapConverter",
                            converter.converterType().orElseThrow()),
                    () -> assertEquals("apply", converter.methodName().orElseThrow()),
                    () -> assertEquals("java.util.Map<java.lang.String, java.lang.Object>",
                            converter.resultType().orElseThrow())
            );
        }

        @Test
        @DisplayName("a default converter named by class is read from source like any other")
        void resolvesTheDefaultFromSource() throws IOException {
            writeItemConverter();

            final var converter = configurer(ConverterConfigurations.namingConverter(CONVERTER))
                    .configureResultRowConverter(SqlConfigurations.sqlConfiguration())
                    .resultRowConverter().orElseThrow();

            assertAll(
                    () -> assertEquals("toItemConverter", converter.alias().orElseThrow()),
                    () -> assertEquals("asItem", converter.methodName().orElseThrow())
            );
        }

        @Test
        @DisplayName("a result row type points the statement at the converter that will be generated for it")
        void configureResultRowConverterFromResultRowType() {
            final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                    .withResultRowType("com.example.domain.Tenant");
            final var converter = configurer().configureResultRowConverter(configuration)
                    .resultRowConverter().orElseThrow();

            assertAll(
                    () -> assertEquals("tenantConverter", converter.alias().orElseThrow()),
                    () -> assertEquals("com.example.persistence.converter.ToTenantConverter",
                            converter.converterType().orElseThrow()),
                    () -> assertEquals("asUserType", converter.methodName().orElseThrow()),
                    () -> assertEquals("com.example.domain.Tenant", converter.resultType().orElseThrow())
            );
        }

        @Test
        @DisplayName("surrounding whitespace in a result row type is not part of the name")
        void trimsResultRowType() {
            final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                    .withResultRowType("  com.example.domain.Tenant  ");
            final var converter = configurer().configureResultRowConverter(configuration)
                    .resultRowConverter().orElseThrow();

            assertEquals("com.example.domain.Tenant", converter.resultType().orElseThrow());
        }

        @Test
        @DisplayName("a blank result row type is no result row type")
        void ignoresBlankResultRowType() {
            final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                    .withResultRowType("   ");
            final var converter = configurer().configureResultRowConverter(configuration)
                    .resultRowConverter().orElseThrow();

            assertEquals("toMap", converter.alias().orElseThrow(), "falls back to the default converter");
        }

        @Test
        @DisplayName("naming a converter outright wins over naming a record")
        void explicitConverterWins() throws IOException {
            writeItemConverter();
            final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                    .withResultRowType("com.example.domain.Tenant")
                    .withResultRowConverter(ResultRowConverter.fromClassName(CONVERTER));
            final var converter = configurer().configureResultRowConverter(configuration)
                    .resultRowConverter().orElseThrow();

            assertAll(
                    () -> assertEquals("toItemConverter", converter.alias().orElseThrow()),
                    () -> assertEquals(CONVERTER, converter.converterType().orElseThrow())
            );
        }

    }

}
