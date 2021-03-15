/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark.codegen;

import org.openjdk.jmh.annotations.Setup;
import wtf.metio.yosql.models.immutables.ApiConfiguration;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.JdbcConfiguration;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.models.sql.ResultRowConverter;
import wtf.metio.yosql.tooling.dagger.DaggerYoSQLComponent;

import java.util.Locale;

abstract class AbstractDaggerBenchmark extends AbstractCodeGenBenchmark {

    @Setup
    public final void setUpYoSQL() {
        yosql = DaggerYoSQLComponent.builder()
                .locale(Locale.ENGLISH)
                .runtimeConfiguration(config())
                .build()
                .yosql();
    }

    private RuntimeConfiguration config() {
        final var jdbcDefaults = JdbcConfiguration.usingDefaults().build();
        return RuntimeConfiguration.usingDefaults()
                .setFiles(FilesConfiguration.usingDefaults()
                        .setInputBaseDirectory(inputDirectory)
                        .setOutputBaseDirectory(outputDirectory)
                        .build())
                .setApi(apiConfig())
                .setJdbc(jdbcDefaults.withDefaultConverter(ResultRowConverter.builder()
                        .setAlias("resultRow")
                        .setConverterType(jdbcDefaults.utilityPackageName() + ".ToResultRowConverter")
                        .setMethodName("asUserType")
                        .setResultType(jdbcDefaults.utilityPackageName() + "." + jdbcDefaults.resultRowClassName())
                        .build()))
                .build();
    }

    protected ApiConfiguration apiConfig() {
        return ApiConfiguration.usingDefaults().build();
    }

}
