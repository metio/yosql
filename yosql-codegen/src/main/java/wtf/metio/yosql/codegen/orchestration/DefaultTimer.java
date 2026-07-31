/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.codegen.orchestration;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.lifecycle.TimerLifecycle;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Default implementation of a {@link Timer}.
 */
public final class DefaultTimer implements Timer {

    private final Map<String, Duration> timings = new LinkedHashMap<>();
    private final LocLogger logger;

    public DefaultTimer(final LocLogger logger) {
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
            final var total = timings.entrySet().stream()
                    .peek(entry -> logger.info(TimerLifecycle.TASK_RUNTIME, entry.getKey(), entry.getValue().toMillis()))
                    .map(Map.Entry::getValue)
                    .reduce(Duration.ofMillis(0), Duration::plus);
            logger.info(TimerLifecycle.APPLICATION_RUNTIME, total.toMillis());
        }
        timings.clear();
    }

}
