/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.utils;

import static de.xn__ho_hia.yosql.model.ApplicationEvents.WORKER_POOL_NAME;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;

import de.xn__ho_hia.yosql.model.ApplicationEvents;
import de.xn__ho_hia.yosql.model.Translator;

/**
 * Names new threads according to a {@link ApplicationEvents#WORKER_POOL_NAME predefined pattern}.
 */
final class NamedForkJoinWorkerThreadFactory implements ForkJoinWorkerThreadFactory {

    private final Translator                  translator;
    private final ForkJoinWorkerThreadFactory threadFactory;

    NamedForkJoinWorkerThreadFactory(
            final Translator translator,
            final ForkJoinPool.ForkJoinWorkerThreadFactory threadFactory) {
        this.translator = translator;
        this.threadFactory = threadFactory;
    }

    @Override
    public ForkJoinWorkerThread newThread(final ForkJoinPool pool) {
        final ForkJoinWorkerThread worker = threadFactory.newThread(pool);
        worker.setName(translator.nonLocalized(WORKER_POOL_NAME, Integer.valueOf(worker.getPoolIndex())));
        return worker;
    }

}
