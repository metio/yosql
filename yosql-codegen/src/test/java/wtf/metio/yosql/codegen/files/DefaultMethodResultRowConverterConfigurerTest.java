/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.ConverterConfigurations;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import static org.junit.jupiter.api.Assertions.*;

class DefaultMethodResultRowConverterConfigurerTest {

    private DefaultMethodResultRowConverterConfigurer configurer;

    @BeforeEach
    void setUp() {
        configurer = new DefaultMethodResultRowConverterConfigurer(ConverterConfigurations.withConverters());
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

}
