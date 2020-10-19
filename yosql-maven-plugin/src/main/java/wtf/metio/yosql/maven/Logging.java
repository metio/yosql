/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import wtf.metio.yosql.model.configuration.LoggingConfiguration;
import wtf.metio.yosql.model.options.LoggingApiOptions;

/**
 * Configures how logging is applied to generated code.
 */
public class Logging {

    /**
     * The logging API to use (default: <strong>auto</strong> which picks the logging API based on the projects
     * dependencies). Possible other values are "jdk", "log4j", "slf4j" and "none".
     */
    String loggingApi = "auto";

    /**
     * The groupId to match for automatic Log4j detection (default: <strong>"org.apache.logging.log4j"</strong>).
     */
    String log4jGroupId = "org.apache.logging.log4j";

    /**
     * The artifactId to match for automatic Log4j detection (default: <strong>"log4j-api"</strong>).
     */
    String log4jArtifactId = "log4j-api";

    /**
     * The groupId to match for automatic SLF4j detection (default: <strong>"org.slf4j"</strong>).
     */
    String slf4jGroupId = "org.slf4j";

    /**
     * The artifactId to match for automatic SLF4j detection (default: <strong>"slf4j-api"</strong>).
     */
    String slf4jArtifactId = "slf4j-api";

    LoggingConfiguration asConfiguration() {
        return LoggingConfiguration.builder()
                .setApi(determineLoggingApi())
                .build();
    }

    private LoggingApiOptions determineLoggingApi() {
        return switch (loggingApi) {
            case "auto", "slf4j" -> LoggingApiOptions.SLF4J;
            case "log4j" -> LoggingApiOptions.LOG4J;
            case "jdk" -> LoggingApiOptions.JDK;
            default -> LoggingApiOptions.NONE;
        };
    }

}
