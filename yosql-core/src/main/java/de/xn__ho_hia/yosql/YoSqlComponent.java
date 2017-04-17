package de.xn__ho_hia.yosql;

import dagger.Component;
import de.xn__ho_hia.yosql.dagger.DefaultConfigurationModule;
import de.xn__ho_hia.yosql.dagger.ErrorModule;
import de.xn__ho_hia.yosql.dagger.I18nModule;
import de.xn__ho_hia.yosql.dagger.LoggerModule;
import de.xn__ho_hia.yosql.generator.dao.DaoModule;
import de.xn__ho_hia.yosql.generator.logging.LoggingModule;
import de.xn__ho_hia.yosql.generator.utilities.DefaultUtilitiesModule;
import de.xn__ho_hia.yosql.parser.DefaultParserModule;
import de.xn__ho_hia.yosql.parser.DefaultResolverModule;

/**
 * Main Dagger2 interface to get a new YoSql instance.
 */
@Component(modules = { DefaultConfigurationModule.class, DefaultParserModule.class,
        DefaultResolverModule.class, DefaultUtilitiesModule.class, LoggingModule.class, DaoModule.class,
        I18nModule.class, ErrorModule.class, YoSqlModule.class, LoggerModule.class })
public interface YoSqlComponent {

    /**
     * @return The YoSql instance contained in this graph.
     */
    YoSql yosql();

}
