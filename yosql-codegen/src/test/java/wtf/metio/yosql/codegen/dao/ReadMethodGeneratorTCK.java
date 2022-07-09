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
 * Verifies that {@link ReadMethodGenerator}s work correctly.
 */
public abstract class ReadMethodGeneratorTCK {

    /**
     * @return A new {@link ReadMethodGenerator}.
     */
    abstract ReadMethodGenerator generator();

    /**
     * @return The expected generated code for a read none method.
     */
    abstract String readNoneExpectation();

    /**
     * @return The expected generated code for a read none method which uses a given connection.
     */
    abstract String readNoneWithGivenConnectionExpectation();

    /**
     * @return The expected generated code for a read single method.
     */
    abstract String readSingleExpectation();

    /**
     * @return The expected generated code for a read single method which uses a given connection.
     */
    abstract String readSingleWithGivenConnectionExpectation();

    /**
     * @return The expected generated code for a read multiple method.
     */
    abstract String readMultipleExpectation();

    /**
     * @return The expected generated code for a read multiple method which uses a given connection.
     */
    abstract String readMultipleWithGivenConnectionExpectation();

    /**
     * @return The expected generated code for a read cursor method.
     */
    abstract String readCursorExpectation();

    /**
     * @return The expected generated code for a read cursor method which uses a given connection.
     */
    abstract String readCursorWithGivenConnectionExpectation();

    @Test
    final void readNone() {
        Assertions.assertEquals(
                readNoneExpectation(),
                generator().readMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.READING)
                        .withReturningMode(ReturningMode.NONE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(true), SqlConfigurations.sqlStatement()).toString(),
                "The generated read none method does not match expectation");
    }

    @Test
    final void readNoneWithGivenConnection() {
        Assertions.assertEquals(
                readNoneWithGivenConnectionExpectation(),
                generator().readMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.READING)
                        .withReturningMode(ReturningMode.NONE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(false), SqlConfigurations.sqlStatement()).toString(),
                "The generated read none method does not match expectation");
    }

    @Test
    final void readSingle() {
        Assertions.assertEquals(
                readSingleExpectation(),
                generator().readMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.READING)
                        .withReturningMode(ReturningMode.SINGLE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(true), SqlConfigurations.sqlStatement()).toString(),
                "The generated read single method does not match expectation");
    }

    @Test
    final void readSingleWithGivenConnection() {
        Assertions.assertEquals(
                readSingleWithGivenConnectionExpectation(),
                generator().readMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.READING)
                        .withReturningMode(ReturningMode.SINGLE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(false), SqlConfigurations.sqlStatement()).toString(),
                "The generated read single method does not match expectation");
    }

    @Test
    final void readMultiple() {
        Assertions.assertEquals(
                readMultipleExpectation(),
                generator().readMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.READING)
                        .withReturningMode(ReturningMode.MULTIPLE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(true), SqlConfigurations.sqlStatement()).toString(),
                "The generated read multiple method does not match expectation");
    }

    @Test
    final void readMultipleWithGivenConnection() {
        Assertions.assertEquals(
                readMultipleWithGivenConnectionExpectation(),
                generator().readMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.READING)
                        .withReturningMode(ReturningMode.MULTIPLE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(false), SqlConfigurations.sqlStatement()).toString(),
                "The generated read multiple method does not match expectation");
    }

    @Test
    final void readCursor() {
        Assertions.assertEquals(
                readCursorExpectation(),
                generator().readMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.READING)
                        .withReturningMode(ReturningMode.CURSOR)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(true), SqlConfigurations.sqlStatement()).toString(),
                "The generated read cursor method does not match expectation");
    }

    @Test
    final void readCursorWithGivenConnection() {
        Assertions.assertEquals(
                readCursorWithGivenConnectionExpectation(),
                generator().readMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.READING)
                        .withReturningMode(ReturningMode.CURSOR)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(false), SqlConfigurations.sqlStatement()).toString(),
                "The generated read cursor method does not match expectation");
    }

}
