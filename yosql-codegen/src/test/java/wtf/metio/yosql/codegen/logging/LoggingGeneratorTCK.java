/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.logging;

import com.squareup.javapoet.ClassName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

/**
 * Verifies that {@link LoggingGenerator}s work correctly.
 */
public interface LoggingGeneratorTCK {

    /**
     * @return A new {@link LoggingGenerator}.
     */
    LoggingGenerator generator();

    /**
     * @return The expected generated code for a logger field.
     */
    String loggerExpectation();

    /**
     * @return The expected generated code for an entering log statement.
     */
    String enteringExpectation();

    /**
     * @return The expected generated code for an executing query.
     */
    String executingQueryExpectation();

    /**
     * @return The expected generated code for a picked query.
     */
    String queryPickedExpectation();

    /**
     * @return The expected generated code for a picked index.
     */
    String indexPickedExpectation();

    /**
     * @return The expected generated code for a test whether to log or not.
     */
    String shouldLogExpectation();

    /**
     * @return The expected generated code for a detected vendor.
     */
    String vendorDetectedExpectation();

    /**
     * @return The expected generated code for a picked vendor query.
     */
    String vendorQueryPickedExpectation();

    /**
     * @return The expected generated code for a picked vendor index.
     */
    String vendorIndexPickedExpectation();

    /**
     * @return The expected boolean signaling whether this logging generator is enabled.
     */
    boolean isEnabledExpectation();

    @Test
    default void isEnabled() {
        Assertions.assertEquals(isEnabledExpectation(), generator().isEnabled());
    }

    @Test
    default void logger() {
        final var logger = generator().logger(
                ClassName.get("com.example", "TestRepository"));
        Assumptions.assumeTrue(logger.isPresent());
        Assertions.assertEquals(
                loggerExpectation(),
                logger.get().toString(),
                "The generated code does not match expectation");
    }

    @Test
    default void entering() {
        Assertions.assertEquals(
                enteringExpectation(),
                generator().entering("TestRepository", "queryData").toString(),
                "The generated code does not match expectation");
    }

    @Test
    default void executingQuery() {
        Assertions.assertEquals(
                executingQueryExpectation(),
                generator().executingQuery().toString(),
                "The generated code does not match expectation");
    }

    @Test
    default void queryPicked() {
        Assertions.assertEquals(
                queryPickedExpectation(),
                generator().queryPicked("queryData").toString(),
                "The generated code does not match expectation");
    }

    @Test
    default void indexPicked() {
        Assertions.assertEquals(
                indexPickedExpectation(),
                generator().indexPicked("queryData").toString(),
                "The generated code does not match expectation");
    }

    @Test
    default void shouldLog() {
        Assertions.assertEquals(
                shouldLogExpectation(),
                generator().shouldLog().toString(),
                "The generated code does not match expectation");
    }

    @Test
    default void vendorDetected() {
        Assertions.assertEquals(
                vendorDetectedExpectation(),
                generator().vendorDetected().toString(),
                "The generated code does not match expectation");
    }

    @Test
    default void vendorQueryPicked() {
        Assertions.assertEquals(
                vendorQueryPickedExpectation(),
                generator().vendorQueryPicked("queryData").toString(),
                "The generated code does not match expectation");
    }

    @Test
    default void vendorIndexPicked() {
        Assertions.assertEquals(
                vendorIndexPickedExpectation(),
                generator().vendorIndexPicked("queryData").toString(),
                "The generated code does not match expectation");
    }

}
