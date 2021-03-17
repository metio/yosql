/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.testing.configs;

import wtf.metio.yosql.models.constants.sql.SqlType;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;
import wtf.metio.yosql.models.sql.ResultRowConverter;
import wtf.metio.yosql.models.sql.SqlParameter;

import java.nio.file.Paths;
import java.util.List;

public final class Sql {

    public static List<SqlStatement> sqlStatements() {
        return List.of(SqlStatement.builder()
                .setSourcePath(Paths.get("src/main/yosql/data/queryData.sql"))
                .setRawStatement("SELECT raw FROM table WHERE test = ? AND id = ?;")
                .setConfiguration(sqlConfiguration())
                .build());
    }

    public static SqlConfiguration sqlConfiguration() {
        final var config = SqlConfiguration.builder();
        config.setName("queryData");
        config.setType(SqlType.READING);
        config.setParameters(List.of(SqlParameter.builder()
                .setName("test")
                .setType(Object.class.getName())
                .setIndices(0)
                .build(), SqlParameter.builder()
                .setName("id")
                .setType(int.class.getName())
                .setIndices(1)
                .build()));
        config.setResultRowConverter(ResultRowConverter.builder()
                .setAlias("resultRow")
                .setConverterType("com.example.ToResultRowConverter")
                .setResultType("com.example.util.ResultRow")
                .setMethodName("apply")
                .build());
        config.setRepository("com.example.persistence.DataRepository");
        return config.build();
    }

    private Sql() {
        // factory class
    }

}
