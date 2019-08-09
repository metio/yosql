/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql;

import dagger.Component;
import wtf.metio.yosql.dagger.*;
import wtf.metio.yosql.generator.dao.DaoModule;
import wtf.metio.yosql.generator.helpers.HelperModule;
import wtf.metio.yosql.generator.logging.LoggingModule;
import wtf.metio.yosql.generator.utilities.DefaultUtilitiesModule;
import wtf.metio.yosql.parser.DefaultParserModule;
import wtf.metio.yosql.parser.DefaultResolverModule;

import javax.inject.Singleton;

/**
 * Dagger interface to get a new YoSql instance based on the default configuration and
 * built-in default implementations of the various YoSql interfaces.
 */
@Singleton
@Component(modules = {
        DefaultConfigurationModule.class,
        DefaultLocaleModule.class,
        DefaultParserModule.class,
        DefaultResolverModule.class,
        DefaultUtilitiesModule.class,
        HelperModule.class,
        LoggingModule.class,
        DaoModule.class,
        I18nModule.class,
        ErrorModule.class,
        LoggerModule.class,
        YoSqlModule.class,
})
@FunctionalInterface
public interface YoSqlComponent {

    /**
     * @return The YoSql instance configured by the list of modules specified in the
     *         @Component class annotation.
     */
    YoSql yosql();

}
