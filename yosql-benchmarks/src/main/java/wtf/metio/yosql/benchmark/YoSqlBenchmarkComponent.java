/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark;

import dagger.Component;
import wtf.metio.yosql.YoSql;
import wtf.metio.yosql.YoSqlModule;
import wtf.metio.yosql.dagger.DaggerModule;
import wtf.metio.yosql.files.FilesModule;
import wtf.metio.yosql.generator.CodeGeneratorModule;
import wtf.metio.yosql.i18n.I18nModule;
import wtf.metio.yosql.orchestration.OrchestrationModule;

import javax.inject.Singleton;

/**
 * Dagger module for running micro-benchmarks.
 */
@Singleton
@Component(modules = {
        I18nModule.class,
        DaggerModule.class,
        OrchestrationModule.class,
        FilesModule.class,
        CodeGeneratorModule.class,
        YoSqlModule.class,
})
public interface YoSqlBenchmarkComponent {

    /**
     * @return The yosql instance contained in this graph.
     */
    YoSql yosql();

}
