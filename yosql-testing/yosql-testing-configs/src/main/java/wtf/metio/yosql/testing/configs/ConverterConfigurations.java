/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.testing.configs;

import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.sql.ResultRowConverter;

public final class ConverterConfigurations {

    public static ConverterConfiguration defaults() {
        return ConverterConfiguration.usingDefaults().build();
    }

    public static ConverterConfiguration withResultRowConverter() {
        return ConverterConfiguration.copyOf(defaults())
                .withDefaultConverter(ResultRowConverter.builder()
                        .setAlias("resultRow")
                        .setConverterType("com.example.persistence.converter.ToResultRowConverter")
                        .setMethodName("apply")
                        .setResultType("com.example.persistence.converter.ResultRow")
                        .build());
    }

    private ConverterConfigurations() {
        // factory class
    }

}
