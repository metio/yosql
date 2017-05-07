package de.xn__ho_hia.yosql.cli;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import de.xn__ho_hia.yosql.YoSql;
import de.xn__ho_hia.yosql.YoSqlModule;
import de.xn__ho_hia.yosql.dagger.ErrorModule;
import de.xn__ho_hia.yosql.dagger.I18nModule;
import de.xn__ho_hia.yosql.dagger.LoggerModule;
import de.xn__ho_hia.yosql.generator.dao.DaoModule;
import de.xn__ho_hia.yosql.generator.logging.LoggingModule;
import de.xn__ho_hia.yosql.generator.utilities.DefaultUtilitiesModule;
import de.xn__ho_hia.yosql.model.HelpOptions;
import de.xn__ho_hia.yosql.model.Translator;
import de.xn__ho_hia.yosql.parser.DefaultParserModule;
import de.xn__ho_hia.yosql.parser.DefaultResolverModule;
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
     * The custom dagger builder to use.
     */
    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder arguments(@UsedFor.CLI String[] arguments);

        YoSqlCLIComponent build();
    }

}
