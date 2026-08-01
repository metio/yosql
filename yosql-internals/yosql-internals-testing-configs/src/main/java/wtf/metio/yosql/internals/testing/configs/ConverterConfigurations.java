/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.internals.testing.configs;

import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;

/**
 * Object mother for {@link ConverterConfiguration}s.
 */
public final class ConverterConfigurations {

    public static ConverterConfiguration withoutConverters() {
        return ConverterConfiguration.builder().build();
    }

    /**
     * A configuration whose default converter is already fully resolved, the way the generated
     * ToMap converter arrives from every frontend.
     */
    public static ConverterConfiguration withConverters() {
        return ConverterConfiguration.copyOf(withoutConverters())
                .withDefaultConverter(toMapConverter());
    }

    /**
     * A converter as a user names one: a class and nothing else.
     */
    public static ConverterConfiguration namingConverter(final String converterClass) {
        return ConverterConfiguration.copyOf(withoutConverters())
                .withDefaultConverter(ResultRowConverter.fromClassName(converterClass));
    }

    public static ResultRowConverter toMapConverter() {
        return ResultRowConverter.builder()
                .setAlias("toMap")
                .setConverterType("com.example.persistence.converter.ToMapConverter")
                .setMethodName("apply")
                .setResultType("java.util.Map<java.lang.String, java.lang.Object>")
                .build();
    }

    public static ResultRowConverter itemConverter() {
        return ResultRowConverter.builder()
                .setAlias("item")
                .setConverterType("com.example.persistence.converter.ToItemConverter")
                .setMethodName("asItem")
                .setResultType("com.example.domain.Item")
                .build();
    }

    private ConverterConfigurations() {
        // factory class
    }

}
