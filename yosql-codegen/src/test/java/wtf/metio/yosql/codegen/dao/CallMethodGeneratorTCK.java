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
 * Verifies that {@link CallMethodGenerator}s work correctly.
 */
public abstract class CallMethodGeneratorTCK {

    /**
     * @return A new {@link CallMethodGenerator}.
     */
    abstract CallMethodGenerator generator();

    /**
     * @return The expected generated code for a call method that returns a single result.
     */
    abstract String callSingleMethodExpectation();

    /**
     * @return The expected generated code for a call method that returns a single result and uses a given connection.
     */
    abstract String callSingleMethodWithGivenConnectionExpectation();

    /**
     * @return The expected generated code for a call method that returns a multiple results.
     */
    abstract String callMultipleMethodExpectation();

    /**
     * @return The expected generated code for a call method that returns a multiple results and uses a given connection.
     */
    abstract String callMultipleMethodWithGivenConnectionExpectation();

    /**
     * @return The expected generated code for a call method that returns a cursor.
     */
    abstract String callCursorMethodExpectation();

    /**
     * @return The expected generated code for a call method that returns a cursor and uses a given connection.
     */
    abstract String callCursorMethodWithGivenConnectionExpectation();

    /**
     * @return The expected generated code for a call method that returns no results.
     */
    abstract String callNoneMethodExpectation();

    /**
     * @return The expected generated code for a call method that returns no results and uses a given connection.
     */
    abstract String callNoneMethodWithGivenConnectionExpectation();

    @Test
    final void callNoneMethod() {
        Assertions.assertEquals(
                callNoneMethodExpectation(),
                generator().callMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.CALLING)
                        .withReturningMode(ReturningMode.NONE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(true), SqlConfigurations.sqlStatement()).toString(),
                "The generated call none method does not match expectation");
    }

    @Test
    final void callNoneMethodWithGivenConnection() {
        Assertions.assertEquals(
                callNoneMethodWithGivenConnectionExpectation(),
                generator().callMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.CALLING)
                        .withReturningMode(ReturningMode.NONE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(false), SqlConfigurations.sqlStatement()).toString(),
                "The generated call none method does not match expectation");
    }

    @Test
    final void callSingleMethod() {
        Assertions.assertEquals(
                callSingleMethodExpectation(),
                generator().callMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.CALLING)
                        .withReturningMode(ReturningMode.SINGLE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(true), SqlConfigurations.sqlStatement()).toString(),
                "The generated call single method does not match expectation");
    }

    @Test
    final void callSingleMethodWithGivenConnection() {
        Assertions.assertEquals(
                callSingleMethodWithGivenConnectionExpectation(),
                generator().callMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.CALLING)
                        .withReturningMode(ReturningMode.SINGLE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(false), SqlConfigurations.sqlStatement()).toString(),
                "The generated call single method does not match expectation");
    }

    @Test
    final void callMultipleMethod() {
        Assertions.assertEquals(
                callMultipleMethodExpectation(),
                generator().callMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.CALLING)
                        .withReturningMode(ReturningMode.MULTIPLE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(true), SqlConfigurations.sqlStatement()).toString(),
                "The generated call multiple method does not match expectation");
    }

    @Test
    final void callMultipleMethodWithGivenConnection() {
        Assertions.assertEquals(
                callMultipleMethodWithGivenConnectionExpectation(),
                generator().callMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.CALLING)
                        .withReturningMode(ReturningMode.MULTIPLE)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(false), SqlConfigurations.sqlStatement()).toString(),
                "The generated call multiple method does not match expectation");
    }

    @Test
    final void callCursorMethod() {
        Assertions.assertEquals(
                callCursorMethodExpectation(),
                generator().callMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.CALLING)
                        .withReturningMode(ReturningMode.CURSOR)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(true), SqlConfigurations.sqlStatement()).toString(),
                "The generated call cursor method does not match expectation");
    }

    @Test
    final void callCursorMethodWithGivenConnection() {
        Assertions.assertEquals(
                callCursorMethodWithGivenConnectionExpectation(),
                generator().callMethod(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withType(SqlStatementType.CALLING)
                        .withReturningMode(ReturningMode.CURSOR)
                        .withUsePreparedStatement(true)
                        .withCreateConnection(false), SqlConfigurations.sqlStatement()).toString(),
                "The generated call cursor method does not match expectation");
    }

}
