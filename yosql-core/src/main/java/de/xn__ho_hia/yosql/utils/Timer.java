package de.xn__ho_hia.yosql.utils;

import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import javax.inject.Inject;

/**
 * Utility class to time how long certain executions take.
 */
public final class Timer {

    private final PrintStream           out;
    private final Map<String, Duration> timings = new LinkedHashMap<>();

    /**
     * @param out
     *            The output stream to use.
     */
    @Inject
    public Timer(final PrintStream out) {
        this.out = out;
    }

    /**
     * @param taskName
     *            The name of the task to run.
     * @param task
     *            The task to run.
     */
    public void timed(final String taskName, final Runnable task) {
        if (out != null) {
            final Instant preRun = Instant.now();
            task.run();
            final Instant postRun = Instant.now();
            timings.put(taskName, Duration.between(preRun, postRun));
        } else {
            task.run();
        }
    }

    /**
     * @param taskName
     *            The name of the task to run.
     * @param supplier
     *            The supplier to execute.
     * @return The value provided by the supplier.
     */
    public <T> T timed(final String taskName, final Supplier<T> supplier) {
        if (out != null) {
            final Instant preRun = Instant.now();
            final T value = supplier.get();
            final Instant postRun = Instant.now();
            timings.put(taskName, Duration.between(preRun, postRun));
            return value;
        }
        return supplier.get();
    }

    /**
     * Prints the previously recorded timings.
     */
    public void printTimings() {
        long totalRuntime = 0;
        for (final Entry<String, Duration> entry : timings.entrySet()) {
            final Long runtimeInMilliseconds = Long.valueOf(entry.getValue().toMillis());
            final String message = String.format("Time spent running [%s]: %s (ms)", //$NON-NLS-1$
                    entry.getKey(), runtimeInMilliseconds);
            out.println(message);
            totalRuntime += runtimeInMilliseconds.longValue();
        }
        out.println(String.format("Total runtime: %s (ms)", Long.valueOf(totalRuntime))); //$NON-NLS-1$
    }

}
