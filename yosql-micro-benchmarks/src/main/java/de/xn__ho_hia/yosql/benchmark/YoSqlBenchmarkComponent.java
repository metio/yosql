/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.benchmark;

import javax.inject.Singleton;

import dagger.Component;
import de.xn__ho_hia.yosql.YoSql;
import de.xn__ho_hia.yosql.YoSqlModule;
import de.xn__ho_hia.yosql.dagger.DefaultLocaleModule;
import de.xn__ho_hia.yosql.dagger.ErrorModule;
import de.xn__ho_hia.yosql.dagger.I18nModule;
import de.xn__ho_hia.yosql.dagger.LoggerModule;
import de.xn__ho_hia.yosql.generator.dao.DaoModule;
import de.xn__ho_hia.yosql.generator.logging.LoggingModule;
import de.xn__ho_hia.yosql.generator.utilities.DefaultUtilitiesModule;
import de.xn__ho_hia.yosql.parser.DefaultParserModule;
import de.xn__ho_hia.yosql.parser.DefaultResolverModule;
import de.xn__ho_hia.yosql.parser.SqlFileParser;

/**
 * Dagger module for running micro-benchmarks.
 */
@Singleton
@Component(modules = {
        BenchmarkConfigurationModule.class,
        DefaultLocaleModule.class,
        DefaultParserModule.class,
        DefaultResolverModule.class,
        DefaultUtilitiesModule.class,
        LoggingModule.class,
        I18nModule.class,
        ErrorModule.class,
        LoggerModule.class,
        DaoModule.class,
        YoSqlModule.class,
})
public interface YoSqlBenchmarkComponent {

    /**
     * @return The yosql instance contained in this graph.
     */
    YoSql yosql();

    /**
     * @return The .sql file parser contained in this graph.
     */
    SqlFileParser sqlFileParser();

}
