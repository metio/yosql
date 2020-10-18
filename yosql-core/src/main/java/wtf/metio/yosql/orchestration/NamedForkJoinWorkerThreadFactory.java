/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.orchestration;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;

/**
 * Names new threads used by YoSQL.
 */
final class NamedForkJoinWorkerThreadFactory implements ForkJoinWorkerThreadFactory {

    private final ForkJoinWorkerThreadFactory threadFactory;

    NamedForkJoinWorkerThreadFactory(final ForkJoinWorkerThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    @Override
    public ForkJoinWorkerThread newThread(final ForkJoinPool pool) {
        final var worker = threadFactory.newThread(pool);
        worker.setName("yosql-worker-" + worker.getPoolIndex());
        return worker;
    }

}
