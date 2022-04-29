/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.tck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.api.BlockingMethodGenerator;
import wtf.metio.yosql.models.constants.sql.ReturningMode;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.testing.configs.SqlConfigurations;

/**
 * Verifies that {@link BlockingMethodGenerator}s work correctly.
 */
public interface BlockingMethodGeneratorTCK {

    /**
     * @return A new {@link BlockingMethodGenerator}.
     */
    BlockingMethodGenerator generator();

    /**
     * @return The expected generated code for a blocking read one method.
     */
    String blockingReadOneMethodExpectation();

    /**
     * @return The expected generated code for a blocking read first method.
     */
    String blockingReadFirstMethodExpectation();

    /**
     * @return The expected generated code for a blocking read list method.
     */
    String blockingReadListMethodExpectation();

    /**
     * @return The expected generated code for a blocking write method.
     */
    String blockingWriteMethodExpectation();

    /**
     * @return The expected generated code for a blocking call method.
     */
    String blockingCallMethodExpectation();

    @Test
    default void blockingReadOneMethod() {
        Assertions.assertEquals(
                blockingReadOneMethodExpectation(),
                generator().blockingReadMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withReturningMode(ReturningMode.ONE), SqlConfigurations.sqlStatements()).toString(),
                "The generated blocking read one method did not match expectation");
    }

    @Test
    default void blockingReadFirstMethod() {
        Assertions.assertEquals(
                blockingReadFirstMethodExpectation(),
                generator().blockingReadMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withReturningMode(ReturningMode.FIRST), SqlConfigurations.sqlStatements()).toString(),
                "The generated blocking read first method did not match expectation");
    }

    @Test
    default void blockingReadListMethod() {
        Assertions.assertEquals(
                blockingReadListMethodExpectation(),
                generator().blockingReadMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withReturningMode(ReturningMode.LIST), SqlConfigurations.sqlStatements()).toString(),
                "The generated blocking read list method did not match expectation");
    }

    @Test
    default void blockingWriteMethod() {
        Assertions.assertEquals(
                blockingWriteMethodExpectation(),
                generator().blockingWriteMethod(SqlConfigurations.sqlConfiguration(), SqlConfigurations.sqlStatements()).toString(),
                "The generated blocking write method did not match expectation");
    }

    @Test
    default void blockingCallMethod() {
        Assertions.assertEquals(
                blockingCallMethodExpectation(),
                generator().blockingCallMethod(SqlConfigurations.sqlConfiguration(), SqlConfigurations.sqlStatements()).toString(),
                "The generated blocking call method did not match expectation");
    }

}
