/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.testing.configs;

import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;

import java.util.List;

/**
 * Object mother for {@link ConverterConfiguration}s.
 */
public final class ConverterConfigurations {

    public static ConverterConfiguration withoutConverters() {
        return ConverterConfiguration.builder().build();
    }

    public static ConverterConfiguration withConverters() {
        return ConverterConfiguration.copyOf(withoutConverters())
                .withDefaultConverter(toMapConverter())
                .withRowConverters(List.of(itemConverter()));
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
