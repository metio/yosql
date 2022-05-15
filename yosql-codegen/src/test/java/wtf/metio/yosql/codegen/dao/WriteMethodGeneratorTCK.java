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
 * Verifies that {@link WriteMethodGenerator}s work correctly.
 */
public interface WriteMethodGeneratorTCK {

    /**
     * @return A new {@link WriteMethodGenerator}.
     */
    WriteMethodGenerator generator();

    /**
     * @return The expected generated code for a standard write method.
     */
    String writeMethodExpectation();

    /**
     * @return The expected generated code for a batch write method.
     */
    String batchWriteMethodExpectation();

    @Test
    default void writeMethod() {
        Assertions.assertEquals(
                writeMethodExpectation(),
                generator().writeMethod(SqlConfigurations.sqlConfiguration(), SqlConfigurations.sqlStatement()).toString(),
                "The generated write method does not match expectation");
    }

    @Test
    default void batchWriteMethod() {
        Assertions.assertEquals(
                batchWriteMethodExpectation(),
                generator().batchWriteMethod(SqlConfigurations.sqlConfiguration(), SqlConfigurations.sqlStatement()).toString(),
                "The generated batch write method does not match expectation");
    }

}
