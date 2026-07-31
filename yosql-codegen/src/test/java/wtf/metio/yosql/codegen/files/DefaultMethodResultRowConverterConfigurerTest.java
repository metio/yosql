/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.records.RecordConverterNames;
import wtf.metio.yosql.internals.testing.configs.ConverterConfigurations;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DefaultMethodResultRowConverterConfigurer")
class DefaultMethodResultRowConverterConfigurerTest {

    private DefaultMethodResultRowConverterConfigurer configurer;

    @BeforeEach
    void setUp() {
        final var converters = ConverterConfigurations.withConverters();
        configurer = new DefaultMethodResultRowConverterConfigurer(converters, new RecordConverterNames(converters));
    }

    @Test
    void configureResultRowConverterWithAliasOnlyConverter() {
        final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withResultRowConverter(ResultRowConverter.builder()
                        .setAlias("item")
                        .build());
        final var adapted = configurer.configureResultRowConverter(configuration);

        assertTrue(adapted.resultRowConverter().isPresent());
        assertAll(
                () -> assertTrue(adapted.resultRowConverter().get().alias().isPresent()),
                () -> assertTrue(adapted.resultRowConverter().get().converterType().isPresent()),
                () -> assertTrue(adapted.resultRowConverter().get().methodName().isPresent()),
                () -> assertTrue(adapted.resultRowConverter().get().resultType().isPresent())
        );
        assertAll(
                () -> assertEquals("item", adapted.resultRowConverter().get().alias().get()),
                () -> assertEquals("com.example.persistence.converter.ToItemConverter", adapted.resultRowConverter().get().converterType().get()),
                () -> assertEquals("asItem", adapted.resultRowConverter().get().methodName().get()),
                () -> assertEquals("com.example.domain.Item", adapted.resultRowConverter().get().resultType().get())
        );
    }

    @Test
    void configureResultRowConverterFallbackToDefaultConverter() {
        final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withResultRowConverter(ResultRowConverter.builder()
                        .setAlias("test")
                        .build());
        final var adapted = configurer.configureResultRowConverter(configuration);

        assertTrue(adapted.resultRowConverter().isPresent());
        assertAll(
                () -> assertTrue(adapted.resultRowConverter().get().alias().isPresent()),
                () -> assertTrue(adapted.resultRowConverter().get().converterType().isPresent()),
                () -> assertTrue(adapted.resultRowConverter().get().methodName().isPresent()),
                () -> assertTrue(adapted.resultRowConverter().get().resultType().isPresent())
        );
        assertAll(
                () -> assertEquals("toMap", adapted.resultRowConverter().get().alias().get()),
                () -> assertEquals("com.example.persistence.converter.ToMapConverter", adapted.resultRowConverter().get().converterType().get()),
                () -> assertEquals("apply", adapted.resultRowConverter().get().methodName().get()),
                () -> assertEquals("java.util.Map<java.lang.String, java.lang.Object>", adapted.resultRowConverter().get().resultType().get())
        );
    }

    @Test
    @DisplayName("a result row type points the statement at the converter that will be generated for it")
    void configureResultRowConverterFromResultRowType() {
        final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withResultRowType("com.example.domain.Tenant");
        final var converter = configurer.configureResultRowConverter(configuration).resultRowConverter().orElseThrow();

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
        final var converter = configurer.configureResultRowConverter(configuration).resultRowConverter().orElseThrow();

        assertEquals("com.example.domain.Tenant", converter.resultType().orElseThrow());
    }

    @Test
    @DisplayName("a blank result row type is no result row type")
    void ignoresBlankResultRowType() {
        final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withResultRowType("   ");
        final var converter = configurer.configureResultRowConverter(configuration).resultRowConverter().orElseThrow();

        assertEquals("toMap", converter.alias().orElseThrow(), "falls back to the default converter");
    }

    @Test
    @DisplayName("naming a converter outright wins over naming a record")
    void explicitConverterWins() {
        final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withResultRowType("com.example.domain.Tenant")
                .withResultRowConverter(ResultRowConverter.builder()
                        .setAlias("item")
                        .build());
        final var converter = configurer.configureResultRowConverter(configuration).resultRowConverter().orElseThrow();

        assertAll(
                () -> assertEquals("item", converter.alias().orElseThrow()),
                () -> assertEquals("com.example.persistence.converter.ToItemConverter",
                        converter.converterType().orElseThrow())
        );
    }

    @Test
    @DisplayName("without either, the default converter still applies")
    void noResultRowTypeKeepsTheDefault() {
        final var converter = configurer.configureResultRowConverter(SqlConfigurations.sqlConfiguration())
                .resultRowConverter().orElseThrow();

        assertEquals("toMap", converter.alias().orElseThrow());
    }

}
