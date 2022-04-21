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
import wtf.metio.yosql.testing.configs.Sql;

/**
 * Verifies that {@link BlockingMethodGenerator}s work correctly.
 */
public interface BlockingMethodGeneratorTCK {

    /**
     * @return A new {@link BlockingMethodGenerator}.
     */
    BlockingMethodGenerator generator();

    /**
     * @return The expected generated code for a blocking read method.
     */
    String blockingReadMethodExpectation();

    /**
     * @return The expected generated code for a blocking write method.
     */
    String blockingWriteMethodExpectation();

    /**
     * @return The expected generated code for a blocking call method.
     */
    String blockingCallMethodExpectation();

    @Test
    default void blockingReadMethod() {
        Assertions.assertEquals(
                blockingReadMethodExpectation(),
                generator().blockingReadMethod(Sql.sqlConfiguration(), Sql.sqlStatements()).toString(),
                "The generated blocking read method did not match expectation");
    }

    @Test
    default void blockingWriteMethod() {
        Assertions.assertEquals(
                blockingWriteMethodExpectation(),
                generator().blockingWriteMethod(Sql.sqlConfiguration(), Sql.sqlStatements()).toString(),
                "The generated blocking write method did not match expectation");
    }

    @Test
    default void blockingCallMethod() {
        Assertions.assertEquals(
                blockingCallMethodExpectation(),
                generator().blockingCallMethod(Sql.sqlConfiguration(), Sql.sqlStatements()).toString(),
                "The generated blocking call method did not match expectation");
    }

}
