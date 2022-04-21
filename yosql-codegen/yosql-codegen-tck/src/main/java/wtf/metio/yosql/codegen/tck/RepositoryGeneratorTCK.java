/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.tck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.api.RepositoryGenerator;
import wtf.metio.yosql.testing.configs.Sql;

/**
 * Verifies that {@link RepositoryGenerator}s work correctly.
 */
public interface RepositoryGeneratorTCK {

    /**
     * @return A new {@link RepositoryGenerator}.
     */
    RepositoryGenerator generator();

    /**
     * @return The expected generated code for a generated repository class.
     */
    String repositoryExpectation();

    @Test
    default void generateRepository() {
        Assertions.assertEquals(
                repositoryExpectation(),
                generator().generateRepository("Test", Sql.sqlStatements()).getType().toString(),
                "The generated repository did not match expectation");
    }

}
