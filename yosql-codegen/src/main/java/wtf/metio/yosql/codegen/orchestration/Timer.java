/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.orchestration;

import java.util.function.Supplier;

/**
 * Utility class to time how long certain executions take.
 */
public interface Timer {

    /**
     * Measures the execution time of a single runnable task.
     *
     * @param taskName The name of the task to run.
     * @param task     The task to run.
     */
    void timed(String taskName, Runnable task);

    /**
     * Measures the execution time of a single supplier.
     *
     * @param taskName The name of the task to run.
     * @param supplier The supplier to execute.
     * @return The value provided by the supplier.
     */
    <T> T timed(String taskName, Supplier<T> supplier);

    /**
     * Prints the previously recorded timings and clear them.
     */
    void printTimings();

}
