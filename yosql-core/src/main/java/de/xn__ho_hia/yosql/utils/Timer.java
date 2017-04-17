package de.xn__ho_hia.yosql.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import javax.inject.Inject;

import org.slf4j.cal10n.LocLogger;

import de.xn__ho_hia.yosql.dagger.LoggerModule;
import de.xn__ho_hia.yosql.model.ApplicationEvents;

/**
 * Utility class to time how long certain executions take.
 */
public final class Timer {

    private final Map<String, Duration> timings = new LinkedHashMap<>();
    private final LocLogger             logger;

    /**
     * Creates a new timer.
     *
     * @param logger
     *            The logger to use.
     */
    @Inject
    public Timer(final @LoggerModule.Timer LocLogger logger) {
        this.logger = logger;
    }

    /**
     * @param taskName
     *            The name of the task to run.
     * @param task
     *            The task to run.
     */
    public void timed(final String taskName, final Runnable task) {
        if (logger.isInfoEnabled()) {
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
        if (logger.isInfoEnabled()) {
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
        if (logger.isInfoEnabled()) {
            long totalRuntime = 0;
            for (final Entry<String, Duration> entry : timings.entrySet()) {
                final Long runtimeInMilliseconds = Long.valueOf(entry.getValue().toMillis());
                logger.info(ApplicationEvents.TASK_RUNTIME, entry.getKey(), runtimeInMilliseconds);
                totalRuntime += runtimeInMilliseconds.longValue();
            }
            logger.info(ApplicationEvents.APPLICATION_RUNTIME, Long.valueOf(totalRuntime));
        }
    }

}
