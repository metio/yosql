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
import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

/**
 * Verifies that {@link ReadMethodGenerator}s work correctly.
 */
public abstract class ReadMethodGeneratorTCK {

    /**
     * @return A new {@link ReadMethodGenerator}.
     */
    abstract ReadMethodGenerator generator();

    /**
     * @return The expected generated code for a read single method.
     */
    abstract String readSingleMethodExpectation();

    /**
     * @return The expected generated code for a read multiple method.
     */
    abstract String readMultipleMethodExpectation();

    /**
     * @return The expected generated code for a read cursor method.
     */
    abstract String readCursorMethodExpectation();

    /**
     * @return The expected generated code for a read none method.
     */
    abstract String readNoneMethodExpectation();

    @Test
    final void readSingleMethod() {
        Assertions.assertEquals(
                readSingleMethodExpectation(),
                generator().readMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withReturningMode(ReturningMode.SINGLE), SqlConfigurations.sqlStatement()).toString(),
                "The generated read single method does not match expectation");
    }

    @Test
    final void readMultipleMethod() {
        Assertions.assertEquals(
                readMultipleMethodExpectation(),
                generator().readMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withReturningMode(ReturningMode.MULTIPLE), SqlConfigurations.sqlStatement()).toString(),
                "The generated read multiple method does not match expectation");
    }

    @Test
    final void readCursorMethod() {
        Assertions.assertEquals(
                readCursorMethodExpectation(),
                generator().readMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withReturningMode(ReturningMode.CURSOR), SqlConfigurations.sqlStatement()).toString(),
                "The generated read cursor method does not match expectation");
    }

    @Test
    final void readNoneMethod() {
        Assertions.assertEquals(
                readNoneMethodExpectation(),
                generator().readMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withReturningMode(ReturningMode.NONE), SqlConfigurations.sqlStatement()).toString(),
                "The generated read none method does not match expectation");
    }

}
