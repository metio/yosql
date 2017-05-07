package de.xn__ho_hia.yosql.benchmark.parse_file;

import javax.inject.Singleton;

import dagger.Component;
import de.xn__ho_hia.yosql.YoSql;
import de.xn__ho_hia.yosql.YoSqlModule;
import de.xn__ho_hia.yosql.benchmark.BenchmarkConfigurationModule;
import de.xn__ho_hia.yosql.dagger.DefaultLocaleModule;
import de.xn__ho_hia.yosql.dagger.ErrorModule;
import de.xn__ho_hia.yosql.dagger.I18nModule;
import de.xn__ho_hia.yosql.dagger.LoggerModule;
import de.xn__ho_hia.yosql.generator.dao.DaoModule;
import de.xn__ho_hia.yosql.generator.logging.LoggingModule;
import de.xn__ho_hia.yosql.generator.utilities.DefaultUtilitiesModule;
import de.xn__ho_hia.yosql.parser.DefaultResolverModule;
import de.xn__ho_hia.yosql.parser.SqlFileParser;

/**
 * Dagger module that uses an alternative parser.
 */
@Singleton
@Component(modules = {
        DefaultLocaleModule.class,
        BenchmarkConfigurationModule.class,
        AlternativeParserModule.class,
        DefaultResolverModule.class,
        DefaultUtilitiesModule.class,
        LoggingModule.class,
        I18nModule.class,
        ErrorModule.class,
        LoggerModule.class,
        DaoModule.class,
        YoSqlModule.class,
})
public interface YoSqlWithAlternativeParserComponent {

    /**
     * @return The yosql instance contained in this graph.
     */
    YoSql yosql();

    /**
     * @return The .sql file parser contained in this graph.
     */
    SqlFileParser sqlFileParser();

}
