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
 * Verifies that {@link WriteMethodGenerator}s work correctly.
 */
public abstract class WriteMethodGeneratorTCK {

    /**
     * @return A new {@link WriteMethodGenerator}.
     */
    abstract WriteMethodGenerator generator();

    /**
     * @return The expected generated code for a write method that returns no results.
     */
    abstract String writeMethodReturningNoneExpectation();

    /**
     * @return The expected generated code for a write method that returns a single result.
     */
    abstract String writeMethodReturningSingleExpectation();

    /**
     * @return The expected generated code for a write method that returns multiple results.
     */
    abstract String writeMethodReturningMultipleExpectation();

    /**
     * @return The expected generated code for a write method that returns a cursor.
     */
    abstract String writeMethodReturningCursorExpectation();

    /**
     * @return The expected generated code for a batch write method.
     */
    abstract String batchWriteMethodExpectation();

    @Test
    final void writeReturningNoneMethod() {
        Assertions.assertEquals(
                writeMethodReturningNoneExpectation(),
                generator().writeMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withReturningMode(ReturningMode.NONE), SqlConfigurations.sqlStatement()).toString(),
                "The generated write method does not match expectation");
    }

    @Test
    final void writeReturningSingleMethod() {
        Assertions.assertEquals(
                writeMethodReturningSingleExpectation(),
                generator().writeMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withReturningMode(ReturningMode.SINGLE), SqlConfigurations.sqlStatement()).toString(),
                "The generated write method does not match expectation");
    }

    @Test
    final void writeReturningMultipleMethod() {
        Assertions.assertEquals(
                writeMethodReturningMultipleExpectation(),
                generator().writeMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withReturningMode(ReturningMode.MULTIPLE), SqlConfigurations.sqlStatement()).toString(),
                "The generated write method does not match expectation");
    }

    @Test
    final void writeReturningCursorMethod() {
        Assertions.assertEquals(
                writeMethodReturningCursorExpectation(),
                generator().writeMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withReturningMode(ReturningMode.CURSOR), SqlConfigurations.sqlStatement()).toString(),
                "The generated write method does not match expectation");
    }

    @Test
    final void batchWriteMethod() {
        Assertions.assertEquals(
                batchWriteMethodExpectation(),
                generator().batchWriteMethod(SqlConfigurations.sqlConfiguration(), SqlConfigurations.sqlStatement()).toString(),
                "The generated batch write method does not match expectation");
    }

}
