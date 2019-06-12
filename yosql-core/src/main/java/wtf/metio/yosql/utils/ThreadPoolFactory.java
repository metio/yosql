/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.utils;

import wtf.metio.yosql.model.ExecutionConfiguration;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * Creates thread pools which are used during parsing/generating/writing.
 */
public final class ThreadPoolFactory {

    private final NamedForkJoinWorkerThreadFactory threadFactory;
    private final ExecutionConfiguration           configuration;

    ThreadPoolFactory(
            final NamedForkJoinWorkerThreadFactory threadFactory,
            final ExecutionConfiguration configuration) {
        this.threadFactory = threadFactory;
        this.configuration = configuration;
    }

    /**
     * Creates a new thread pool using the {@link ExecutionConfiguration#maxThreads() configured amount of maximum
     * threads}.
     *
     * @return The newly created thread pool.
     */
    public Executor createThreadPool() {
        return new ForkJoinPool(calculateNumberOfThreadsToUse(), threadFactory, null, false);
    }

    private int calculateNumberOfThreadsToUse() {
        final int threads = configuration.maxThreads();
        final int processors = Runtime.getRuntime().availableProcessors();
        return threads < 1 ? processors : Math.max(1, Math.min(threads, processors));
    }

}
