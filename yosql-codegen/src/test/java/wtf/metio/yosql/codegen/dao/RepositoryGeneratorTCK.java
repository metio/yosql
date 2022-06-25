/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;

/**
 * Verifies that {@link RepositoryGenerator}s work correctly.
 */
public abstract class RepositoryGeneratorTCK {

    /**
     * @return A new {@link RepositoryGenerator}.
     */
    abstract RepositoryGenerator generator();

    /**
     * @return The expected generated code for a generated repository class.
     */
    abstract String repositoryClassExpectation();

    /**
     * @return The expected generated code for a generated repository interface.
     */
    abstract String repositoryInterfaceExpectation();

    @Test
    final void generateRepositoryClass() {
        Assertions.assertEquals(
                repositoryClassExpectation(),
                generator().generateRepositoryClass("Test", SqlConfigurations.sqlStatement()).getType().toString(),
                "The generated repository class does not match expectation");
    }

    @Test
    final void generateRepositoryInterface() {
        Assertions.assertEquals(
                repositoryInterfaceExpectation(),
                generator().generateRepositoryInterface("Test", SqlConfigurations.sqlStatement()).getType().toString(),
                "The generated repository interface does not match expectation");
    }

}
