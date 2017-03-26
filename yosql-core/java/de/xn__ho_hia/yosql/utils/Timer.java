package de.xn__ho_hia.yosql.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public final class Timer {

	public static void timed(final String taskName, final Runnable task) {
        final Instant preRun = Instant.now();
        task.run();
        final Instant postRun = Instant.now();
        final String message = String.format("Time spent running [%s]: %s (ms)",
                taskName, Duration.between(preRun, postRun).toMillis());
        System.out.println(message);
    }

	public static <T> T timed(final String taskName, final Supplier<T> supplier) {
        final Instant preRun = Instant.now();
        final T value = supplier.get();
        final Instant postRun = Instant.now();
        final String message = String.format("Time spent running [%s]: %s (ms)",
                taskName, Duration.between(preRun, postRun).toMillis());
        System.out.println(message);
        return value;
    }

}
