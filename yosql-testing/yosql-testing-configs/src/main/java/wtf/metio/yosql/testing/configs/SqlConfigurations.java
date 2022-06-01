/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.testing.configs;

import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.configuration.SqlType;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.nio.file.Paths;
import java.util.List;

/**
 * Object mother for {@link SqlConfiguration}s and {@link SqlStatement}s.
 */
public final class SqlConfigurations {

    public static List<SqlStatement> sqlStatement() {
        return List.of(sqlStatement(sqlConfiguration()));
    }

    public static List<SqlStatement> sqlStatements() {
        return List.of(sqlStatement(sqlConfiguration()), sqlStatement(sqlConfiguration()));
    }

    public static List<SqlStatement> sqlStatementWithCustomConverter() {
        return List.of(sqlStatement(withCustomConverter()));
    }

    public static List<SqlStatement> sqlStatementsWithCustomConverter() {
        return List.of(sqlStatement(withCustomConverter()), sqlStatement(withCustomConverter()));
    }

    public static List<SqlStatement> sqlStatementsWithMixedConverter() {
        return List.of(sqlStatement(sqlConfiguration()), sqlStatement(withCustomConverter()));
    }

    private static SqlStatement sqlStatement(final SqlConfiguration configuration) {
        return SqlStatement.builder()
                .setSourcePath(Paths.get("src/main/yosql/data/queryData.sql"))
                .setConfiguration(configuration)
                .setRawStatement("SELECT raw FROM table WHERE test = ? AND id = ?;")
                .build();
    }

    public static SqlConfiguration sqlConfiguration() {
        final var config = SqlConfiguration.builder();
        config.setName("queryData");
        config.setType(SqlType.READING);
        config.setReturningMode(ReturningMode.MULTIPLE);
        config.setRepository("com.example.persistence.DataRepository");
        config.setCatchAndRethrow(true);
        config.setGenerateBatchApi(true);
        config.setGenerateBlockingApi(true);
        config.setParameters(List.of(SqlParameter.builder()
                .setName("test")
                .setType(Object.class.getName())
                .setIndices(new int[]{0})
                .build(), SqlParameter.builder()
                .setName("id")
                .setType(int.class.getName())
                .setIndices(new int[]{1})
                .build()));
        return config.build();
    }

    public static SqlConfiguration simpleSqlConfiguration() {
        return SqlConfiguration.builder().setName("queryData").build();
    }

    public static SqlConfiguration withCustomConverter() {
        return SqlConfiguration.copyOf(sqlConfiguration())
                .withResultRowConverter(ResultRowConverter.builder()
                        .setAlias("item")
                        .setConverterType("com.example.ToItemConverter")
                        .setMethodName("asItem")
                        .setResultType("com.example.domain.Item")
                        .build());
    }

    private SqlConfigurations() {
        // factory class
    }

}
