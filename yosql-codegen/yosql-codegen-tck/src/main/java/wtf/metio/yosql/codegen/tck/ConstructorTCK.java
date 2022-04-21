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
import wtf.metio.yosql.testing.configs.Sql;

/**
 * Verifies that {@link ConstructorGenerator}s work correctly.
 */
public interface ConstructorTCK {

    ConstructorGenerator generator();

    String forRepositoryExpectation();

    @Test
    default void forRepository() {
        Assertions.assertEquals(
                forRepositoryExpectation(),
                generator().forRepository(Sql.sqlStatements()).toString(),
                "The generated constructor did not match expectation");
    }

}
