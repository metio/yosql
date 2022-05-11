/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.tck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.api.ConstructorGenerator;
import wtf.metio.yosql.testing.configs.SqlConfigurations;

/**
 * Verifies that {@link ConstructorGenerator}s work correctly.
 */
public interface ConstructorTCK {

    ConstructorGenerator generator();

    String forRepositoryExpectation();
    String forRepositoryWithCustomConverterExpectation();
    String forRepositoryWithMultipleStatementsAndMixedConverterExpectation();

    @Test
    default void forRepository() {
        Assertions.assertEquals(
                forRepositoryExpectation(),
                generator().forRepository(SqlConfigurations.sqlStatement()).toString(),
                "The generated constructor does not match expectation");
    }

    @Test
    default void forRepositoryWithCustomConverter() {
        Assertions.assertEquals(
                forRepositoryWithCustomConverterExpectation(),
                generator().forRepository(SqlConfigurations.sqlStatementWithCustomConverter()).toString(),
                "The generated constructor does not match expectation");
    }

    @Test
    default void forRepositoryWithMultipleStatementsAndMixedConverter() {
        Assertions.assertEquals(
                forRepositoryWithMultipleStatementsAndMixedConverterExpectation(),
                generator().forRepository(SqlConfigurations.sqlStatementsWithMixedConverter()).toString(),
                "The generated constructor does not match expectation");
    }

}
