/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.logging;

import com.palantir.javapoet.ClassName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.models.configuration.GeneratedNames;

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

    /**
     * The line exists to say which vendor was found, so it has to read the variable holding it.
     * Formatting the name with {@code $S} instead of {@code $N} logs the word
     * {@code databaseProductName} on every run, and an expectation written from that output looks
     * every bit as settled as a correct one.
     */
    @Test
    default void vendorDetectedReadsTheDetectedVendor() {
        final var generated = generator().vendorDetected().toString();
        Assumptions.assumeFalse(generated.isBlank(), "a generator that logs nothing has nothing to read");
        Assertions.assertFalse(generated.contains("\"" + GeneratedNames.DATABASE_PRODUCT_NAME + "\""),
                () -> "logs the name of the variable rather than the vendor it holds: " + generated);
        Assertions.assertTrue(generated.contains(GeneratedNames.DATABASE_PRODUCT_NAME),
                () -> "never reads the detected vendor: " + generated);
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
