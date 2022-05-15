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
 * Verifies that {@link CallMethodGenerator}s work correctly.
 */
public interface CallMethodGeneratorTCK {

    /**
     * @return A new {@link CallMethodGenerator}.
     */
    CallMethodGenerator generator();

    /**
     * @return The expected generated code for a blocking call method.
     */
    String callMethodExpectation();

    @Test
    default void callMethod() {
        Assertions.assertEquals(
                callMethodExpectation(),
                generator().callMethod(SqlConfigurations.sqlConfiguration(), SqlConfigurations.sqlStatement()).toString(),
                "The generated call method does not match expectation");
    }

}
