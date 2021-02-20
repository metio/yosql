/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.ant;

import wtf.metio.yosql.tooling.codegen.model.configuration.ApiConfiguration;
import wtf.metio.yosql.tooling.codegen.model.options.DaoApiOptions;
import wtf.metio.yosql.tooling.codegen.model.options.LoggingApiOptions;

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

    ApiConfiguration asConfiguration() {
        return ApiConfiguration.builder()
                .setLoggingApi(determineLoggingApi())
                .setDaoApi(determineDaoApi())
                .build();
    }

    private LoggingApiOptions determineLoggingApi() {
        return switch (loggingApi) {
            case "slf4j" -> LoggingApiOptions.SLF4J;
            case "log4j" -> LoggingApiOptions.LOG4J;
            case "jdk" -> LoggingApiOptions.JDK;
            default -> LoggingApiOptions.NONE;
        };
    }

    private DaoApiOptions determineDaoApi() {
        return switch (daoApi) {
            case "ebean" -> DaoApiOptions.EBEAN;
            case "jdbi" -> DaoApiOptions.JDBI;
            case "jooq" -> DaoApiOptions.JOOQ;
            case "jpa" -> DaoApiOptions.JPA;
            case "mybatis" -> DaoApiOptions.MYBATIS;
            case "spring_data_jdbc" -> DaoApiOptions.SPRING_DATA_JDBC;
            case "spring_data_jpa" -> DaoApiOptions.SPRING_DATA_JPA;
            case "spring_data_r2dbc" -> DaoApiOptions.SPRING_DATA_R2DBC;
            case "spring_jdbc" -> DaoApiOptions.SPRING_JDBC;
            case "r2dbc" -> DaoApiOptions.R2DBC;
            default -> DaoApiOptions.JDBC;
        };
    }

}
