/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.cli.options;

import picocli.CommandLine;
import wtf.metio.yosql.tooling.codegen.model.configuration.ApiConfiguration;
import wtf.metio.yosql.tooling.codegen.model.options.DaoApiOptions;
import wtf.metio.yosql.tooling.codegen.model.options.LoggingApiOptions;

/**
 * Configures the API used in generated code.
 */
public class Api {

    @CommandLine.Option(
            names = "--logging-api",
            description = "The logging API to use.",
            defaultValue = "JDK")
    LoggingApiOptions loggingApi;

    @CommandLine.Option(
            names = "--dao-api",
            description = "The DAO API to use.",
            defaultValue = "JDBC")
    DaoApiOptions daoApi;

    ApiConfiguration asConfiguration() {
        return ApiConfiguration.builder()
                .setLoggingApi(loggingApi)
                .setDaoApi(daoApi)
                .build();
    }

}
