/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.orchestration;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.model.internal.ApplicationEvents;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Default implementation of a {@link Timer}.
 */
final class DefaultTimer implements Timer {

    private final Map<String, Duration> timings = new LinkedHashMap<>();
    private final LocLogger logger;

    DefaultTimer(final LocLogger logger) {
        this.logger = logger;
    }

    @Override
    public void timed(final String taskName, final Runnable task) {
        if (logger.isInfoEnabled()) {
            final var preRun = Instant.now();
            task.run();
            final var postRun = Instant.now();
            timings.put(taskName, Duration.between(preRun, postRun));
        } else {
            task.run();
        }
    }

    @Override
    public <T> T timed(final String taskName, final Supplier<T> supplier) {
        if (logger.isInfoEnabled()) {
            final var preRun = Instant.now();
            final var value = supplier.get();
            final var postRun = Instant.now();
            timings.put(taskName, Duration.between(preRun, postRun));
            return value;
        }
        return supplier.get();
    }

    @Override
    public void printTimings() {
        if (logger.isInfoEnabled()) {
            Duration totalRuntime = Duration.ofMillis(0);
            for (final var entry : timings.entrySet()) {
                final var runtimeInMilliseconds = entry.getValue();
                logger.info(ApplicationEvents.TASK_RUNTIME, entry.getKey(), runtimeInMilliseconds);
                totalRuntime = totalRuntime.plus(runtimeInMilliseconds);
            }
            logger.info(ApplicationEvents.APPLICATION_RUNTIME, totalRuntime);
        }
        timings.clear();
    }

}
