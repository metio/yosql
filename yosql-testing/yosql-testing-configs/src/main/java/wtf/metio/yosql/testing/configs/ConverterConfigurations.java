/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.testing.configs;

import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import java.util.List;

public final class ConverterConfigurations {

    public static ConverterConfiguration withoutConverters() {
        return ConverterConfiguration.usingDefaults().build();
    }

    public static ConverterConfiguration withConverters() {
        return ConverterConfiguration.copyOf(withoutConverters())
                .withDefaultConverter(ResultRowConverter.builder()
                        .setAlias("toMap")
                        .setConverterType("com.example.persistence.converter.ToMapConverter")
                        .setMethodName("apply")
                        .setResultType("java.util.Map<String, Object>")
                        .build())
                .withRowConverters(List.of(ResultRowConverter.builder()
                        .setAlias("item")
                        .setConverterType("com.example.persistence.converter.ToItemConverter")
                        .setMethodName("asItem")
                        .setResultType("com.example.domain.Item")
                        .build()));
    }

    private ConverterConfigurations() {
        // factory class
    }

}
