/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.orchestration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.logging.LoggingObjectMother;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Errors are recorded from several threads at once — the file walk is parallel, and so is writing
 * types out.
 *
 * <p>A dropped error is not merely a missing line in a log. The orchestrator asks at each stage
 * boundary whether anything failed, and an answer of "no" lets the build carry on and write files
 * from input that did not parse.</p>
 */
@DisplayName("ExecutionErrors under concurrency")
class ExecutionErrorsConcurrencyTest {

    private static final int ERRORS = 10_000;

    @Test
    @DisplayName("every error recorded in parallel is kept")
    void shouldKeepEveryErrorAddedInParallel() {
        final var errors = new ExecutionErrors(LoggingObjectMother.logger());

        IntStream.range(0, ERRORS).parallel()
                .forEach(index -> errors.add(new IllegalStateException("error " + index)));

        assertAll(
                () -> assertTrue(errors.hasErrors(), "errors were recorded"),
                () -> assertEquals(ERRORS, errors.count(), "no error was dropped"));
    }

}
