/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
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

/**
 * Subclass of {@link AbstractCodeGenBenchmark} that initializes YoSQL using the yosql-tooling-dagger module.
 */
abstract class AbstractDaggerBenchmark extends AbstractCodeGenBenchmark {

    /**
     * Creates a new YoSQL instance using the english locale and the determined {@link #config() runtime configuration}.
     */
    @Setup
    public final void setUpYoSQL() {
        yosql = DaggerYoSQLComponent.builder()
                .locale(Locale.ENGLISH)
                .runtimeConfiguration(config())
                .build()
                .yosql();
    }

    /**
     * Determines the runtime configuration based on the currently available input- and output-directories
     *
     * @return Fully configured runtime configuration.
     */
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

    /**
     * Subclasses are allowed to overwrite the APIs used during code generation.
     *
     * @return The API configuration to use while generating code.
     */
    protected ApiConfiguration apiConfig() {
        return ApiConfiguration.usingDefaults().build();
    }

}
