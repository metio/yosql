package wtf.metio.yosql.orchestration;

import dagger.Module;
import dagger.Provides;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.i18n.Translator;
import wtf.metio.yosql.model.annotations.Delegating;
import wtf.metio.yosql.model.annotations.Writer;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.errors.ExecutionErrors;

import javax.inject.Singleton;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

@Module
public class OrchestrationModule {

    @Provides
    @Singleton
    Timer provideTimer(@wtf.metio.yosql.model.annotations.Timer final LocLogger logger) {
        return new DefaultTimer(logger);
    }

    @Provides
    @Singleton
    TypeWriter provideTypeWriter(
            @Writer final LocLogger logger,
            final RuntimeConfiguration runtime,
            final ExecutionErrors errors) {
        return new DefaultTypeWriter(logger, runtime.files(), errors);
    }

    @Provides
    @Singleton
    Orchestrator provideOrchestrator(
            final Executor pool,
            final Timer timer,
            final Translator translator,
            final TypeWriter typeWriter,
            final ExecutionErrors errors) {
        return new DefaultOrchestrator(
                pool,
                timer,
                translator,
                typeWriter,
                errors);
    }

    @Provides
    @Singleton
    ForkJoinPool.ForkJoinWorkerThreadFactory provideNativeForkJoinWorkerThreadFactory() {
        return ForkJoinPool.defaultForkJoinWorkerThreadFactory;
    }

    @Provides
    @Singleton
    @Delegating
    ForkJoinPool.ForkJoinWorkerThreadFactory provideForkJoinWorkerThreadFactory(
            final Translator translator,
            final ForkJoinPool.ForkJoinWorkerThreadFactory threadFactory) {
        return new NamedForkJoinWorkerThreadFactory(translator, threadFactory);
    }

    @Provides
    @Singleton
    ThreadPoolFactory provideThreadPoolFactory(
            @Delegating final ForkJoinPool.ForkJoinWorkerThreadFactory threadFactory,
            final RuntimeConfiguration runtime) {
        return new ThreadPoolFactory(threadFactory, runtime.resources());
    }

    @Provides
    @Singleton
    Executor provideExecutor(final ThreadPoolFactory threadPoolFactory) {
        return threadPoolFactory.createThreadPool();
    }

}
