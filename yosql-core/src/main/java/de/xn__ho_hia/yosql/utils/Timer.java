package de.xn__ho_hia.yosql.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to time how long certain executions take.
 */
public final class Timer {

    private static final Logger         LOG     = LoggerFactory.getLogger("yosql.timer"); //$NON-NLS-1$

    private final Map<String, Duration> timings = new LinkedHashMap<>();

    /**
     * Creates a new timer.
     */
    @Inject
    public Timer() {
        // required by dagger
    }

    /**
     * @param taskName
     *            The name of the task to run.
     * @param task
     *            The task to run.
     */
    public void timed(final String taskName, final Runnable task) {
        if (LOG.isInfoEnabled()) {
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
        if (LOG.isInfoEnabled()) {
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
        if (LOG.isInfoEnabled()) {
            long totalRuntime = 0;
            for (final Entry<String, Duration> entry : timings.entrySet()) {
                final Long runtimeInMilliseconds = Long.valueOf(entry.getValue().toMillis());
                LOG.info("Time spent running [{}]: {} (ms)", entry.getKey(), runtimeInMilliseconds); //$NON-NLS-1$
                totalRuntime += runtimeInMilliseconds.longValue();
            }
            LOG.info("Total runtime: {} (ms)", Long.valueOf(totalRuntime)); //$NON-NLS-1$
        }
    }

}
