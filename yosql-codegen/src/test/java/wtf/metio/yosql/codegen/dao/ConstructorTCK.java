/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.util.List;

/**
 * Verifies that {@link ConstructorGenerator}s work correctly.
 */
public abstract class ConstructorTCK {

    /**
     * @return A new {@link ConstructorGenerator}.
     */
    abstract ConstructorGenerator generator();

    /**
     * @return A {@link ConstructorGenerator} that generates one method per statement, so that a
     *         repository whose statements all take a given connection has nothing to do with a
     *         {@code DataSource}.
     */
    abstract ConstructorGenerator generatorWithoutConnectionOverloads();

    /**
     * @return The expected generated code for a repository constructor.
     */
    abstract String forRepositoryExpectation();

    /**
     * @return The expected generated code for a repository constructor that only uses given connections.
     */
    abstract String forRepositoryWithGivenConnectionsExpectation();

    /**
     * @return The expected generated code for a repository constructor with a custom converter.
     */
    abstract String forRepositoryWithCustomConverterExpectation();

    /**
     * @return The expected generated code for a repository constructor with multiple statements and converters.
     */
    abstract String forRepositoryWithMultipleStatementsAndMixedConverterExpectation();

    @Test
    final void forRepository() {
        Assertions.assertEquals(
                forRepositoryExpectation(),
                generator().repository(SqlConfigurations.sqlStatement()).toString(),
                "The generated constructor does not match expectation");
    }

    @Test
    final void forRepositoryWithCustomConverter() {
        Assertions.assertEquals(
                forRepositoryWithCustomConverterExpectation(),
                generator().repository(SqlConfigurations.sqlStatementWithCustomConverter()).toString(),
                "The generated constructor does not match expectation");
    }

    @Test
    final void forRepositoryWithMultipleStatementsAndMixedConverter() {
        Assertions.assertEquals(
                forRepositoryWithMultipleStatementsAndMixedConverterExpectation(),
                generator().repository(SqlConfigurations.sqlStatementsWithMixedConverter()).toString(),
                "The generated constructor does not match expectation");
    }

    @Test
    final void forRepositoryWithGivenConnections() {
        Assertions.assertEquals(
                forRepositoryWithGivenConnectionsExpectation(),
                generatorWithoutConnectionOverloads().repository(List.of(SqlConfigurations.sqlStatement(
                        SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                                .withCreateConnection(false)))).toString(),
                "The generated constructor does not match expectation");
    }

}
