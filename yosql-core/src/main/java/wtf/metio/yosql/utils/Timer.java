/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.utils;

import wtf.metio.yosql.model.ApplicationEvents;

import org.slf4j.cal10n.LocLogger;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

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
    public Timer(final @wtf.metio.yosql.model.annotations.Timer LocLogger logger) {
        this.logger = logger;
    }

    /**
     * Measures the execution time of a single runnable task.
     *
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
     * Measures the execution time of a single supplier.
     *
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
        timings.clear();
    }

}
