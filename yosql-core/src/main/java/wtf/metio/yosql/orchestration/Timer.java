package wtf.metio.yosql.orchestration;

import java.util.function.Supplier;

/**
 * Utility class to time how long certain executions take.
 */
public interface Timer {

    /**
     * Measures the execution time of a single runnable task.
     *
     * @param taskName
     *            The name of the task to run.
     * @param task
     *            The task to run.
     */
    void timed(String taskName, Runnable task);

    /**
     * Measures the execution time of a single supplier.
     *
     * @param taskName
     *            The name of the task to run.
     * @param supplier
     *            The supplier to execute.
     * @return The value provided by the supplier.
     */
    <T> T timed(String taskName, Supplier<T> supplier);

    /**
     * Prints the previously recorded timings.
     */
    void printTimings();

}
