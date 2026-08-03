/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.tooling.dagger.orchestration;

import ch.qos.cal10n.IMessageConveyor;
import dagger.Module;
import dagger.Provides;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.dao.CodeGenerator;
import wtf.metio.yosql.codegen.files.FileParser;
import wtf.metio.yosql.codegen.schema.SchemaValidator;
import wtf.metio.yosql.codegen.orchestration.*;
import wtf.metio.yosql.codegen.validation.RuntimeValidator;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.annotations.Execution;
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
            final FileParser files,
            final CodeGenerator codeGenerator,
            final Executor pool,
            final Timer timer,
            final IMessageConveyor messages,
            final TypeWriter typeWriter,
            final ExecutionErrors errors,
            final RuntimeValidator runtimeValidator,
            final SchemaValidator schemaValidator) {
        return new DefaultYoSQL(
                files,
                codeGenerator,
                pool,
                timer,
                messages,
                typeWriter,
                errors,
                runtimeValidator,
                schemaValidator);
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
    ExecutionErrors provideExecutionErrors(@Execution final LocLogger logger) {
        return new ExecutionErrors(logger);
    }

}
