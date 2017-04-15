package de.xn__ho_hia.yosql.utils;

import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

import javax.inject.Inject;

/**
 * Utility class to time how long certain executions take.
 */
public final class Timer {

    private final PrintStream out;

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
            final String message = String.format("Time spent running [%s]: %s (ms)", //$NON-NLS-1$
                    taskName, Long.valueOf(Duration.between(preRun, postRun).toMillis()));
            out.println(message);
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
            final String message = String.format("Time spent running [%s]: %s (ms)", //$NON-NLS-1$
                    taskName, Long.valueOf(Duration.between(preRun, postRun).toMillis()));
            out.println(message);
            return value;
        }
        return supplier.get();
    }

}
