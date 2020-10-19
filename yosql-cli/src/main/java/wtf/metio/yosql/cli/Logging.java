/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.cli;

import picocli.CommandLine;
import wtf.metio.yosql.model.configuration.LoggingConfiguration;
import wtf.metio.yosql.model.options.LoggingApiOptions;

/**
 * Configures how logging is applied to generated code.
 */
public class Logging {

    @CommandLine.Option(
            names = "--logging-api",
            description = "The logging API to use.",
            defaultValue = "JDK")
    LoggingApiOptions loggingApi;

    LoggingConfiguration asConfiguration() {
        return LoggingConfiguration.builder()
                .setApi(loggingApi)
                .build();
    }

}
