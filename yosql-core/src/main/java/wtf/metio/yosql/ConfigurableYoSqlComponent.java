/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql;

import dagger.BindsInstance;
import dagger.Component;
import wtf.metio.yosql.files.FilesModule;
import wtf.metio.yosql.generator.CodeGeneratorModule;
import wtf.metio.yosql.i18n.I18nModule;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.orchestration.OrchestrationModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        I18nModule.class,
        OrchestrationModule.class,
        FilesModule.class,
        CodeGeneratorModule.class,
        YoSqlModule.class
})
public interface ConfigurableYoSqlComponent {

    YoSql yosql();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder runtimeConfiguration(RuntimeConfiguration runtimeConfiguration);

        ConfigurableYoSqlComponent build();

    }

}
