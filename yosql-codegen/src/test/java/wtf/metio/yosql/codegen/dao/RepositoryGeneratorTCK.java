/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
