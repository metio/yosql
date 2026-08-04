/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.nio.file.Paths;
import java.util.List;

/**
 * What a statement writing {@code in (:names)} generates.
 *
 * <p>The expanded form is a different code path from the fixed-index one at almost every step: the
 * query is assembled rather than read from one constant, and binding counts placeholders instead of
 * looking their positions up. The snapshot expectations elsewhere all use fixed indices, so nothing
 * else here holds the expanded path to the same standard.</p>
 */
@DisplayName("a statement with a collection parameter")
class ExpandedParametersTest {

    private static SqlConfiguration withCollection() {
        return SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withParameters(SqlConfigurations.collectionParameter());
    }

    private static List<SqlStatement> statement(final SqlConfiguration configuration) {
        return List.of(SqlStatement.builder()
                .setSourcePath(Paths.get("src/main/yosql/queryData.sql"))
                .setConfiguration(configuration)
                .setRawStatement("SELECT raw FROM table WHERE name in (:names);")
                .build());
    }

    @Test
    @DisplayName("declares the raw query the log line reads")
    void picksRawQueryForLogging() {
        final var generated = DaoObjectMother.jdbcBlocks()
                .pickVendorQuery(statement(withCollection())).toString();
        Assertions.assertTrue(generated.contains("final var rawQuery = QUERY_DATA_RAW;"),
                () -> "logExecutedQuery reads rawQuery, so picking the query has to declare it:\n" + generated);
    }

    @Test
    @DisplayName("binds every local the log line and the binding use")
    void declaresEveryLocalItUses() {
        final var configuration = withCollection();
        final var jdbc = DaoObjectMother.jdbcBlocks();
        final var generated = jdbc.pickVendorQuery(statement(configuration)).toString()
                + jdbc.setParameters(configuration)
                + jdbc.logExecutedQuery(configuration);
        for (final var local : List.of("query", "rawQuery")) {
            Assertions.assertTrue(generated.contains("var " + local + " ="),
                    () -> "uses " + local + " without declaring it:\n" + generated);
        }
    }

    @Test
    @DisplayName("converts each element the way the same type converts on its own")
    void convertsElements() {
        final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withParameters(SqlParameter.builder()
                        .setName("moments")
                        .setType("java.util.List<java.time.Instant>")
                        .setIndices(new int[]{1})
                        .build());
        final var generated = DaoObjectMother.jdbcBlocks().setParameters(configuration).toString();
        Assertions.assertTrue(generated.contains("Timestamp.from(momentsElement)"),
                () -> "an Instant element binds as a Timestamp, exactly as a scalar Instant does:\n" + generated);
        Assertions.assertFalse(generated.contains("setObject(jdbcIndex++, momentsElement)"),
                () -> "the raw element must not reach the driver:\n" + generated);
    }

    @Test
    @DisplayName("binds an element needing no conversion as it is")
    void bindsPlainElementsUnchanged() {
        final var generated = DaoObjectMother.jdbcBlocks().setParameters(withCollection()).toString();
        Assertions.assertTrue(generated.contains("statement.setObject(jdbcIndex++, namesElement)"),
                () -> "a String element binds without a local of its own:\n" + generated);
    }

}
