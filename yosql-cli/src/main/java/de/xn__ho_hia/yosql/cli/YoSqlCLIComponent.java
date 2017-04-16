package de.xn__ho_hia.yosql.cli;

import dagger.Component;
import de.xn__ho_hia.yosql.YoSql;
import de.xn__ho_hia.yosql.YoSqlModule;
import de.xn__ho_hia.yosql.dagger.DefaultPrintStreamModule;
import de.xn__ho_hia.yosql.dagger.ErrorModule;
import de.xn__ho_hia.yosql.dagger.I18nModule;
import de.xn__ho_hia.yosql.generator.dao.DaoModule;
import de.xn__ho_hia.yosql.generator.logging.LoggingModule;
import de.xn__ho_hia.yosql.generator.utilities.DefaultUtilitiesModule;
import de.xn__ho_hia.yosql.parser.DefaultParserModule;
import de.xn__ho_hia.yosql.parser.DefaultResolverModule;

@SuppressWarnings("javadoc")
@Component(modules = { DefaultPrintStreamModule.class, JOptConfigurationModule.class, DefaultParserModule.class,
        DefaultResolverModule.class, DefaultUtilitiesModule.class, LoggingModule.class, DaoModule.class,
        I18nModule.class, ErrorModule.class, YoSqlModule.class })
public interface YoSqlCLIComponent {

    YoSql yosql();

}