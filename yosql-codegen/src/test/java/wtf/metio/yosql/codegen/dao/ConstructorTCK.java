/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testing.configs.SqlConfigurations;

/**
 * Verifies that {@link ConstructorGenerator}s work correctly.
 */
public abstract class ConstructorTCK {

    /**
     * @return A new {@link ConstructorGenerator}.
     */
    abstract ConstructorGenerator generator();

    /**
     * @return The expected generated code for a repository constructor.
     */
    abstract String forRepositoryExpectation();

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

}
