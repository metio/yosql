/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql;

import dagger.Component;
import wtf.metio.yosql.files.FilesModule;
import wtf.metio.yosql.generator.CodeGeneratorModule;
import wtf.metio.yosql.i18n.I18nModule;
import wtf.metio.yosql.model.configuration.ModelConfigurationModule;
import wtf.metio.yosql.orchestration.OrchestrationModule;

import javax.inject.Singleton;

/**
 * Dagger interface to get a new YoSQL instance based on the default configuration and
 * built-in default implementations of the various YoSQL interfaces.
 */
@Singleton
@Component(modules = {
        I18nModule.class,
        ModelConfigurationModule.class,
        OrchestrationModule.class,
        FilesModule.class,
        CodeGeneratorModule.class,
        YoSqlModule.class,
})
@FunctionalInterface
public interface YoSqlComponent {

    /**
     * @return The YoSQL instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    YoSql yosql();

}
