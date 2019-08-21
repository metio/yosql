/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark;

import javax.inject.Singleton;

import dagger.Component;
import wtf.metio.yosql.YoSql;
import wtf.metio.yosql.YoSqlModule;
import wtf.metio.yosql.dagger.DefaultLocaleModule;
import wtf.metio.yosql.dagger.ErrorModule;
import wtf.metio.yosql.dagger.I18nModule;
import wtf.metio.yosql.dagger.LoggerModule;
import wtf.metio.yosql.generator.dao.DaoModule;
import wtf.metio.yosql.generator.helpers.HelperModule;
import wtf.metio.yosql.generator.logging.LoggingModule;
import wtf.metio.yosql.generator.utilities.DefaultUtilitiesModule;
import wtf.metio.yosql.parser.DefaultParserModule;
import wtf.metio.yosql.parser.DefaultResolverModule;
import wtf.metio.yosql.parser.SqlFileParser;

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
        HelperModule.class,
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
