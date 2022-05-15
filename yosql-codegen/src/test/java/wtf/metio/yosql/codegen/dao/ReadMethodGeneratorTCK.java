/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.testing.configs.SqlConfigurations;

/**
 * Verifies that {@link ReadMethodGenerator}s work correctly.
 */
public interface ReadMethodGeneratorTCK {

    /**
     * @return A new {@link ReadMethodGenerator}.
     */
    ReadMethodGenerator generator();

    /**
     * @return The expected generated code for a read single method.
     */
    String readSingleMethodExpectation();

    /**
     * @return The expected generated code for a read multiple method.
     */
    String readMultipleMethodExpectation();

    /**
     * @return The expected generated code for a read cursor method.
     */
    String readCursorMethodExpectation();

    /**
     * @return The expected generated code for a read none method.
     */
    String readNoneMethodExpectation();

    @Test
    default void readSingleMethod() {
        Assertions.assertEquals(
                readSingleMethodExpectation(),
                generator().readMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withReturningMode(ReturningMode.SINGLE), SqlConfigurations.sqlStatement()).toString(),
                "The generated read single method does not match expectation");
    }

    @Test
    default void readMultipleMethod() {
        Assertions.assertEquals(
                readMultipleMethodExpectation(),
                generator().readMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withReturningMode(ReturningMode.MULTIPLE), SqlConfigurations.sqlStatement()).toString(),
                "The generated read multiple method does not match expectation");
    }

    @Test
    default void readCursorMethod() {
        Assertions.assertEquals(
                readCursorMethodExpectation(),
                generator().readMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withReturningMode(ReturningMode.CURSOR), SqlConfigurations.sqlStatement()).toString(),
                "The generated read cursor method does not match expectation");
    }

    @Test
    default void readNoneMethod() {
        Assertions.assertEquals(
                readNoneMethodExpectation(),
                generator().readMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withReturningMode(ReturningMode.NONE), SqlConfigurations.sqlStatement()).toString(),
                "The generated read none method does not match expectation");
    }

}
