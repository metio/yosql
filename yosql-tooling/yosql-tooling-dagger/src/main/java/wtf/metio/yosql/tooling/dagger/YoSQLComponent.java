/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.dagger;

import dagger.BindsInstance;
import dagger.Component;
import wtf.metio.yosql.codegen.api.YoSQL;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.codegen.DefaultCodeGeneratorModule;
import wtf.metio.yosql.tooling.dagger.files.DefaultFilesModule;
import wtf.metio.yosql.tooling.dagger.i18n.Cal10nModule;
import wtf.metio.yosql.tooling.dagger.orchestration.DefaultOrchestrationModule;

import javax.inject.Singleton;
import java.util.Locale;

/**
 * Configures the entire dependency graph without a {@link RuntimeConfiguration} and {@link Locale} which needs to be
 * provided through the builder method.
 */
@Singleton
@Component(modules = {
        Cal10nModule.class,
        DefaultOrchestrationModule.class,
        DefaultFilesModule.class,
        DefaultCodeGeneratorModule.class
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
