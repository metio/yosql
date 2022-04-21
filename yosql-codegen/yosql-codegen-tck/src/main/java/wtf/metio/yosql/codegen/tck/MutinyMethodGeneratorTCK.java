/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.tck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.api.MutinyMethodGenerator;
import wtf.metio.yosql.testing.configs.Sql;

/**
 * Verifies that {@link MutinyMethodGenerator}s work correctly.
 */
public interface MutinyMethodGeneratorTCK {

    /**
     * @return A new {@link MutinyMethodGenerator}.
     */
    MutinyMethodGenerator generator();

    /**
     * @return The expected generated code for a Mutiny read method.
     */
    String mutinyReadMethodExpectation();

    @Test
    default void mutinyReadMethod() {
        Assertions.assertEquals(
                mutinyReadMethodExpectation(),
                generator().mutinyReadMethod(Sql.sqlConfiguration(), Sql.sqlStatements()).toString(),
                "The generated Mutiny read method did not match expectation");
    }

}
