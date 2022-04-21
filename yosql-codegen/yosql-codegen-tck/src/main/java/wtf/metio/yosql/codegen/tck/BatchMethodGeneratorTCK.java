/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.tck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.api.BatchMethodGenerator;
import wtf.metio.yosql.testing.configs.Sql;

/**
 * Verifies that {@link BatchMethodGenerator}s work correctly.
 */
public interface BatchMethodGeneratorTCK {

    /**
     * @return A new {@link BatchMethodGenerator}.
     */
    BatchMethodGenerator generator();

    /**
     * @return The expected generated code for a batch write method.
     */
    String batchWriteMethodExpectation();

    @Test
    default void batchWriteMethod() {
        Assertions.assertEquals(
                batchWriteMethodExpectation(),
                generator().batchWriteMethod(Sql.sqlConfiguration(), Sql.sqlStatements()).toString(),
                "The generated batch write method did not match expectation");
    }

}
