/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.cli.dagger;

import javax.inject.Singleton;

import org.slf4j.Logger;

import dagger.BindsInstance;
import dagger.Component;
import wtf.metio.yosql.YoSql;
import wtf.metio.yosql.YoSqlModule;
import wtf.metio.yosql.cli.i18n.Commands;
import wtf.metio.yosql.cli.parser.YoSqlOptionParser;
import wtf.metio.yosql.dagger.ErrorModule;
import wtf.metio.yosql.dagger.I18nModule;
import wtf.metio.yosql.dagger.LoggerModule;
import wtf.metio.yosql.generator.dao.DaoModule;
import wtf.metio.yosql.generator.helpers.HelperModule;
import wtf.metio.yosql.generator.logging.LoggingModule;
import wtf.metio.yosql.generator.utilities.DefaultUtilitiesModule;
import wtf.metio.yosql.model.HelpOptions;
import wtf.metio.yosql.model.Translator;
import wtf.metio.yosql.parser.DefaultParserModule;
import wtf.metio.yosql.parser.DefaultResolverModule;
import joptsimple.OptionSpec;

/**
 * Dagger component for the yosql-cli.
 */
@Singleton
@Component(modules = {
        JOptConfigurationModule.class,
        JOptLocaleModule.class,
        DefaultParserModule.class,
        DefaultResolverModule.class,
        DefaultUtilitiesModule.class,
        HelperModule.class,
        LoggingModule.class,
        I18nModule.class,
        OptionParserModule.class,
        VersionOptionParserModule.class,
        GenerateOptionParserModule.class,
        HelpOptionParserModule.class,
        ErrorModule.class,
        LoggerModule.class,
        DaoModule.class,
        YoSqlModule.class,
})
public interface YoSqlCLIComponent {

    /**
     * @return The YoSql instance to use.
     */
    YoSql yoSql();

    /**
     * @return The translator to use.
     */
    Translator translator();

    /**
     * @return The option parser for the 'generate' command.
     */
    @UsedFor.Command(Commands.GENERATE)
    YoSqlOptionParser generateParser();

    /**
     * @return The option parser for the 'help' command.
     */
    @UsedFor.Command(Commands.HELP)
    YoSqlOptionParser helpParser();

    /**
     * @return The option parser for the 'version' command.
     */
    @UsedFor.Command(Commands.VERSION)
    YoSqlOptionParser versionParser();

    /**
     * @return The help '--command' option
     */
    @UsedFor.HelpOption(HelpOptions.COMMAND)
    OptionSpec<String> helpCommandOption();

    /**
     * @return The root logger.
     */
    Logger rootLogger();

    /**
     * The custom dagger builder to use.
     */
    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder arguments(@UsedFor.CLI String[] arguments);

        YoSqlCLIComponent build();
    }

}
