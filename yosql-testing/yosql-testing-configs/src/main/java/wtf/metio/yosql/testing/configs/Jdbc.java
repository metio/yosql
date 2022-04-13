/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.testing.configs;

import wtf.metio.yosql.models.immutables.JdbcConfiguration;
import wtf.metio.yosql.models.sql.ResultRowConverter;

public final class Jdbc {

    public static JdbcConfiguration defaults() {
        return JdbcConfiguration.usingDefaults().build();
    }

    public static JdbcConfiguration withResultRowConverter() {
        return JdbcConfiguration.copyOf(defaults())
                .withDefaultConverter(ResultRowConverter.builder()
                        .setAlias("resultRow")
                        .setConverterType("com.example.persistence.util.ToResultRowConverter")
                        .setMethodName("asUserType")
                        .setResultType("com.example.persistence.util.ResultRow")
                        .build());
    }

    private Jdbc() {
        // factory class
    }

}
