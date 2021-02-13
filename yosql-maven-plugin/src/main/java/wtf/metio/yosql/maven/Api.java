/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import org.apache.maven.project.MavenProject;
import wtf.metio.yosql.model.configuration.ApiConfiguration;
import wtf.metio.yosql.model.options.DaoApiOptions;
import wtf.metio.yosql.model.options.LoggingApiOptions;

import java.util.Locale;

/**
 * Configures how logging is applied to generated code.
 */
public class Api {

    /**
     * The logging API to use (default: <strong>auto</strong> which picks the logging API based on the projects
     * dependencies). Possible other values are "jdk", "log4j", "slf4j" and "none".
     */
    String loggingApi = "auto";

    /**
     * The DAO API to use (default: <strong>auto</strong> which picks the DAO API based on the projects
     * dependencies). Possible other values are "ebean", "jdbc", "jdbi", "jooq", "jpa", "r2dbc", "spring_data_jdbc" and
     * "spring_jdbc".
     */
    String daoApi = "auto";

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

    ApiConfiguration asConfiguration(final MavenProject project) {
        return ApiConfiguration.builder()
                .setLoggingApi(determineLoggingApi(project))
                .setDaoApi(determineDaoApi())
                .build();
    }

    private LoggingApiOptions determineLoggingApi(final MavenProject project) {
        var logging = loggingApi.toLowerCase(Locale.ROOT);
        if ("auto".equals(logging)) {
            logging = project.getDependencies().stream()
                    .filter(dependency -> slf4jGroupId.equals(dependency.getGroupId()))
                    .filter(dependency -> slf4jArtifactId.equals(dependency.getArtifactId()))
                    .findFirst()
                    .map(dependency -> "slf4j")
                    .or(() -> project.getDependencies().stream()
                            .filter(dependency -> log4jGroupId.equals(dependency.getGroupId()))
                            .filter(dependency -> log4jArtifactId.equals(dependency.getArtifactId()))
                            .findFirst()
                            .map(dependency -> "log4j"))
                    .orElse("jdk");
        }
        return switch (logging) {
            case "slf4j" -> LoggingApiOptions.SLF4J;
            case "log4j" -> LoggingApiOptions.LOG4J;
            case "jdk" -> LoggingApiOptions.JDK;
            default -> LoggingApiOptions.NONE;
        };
    }

    private DaoApiOptions determineDaoApi() {
        return switch (daoApi.toLowerCase(Locale.ROOT)) {
            case "ebean" -> DaoApiOptions.EBEAN;
            case "jdbi" -> DaoApiOptions.JDBI;
            case "jooq" -> DaoApiOptions.JOOQ;
            case "jpa" -> DaoApiOptions.JPA;
            case "spring_data_jdbc" -> DaoApiOptions.SPRING_DATA_JDBC;
            case "spring_jdbc" -> DaoApiOptions.SPRING_JDBC;
            case "r2dbc" -> DaoApiOptions.R2DBC;
            default -> DaoApiOptions.JDBC;
        };
    }

}
