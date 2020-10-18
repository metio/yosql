/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import wtf.metio.yosql.DaggerYoSQLComponent;
import wtf.metio.yosql.YoSQL;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;

import java.nio.file.Paths;

/**
 * The generate goal generates Java code based on SQL files.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class YoSqlGenerateMojo extends AbstractMojo {

    @Parameter(required = true, defaultValue = "${classObject}")
    private Files files;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Annotations annotations;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Java java;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Logging logging;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Methods methods;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Repositories repositories;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Statements statements;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Resources resources;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Variables variables;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Packages packages;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Results results;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Jdbc jdbc;

    @Parameter(required = true, defaultValue = "${classObject}")
    private RxJava rxJava;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    MavenProject project;

    @Override
    public void execute() {
        final var configuration = createConfiguration();
        final var yosql = createYoSql(configuration);

        yosql.generateCode();
    }

    private YoSQL createYoSql(final RuntimeConfiguration configuration) {
        return DaggerYoSQLComponent.builder()
                .runtimeConfiguration(configuration)
                .build()
                .yosql();
    }

    private RuntimeConfiguration createConfiguration() {
        final var utilityPackage = packages.getUtilityPackageName();
        return RuntimeConfiguration.builder()
                .setFiles(files.asConfiguration(project.getBasedir().toPath(), Paths.get(project.getBuild().getOutputDirectory())))
                .setAnnotations(annotations.asConfiguration())
                .setJava(java.asConfiguration())
                .setLogging(logging.asConfiguration())
                .setMethods(methods.asConfiguration())
                .setRepositories(repositories.asConfiguration())
                .setResources(resources.asConfiguration())
                .setVariables(variables.asConfiguration())
                .setStatements(statements.asConfiguration())
                .setNames(packages.asConfiguration())
                .setResult(results.asConfiguration(utilityPackage))
                .setJdbcNames(jdbc.namesConfiguration())
                .setJdbcFields(jdbc.fieldsConfiguration())
                .setRxJava(rxJava.asConfiguration(utilityPackage))
                .build();
    }

}
