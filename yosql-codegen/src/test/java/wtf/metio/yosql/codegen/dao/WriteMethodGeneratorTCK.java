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
import wtf.metio.yosql.models.configuration.SqlStatementType;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

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
    abstract String writeReturningNoneMethodExpectation();

    /**
     * @return The expected generated code for a write method that returns no results and uses a given connection.
     */
    abstract String writeReturningNoneWithGivenConnectionExpectation();

    /**
     * @return The expected generated code for a write method that returns the update count.
     */
    abstract String writeReturningUpdateCountExpectation();

    /**
     * @return The expected generated code for a write method that returns the update count and uses a given connection.
     */
    abstract String writeReturningUpdateCountWithGivenConnectionExpectation();

    /**
     * @return The expected generated code for a write method that returns a single result.
     */
    abstract String writeReturningSingleExpectation();

    /**
     * @return The expected generated code for a write method that returns a single result and uses a given connection.
     */
    abstract String writeReturningSingleWithGivenConnectionExpectation();

    /**
     * @return The expected generated code for a write method that returns multiple results.
     */
    abstract String writeReturningMultipleExpectation();

    /**
     * @return The expected generated code for a write method that returns multiple results and uses a given connection.
     */
    abstract String writeReturningMultipleWithGivenConnectionExpectation();

    /**
     * @return The expected generated code for a write method that returns a cursor.
     */
    abstract String writeReturningCursorExpectation();

    /**
     * @return The expected generated code for a write method that returns a cursor and uses a given connection.
     */
    abstract String writeReturningCursorWithGivenConnectionExpectation();

    /**
     * @return The expected generated code for a batch write method.
     */
    abstract String batchWriteExpectation();

    /**
     * @return The expected generated code for a batch write method which uses a given connection.
     */
    abstract String batchWriteWithGivenConnectionExpectation();

    @Test
    final void writeReturningNone() {
        Assertions.assertEquals(
                writeReturningNoneMethodExpectation(),
                generator().writeMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.WRITING)
                        .withReturningMode(ReturningMode.NONE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(true), SqlConfigurations.sqlStatement()).toString(),
                "The generated write method does not match expectation");
    }

    @Test
    final void writeReturningNoneWithGivenConnection() {
        Assertions.assertEquals(
                writeReturningNoneWithGivenConnectionExpectation(),
                generator().writeMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.WRITING)
                        .withReturningMode(ReturningMode.NONE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(false), SqlConfigurations.sqlStatement()).toString(),
                "The generated write method does not match expectation");
    }

    @Test
    final void writeReturningUpdateCount() {
        Assertions.assertEquals(
                writeReturningUpdateCountExpectation(),
                generator().writeMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.WRITING)
                        .withReturningMode(ReturningMode.NONE)
                        .withUsePreparedStatement(true)
                        .withWritesReturnUpdateCount(true)
                        .withCreateConnection(true), SqlConfigurations.sqlStatement()).toString(),
                "The generated write method does not match expectation");
    }

    @Test
    final void writeReturningUpdateCountWithGivenConnection() {
        Assertions.assertEquals(
                writeReturningUpdateCountWithGivenConnectionExpectation(),
                generator().writeMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.WRITING)
                        .withReturningMode(ReturningMode.NONE)
                        .withUsePreparedStatement(true)
                        .withWritesReturnUpdateCount(true)
                        .withCreateConnection(false), SqlConfigurations.sqlStatement()).toString(),
                "The generated write method does not match expectation");
    }

    @Test
    final void writeReturningSingle() {
        Assertions.assertEquals(
                writeReturningSingleExpectation(),
                generator().writeMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.WRITING)
                        .withReturningMode(ReturningMode.SINGLE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(true), SqlConfigurations.sqlStatement()).toString(),
                "The generated write method does not match expectation");
    }

    @Test
    final void writeReturningSingleWithGivenConnection() {
        Assertions.assertEquals(
                writeReturningSingleWithGivenConnectionExpectation(),
                generator().writeMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.WRITING)
                        .withReturningMode(ReturningMode.SINGLE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(false), SqlConfigurations.sqlStatement()).toString(),
                "The generated write method does not match expectation");
    }

    @Test
    final void writeReturningMultiple() {
        Assertions.assertEquals(
                writeReturningMultipleExpectation(),
                generator().writeMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.WRITING)
                        .withReturningMode(ReturningMode.MULTIPLE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(true), SqlConfigurations.sqlStatement()).toString(),
                "The generated write method does not match expectation");
    }

    @Test
    final void writeReturningMultipleWithGivenConnection() {
        Assertions.assertEquals(
                writeReturningMultipleWithGivenConnectionExpectation(),
                generator().writeMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.WRITING)
                        .withReturningMode(ReturningMode.MULTIPLE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(false), SqlConfigurations.sqlStatement()).toString(),
                "The generated write method does not match expectation");
    }

    @Test
    final void writeReturningCursor() {
        Assertions.assertEquals(
                writeReturningCursorExpectation(),
                generator().writeMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.WRITING)
                        .withReturningMode(ReturningMode.CURSOR)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(true), SqlConfigurations.sqlStatement()).toString(),
                "The generated write method does not match expectation");
    }

    @Test
    final void writeReturningCursorWithGivenConnection() {
        Assertions.assertEquals(
                writeReturningCursorWithGivenConnectionExpectation(),
                generator().writeMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.WRITING)
                        .withReturningMode(ReturningMode.CURSOR)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(false), SqlConfigurations.sqlStatement()).toString(),
                "The generated write method does not match expectation");
    }

    @Test
    final void batchWrite() {
        Assertions.assertEquals(
                batchWriteExpectation(),
                generator().batchWriteMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.WRITING)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(true), SqlConfigurations.sqlStatement()).toString(),
                "The generated batch write method does not match expectation");
    }

    @Test
    final void batchWriteWithGivenConnection() {
        Assertions.assertEquals(
                batchWriteWithGivenConnectionExpectation(),
                generator().batchWriteMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.WRITING)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(false), SqlConfigurations.sqlStatement()).toString(),
                "The generated batch write method does not match expectation");
    }

}
