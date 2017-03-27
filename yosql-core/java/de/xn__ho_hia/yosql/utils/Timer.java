package de.xn__ho_hia.yosql.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

/**
 * Utility class to time how long certain executions take.
 */
@SuppressWarnings("nls")
public final class Timer {

    /**
     * @param taskName
     *            The name of the task to run.
     * @param task
     *            The task to run.
     */
    public static void timed(final String taskName, final Runnable task) {
        final Instant preRun = Instant.now();
        task.run();
        final Instant postRun = Instant.now();
        final String message = String.format("Time spent running [%s]: %s (ms)",
                taskName, Long.valueOf(Duration.between(preRun, postRun).toMillis()));
        System.out.println(message);
    }

    /**
     * @param taskName
     *            The name of the task to run.
     * @param supplier
     *            The supplier to execute.
     * @return The value provided by the supplier.
     */
    public static <T> T timed(final String taskName, final Supplier<T> supplier) {
        final Instant preRun = Instant.now();
        final T value = supplier.get();
        final Instant postRun = Instant.now();
        final String message = String.format("Time spent running [%s]: %s (ms)",
                taskName, Long.valueOf(Duration.between(preRun, postRun).toMillis()));
        System.out.println(message);
        return value;
    }

}
