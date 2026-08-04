/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.internals.testing.configs;

import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.configuration.SqlStatementType;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.nio.file.Paths;
import java.util.List;

/**
 * Object mother for {@link SqlConfiguration}s, {@link SqlStatement}s, and {@link SqlParameter}s.
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

    public static SqlStatement sqlStatement(final SqlConfiguration configuration) {
        return SqlStatement.builder()
                .setSourcePath(Paths.get("src/main/yosql/queryData.sql"))
                .setConfiguration(configuration)
                .setRawStatement("SELECT raw FROM table WHERE test = ? AND id = ?;")
                .build();
    }

    public static SqlConfiguration sqlConfiguration() {
        final var config = SqlConfiguration.builder();
        config.setName("queryData");
        config.setType(SqlStatementType.READING);
        config.setReturningMode(ReturningMode.MULTIPLE);
        config.setRepository("com.example.persistence.DataRepository");
        config.setCatchAndRethrow(true);
        config.setExecuteOnce(true);
        config.setUsePreparedStatement(true);
        config.setCreateConnection(true);
        config.addParameters(testParameter(), idParameter());
        return config.build();
    }

    public static SqlParameter testParameter() {
        return SqlParameter.builder()
                .setName("test")
                .setType(Object.class.getName())
                .setIndices(new int[]{0})
                .build();
    }

    public static SqlParameter idParameter() {
        return SqlParameter.builder()
                .setName("id")
                .setType(int.class.getName())
                .setIndices(new int[]{1})
                .build();
    }

    /**
     * A parameter whose values each need a placeholder of their own.
     */
    public static SqlParameter collectionParameter() {
        return SqlParameter.builder()
                .setName("names")
                .setType("java.util.List<java.lang.String>")
                .setIndices(new int[]{1})
                .build();
    }

    /**
     * Two vendor variants of one statement that do not bind the same parameters: the H2 variant binds
     * only {@code id} while the PostgreSQL variant binds {@code test} as well. Both share the single
     * generated method, whose signature is the union of the two.
     */
    public static List<SqlStatement> sqlStatementsWithDifferingVendorParameters() {
        return List.of(
                sqlStatement(SqlConfiguration.copyOf(sqlConfiguration())
                        .withVendor("H2")
                        .withParameters(idParameter())),
                sqlStatement(SqlConfiguration.copyOf(sqlConfiguration())
                        .withVendor("PostgreSQL")
                        .withParameters(testParameter(), idParameter())));
    }

    /**
     * Two vendor variants of one statement where only one of them binds anything at all.
     */
    public static List<SqlStatement> sqlStatementsWithVendorWithoutParameters() {
        return List.of(
                sqlStatement(SqlConfiguration.copyOf(sqlConfiguration())
                        .withVendor("H2")
                        .withParameters()),
                sqlStatement(SqlConfiguration.copyOf(sqlConfiguration())
                        .withVendor("PostgreSQL")
                        .withParameters(idParameter())));
    }

    public static SqlConfiguration simpleSqlConfiguration() {
        return SqlConfiguration.builder().setName("queryData").build();
    }

    public static SqlConfiguration withCustomConverter() {
        return SqlConfiguration.copyOf(sqlConfiguration())
                .withResultRowConverter(ConverterConfigurations.itemConverter());
    }

    private SqlConfigurations() {
        // factory class
    }

}
