/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.tooling.dagger;

import dagger.BindsInstance;
import dagger.Component;
import wtf.metio.yosql.codegen.orchestration.YoSQL;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.blocks.DefaultBlocksModule;
import wtf.metio.yosql.tooling.dagger.dao.DefaultDaoModule;
import wtf.metio.yosql.tooling.dagger.files.DefaultFilesModule;
import wtf.metio.yosql.tooling.dagger.i18n.Cal10nModule;
import wtf.metio.yosql.tooling.dagger.logging.DefaultLoggingModule;
import wtf.metio.yosql.tooling.dagger.orchestration.DefaultOrchestrationModule;
import wtf.metio.yosql.tooling.dagger.validation.DefaultValidationModule;

import javax.inject.Singleton;
import java.util.Locale;

/**
 * Configures the entire dependency graph without a {@link RuntimeConfiguration} and {@link Locale} which needs to be
 * provided through the builder method.
 */
@Singleton
@Component(modules = {
        Cal10nModule.class,
        DefaultValidationModule.class,
        DefaultOrchestrationModule.class,
        DefaultFilesModule.class,
        DefaultBlocksModule.class,
        DefaultDaoModule.class,
        DefaultLoggingModule.class
})
public interface YoSQLComponent {

    /**
     * @return The configured YoSQL instance.
     */
    YoSQL yosql();

    /**
     * Custom builder that allows to inject a user provided {@link RuntimeConfiguration} and {@link Locale}.
     */
    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder runtimeConfiguration(RuntimeConfiguration runtimeConfiguration);

        @BindsInstance
        Builder locale(Locale locale);

        YoSQLComponent build();

    }

}
