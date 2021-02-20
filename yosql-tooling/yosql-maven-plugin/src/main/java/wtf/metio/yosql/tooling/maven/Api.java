/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.maven;

import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import wtf.metio.yosql.tooling.codegen.model.configuration.ApiConfiguration;
import wtf.metio.yosql.tooling.codegen.model.options.DaoApiOptions;
import wtf.metio.yosql.tooling.codegen.model.options.LoggingApiOptions;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

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

    /**
     * The groupId to match for automatic Ebean detection (default: <strong>"io.ebean"</strong>).
     */
    String ebeanGroupId = "io.ebean";

    /**
     * The artifactId to match for automatic Ebean detection (default: <strong>"ebean"</strong>).
     */
    String ebeanArtifactId = "ebean";

    /**
     * The groupId to match for automatic JDBI detection (default: <strong>"org.jdbi"</strong>).
     */
    String jdbiGroupId = "org.jdbi";

    /**
     * The artifactId to match for automatic JDBI detection (default: <strong>"jdbi3-core"</strong>).
     */
    String jdbiArtifactId = "jdbi3-core";

    /**
     * The groupId to match for automatic jOOQ detection (default: <strong>"org.jooq"</strong>).
     */
    String jooqGroupId = "org.jooq";

    /**
     * The artifactId to match for automatic jOOQ detection (default: <strong>"jooq"</strong>).
     */
    String jooqArtifactId = "jooq";

    /**
     * The groupId to match for automatic Spring-Data detection (default: <strong>"org.springframework.data"</strong>).
     */
    String springDataGroupId = "org.springframework.data";

    /**
     * The artifactId to match for automatic Spring-Data-JPA detection (default: <strong>"spring-data-jpa"</strong>).
     */
    String springDataJpa = "spring-data-jpa";
    String springDataR2dbc = "spring-data-r2dbc";
    String springDataJdbc = "spring-data-jdbc";

    String springBootGroupId = "org.springframework.boot";
    String dataJpaStarter = "spring-boot-starter-data-jpa";
    String dataJdbcStarter = "spring-boot-starter-jdbc";

    String hibernateGroupId = "org.hibernate";
    String hibernateCore = "hibernate-core";

    String mybatisGroupId = "org.mybatis";
    String mybatisArtifactId = "mybatis";

    String r2dbcGroupId = "io.r2dbc";
    String r2dbcH2 = "r2dbc-h2";
    String r2dbcPostgres = "r2dbc-postgresql";
    String r2dbcMSSQL = "r2dbc-mssql";

    private static Optional<String> hasDependency(
            final List<Dependency> dependencies,
            final String group,
            final String artifact,
            final String api) {
        return dependencies.stream()
                .filter(dependency -> group.equals(dependency.getGroupId()))
                .filter(dependency -> artifact.equals(dependency.getArtifactId()))
                .map(dependency -> api)
                .findFirst();
    }

    ApiConfiguration asConfiguration(final MavenProject project) {
        return ApiConfiguration.builder()
                .setLoggingApi(determineLoggingApi(project))
                .setDaoApi(determineDaoApi(project))
                .build();
    }

    private LoggingApiOptions determineLoggingApi(final MavenProject project) {
        var logging = loggingApi.toLowerCase(Locale.ROOT).strip();
        if ("auto".equals(logging)) {
            final var dependencies = project.getDependencies();
            logging = hasDependency(dependencies, slf4jGroupId, slf4jArtifactId, "slf4j")
                    .or(() -> hasDependency(dependencies, log4jGroupId, log4jArtifactId, "log4j"))
                    .orElse("jdk");
        }
        return switch (logging) {
            case "slf4j" -> LoggingApiOptions.SLF4J;
            case "log4j" -> LoggingApiOptions.LOG4J;
            case "jdk" -> LoggingApiOptions.JDK;
            default -> LoggingApiOptions.NONE;
        };
    }

    private DaoApiOptions determineDaoApi(final MavenProject project) {
        var dao = daoApi.toLowerCase(Locale.ROOT).strip();
        if ("auto".equals(dao)) {
            final var dependencies = project.getDependencies();
            dao = hasDependency(dependencies, ebeanGroupId, ebeanArtifactId, "ebean")
                    .or(() -> hasDependency(dependencies, jdbiGroupId, jdbiArtifactId, "jdbi"))
                    .or(() -> hasDependency(dependencies, jooqGroupId, jooqArtifactId, "jooq"))
                    .or(() -> hasDependency(dependencies, hibernateGroupId, hibernateCore, "jpa"))
                    .or(() -> hasDependency(dependencies, mybatisGroupId, mybatisArtifactId, "mybatis"))
                    .or(() -> hasDependency(dependencies, springDataGroupId, springDataJdbc, "spring_data_jdbc"))
                    .or(() -> hasDependency(dependencies, springDataGroupId, springDataJpa, "spring_data_jpa"))
                    .or(() -> hasDependency(dependencies, springDataGroupId, springDataR2dbc, "spring_data_r2dbc"))
                    .or(() -> hasDependency(dependencies, springBootGroupId, dataJpaStarter, "spring_data_jpa"))
                    .or(() -> hasDependency(dependencies, springBootGroupId, dataJdbcStarter, "spring_jdbc"))
                    .or(() -> hasDependency(dependencies, r2dbcGroupId, r2dbcH2, "r2dbc"))
                    .or(() -> hasDependency(dependencies, r2dbcGroupId, r2dbcPostgres, "r2dbc"))
                    .or(() -> hasDependency(dependencies, r2dbcGroupId, r2dbcMSSQL, "r2dbc"))
                    .orElse("jdbc");
        }
        return switch (dao) {
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
