/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.dagger.orchestration;

import ch.qos.cal10n.IMessageConveyor;
import dagger.Module;
import dagger.Provides;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.dao.CodeGenerator;
import wtf.metio.yosql.codegen.files.FileParser;
import wtf.metio.yosql.codegen.lifecycle.ExecutionErrors;
import wtf.metio.yosql.codegen.orchestration.*;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.annotations.TimeLogger;
import wtf.metio.yosql.tooling.dagger.annotations.Writer;

import javax.inject.Singleton;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

@Module
public class DefaultOrchestrationModule {

    @Provides
    @Singleton
    YoSQL provideYoSql(
            final Orchestrator orchestrator,
            final FileParser files,
            final CodeGenerator codeGenerator) {
        return new OrchestratedYoSQL(orchestrator, files, codeGenerator);
    }

    @Provides
    @Singleton
    Timer provideTimer(@TimeLogger final LocLogger logger) {
        return new DefaultTimer(logger);
    }

    @Provides
    @Singleton
    TypeWriter provideTypeWriter(
            @Writer final LocLogger logger,
            final RuntimeConfiguration runtimeConfiguration,
            final ExecutionErrors errors) {
        return new DefaultTypeWriter(logger, runtimeConfiguration.files(), errors);
    }

    @Provides
    @Singleton
    Orchestrator provideOrchestrator(
            final Executor pool,
            final Timer timer,
            final IMessageConveyor messages,
            final TypeWriter typeWriter,
            final ExecutionErrors errors) {
        return new DefaultOrchestrator(
                pool,
                timer,
                messages,
                typeWriter,
                errors);
    }

    @Provides
    @Singleton
    ForkJoinPool.ForkJoinWorkerThreadFactory provideForkJoinWorkerThreadFactory() {
        return (pool) -> {
            final var threadFactory = ForkJoinPool.defaultForkJoinWorkerThreadFactory;
            final var worker = threadFactory.newThread(pool);
            worker.setName("yosql-worker-" + worker.getPoolIndex());
            return worker;
        };
    }

    @Provides
    @Singleton
    Executor provideExecutor(final RuntimeConfiguration runtimeConfiguration,
                             final ForkJoinPool.ForkJoinWorkerThreadFactory threadFactory) {
        return new ForkJoinPool(calculateNumberOfThreadsToUse(runtimeConfiguration), threadFactory, null, false);
    }

    private int calculateNumberOfThreadsToUse(final RuntimeConfiguration runtimeConfiguration) {
        final var threads = runtimeConfiguration.resources().maxThreads();
        final var processors = Runtime.getRuntime().availableProcessors();
        return threads < 1 ? processors : Math.max(1, Math.min(threads, processors));
    }

    @Provides
    @Singleton
    ExecutionErrors provideExecutionErrors() {
        return new ExecutionErrors();
    }

}
