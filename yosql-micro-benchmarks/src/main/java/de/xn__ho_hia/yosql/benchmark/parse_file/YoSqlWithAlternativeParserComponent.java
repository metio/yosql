package de.xn__ho_hia.yosql.benchmark.parse_file;

import dagger.Component;
import de.xn__ho_hia.yosql.YoSql;
import de.xn__ho_hia.yosql.YoSqlModule;
import de.xn__ho_hia.yosql.benchmark.BenchmarkConfigurationModule;
import de.xn__ho_hia.yosql.dagger.ErrorModule;
import de.xn__ho_hia.yosql.dagger.I18nModule;
import de.xn__ho_hia.yosql.generator.dao.DaoModule;
import de.xn__ho_hia.yosql.generator.logging.LoggingModule;
import de.xn__ho_hia.yosql.generator.utilities.DefaultUtilitiesModule;
import de.xn__ho_hia.yosql.parser.DefaultResolverModule;
import de.xn__ho_hia.yosql.parser.SqlFileParser;

@SuppressWarnings("javadoc")
@Component(modules = { BenchmarkConfigurationModule.class, AlternativeParserModule.class,
        DefaultResolverModule.class, DefaultUtilitiesModule.class, LoggingModule.class, DaoModule.class,
        I18nModule.class, ErrorModule.class, YoSqlModule.class })
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
