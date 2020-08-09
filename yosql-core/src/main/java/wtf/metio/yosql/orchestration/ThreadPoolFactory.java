/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.orchestration;

import wtf.metio.yosql.model.configuration.ResourceConfiguration;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * Creates thread pools which are used during parsing/generating/writing.
 */
public final class ThreadPoolFactory {

    private final ForkJoinPool.ForkJoinWorkerThreadFactory threadFactory;
    private final ResourceConfiguration configuration;

    ThreadPoolFactory(
            final ForkJoinPool.ForkJoinWorkerThreadFactory threadFactory,
            final ResourceConfiguration configuration) {
        this.threadFactory = threadFactory;
        this.configuration = configuration;
    }

    /**
     * Creates a new thread pool using the {@link ResourceConfiguration#maxThreads() configured amount of maximum
     * threads}.
     *
     * @return The newly created thread pool.
     */
    Executor createThreadPool() {
        return new ForkJoinPool(calculateNumberOfThreadsToUse(), threadFactory, null, false);
    }

    private int calculateNumberOfThreadsToUse() {
        final var threads = configuration.maxThreads();
        final var processors = Runtime.getRuntime().availableProcessors();
        return threads < 1 ? processors : Math.max(1, Math.min(threads, processors));
    }

}
