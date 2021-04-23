/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.InstantiationStrategy;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wtf.metio.yosql.codegen.api.YoSQL;
import wtf.metio.yosql.codegen.exceptions.CodeGenerationException;
import wtf.metio.yosql.codegen.logging.Loggers;
import wtf.metio.yosql.internals.jdk.SupportedLocales;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.DaggerYoSQLComponent;

import java.util.Arrays;

/**
 * The generate goal generates Java code based on SQL files.
 */
@Mojo(
        name = "generate",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        instantiationStrategy = InstantiationStrategy.SINGLETON,
        threadSafe = true
)
public class GenerateMojo extends AbstractMojo {

    private static final Logger LOG = LoggerFactory.getLogger(Loggers.EXECUTIONS.loggerName);

    @Parameter(required = true, defaultValue = "${classObject}")
    Files files;

    @Parameter(required = true, defaultValue = "${classObject}")
    Annotations annotations;

    @Parameter(required = true, defaultValue = "${classObject}")
    Java java;

    @Parameter(required = true, defaultValue = "${classObject}")
    Api api;

    @Parameter(required = true, defaultValue = "${classObject}")
    Repositories repositories;

    @Parameter(required = true, defaultValue = "${classObject}")
    Resources resources;

    @Parameter(required = true, defaultValue = "${classObject}")
    Jdbc jdbc;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    MavenProject project;

    @Override
    public void execute() {
        try {
            buildYoSQL().generateCode();
        } catch (final CodeGenerationException exception) {
            Arrays.stream(exception.getSuppressed())
                    .forEach(suppressed -> LOG.error(suppressed.getMessage(), suppressed));
            throw exception;
        }
    }

    private YoSQL buildYoSQL() {
        return DaggerYoSQLComponent.builder()
                .runtimeConfiguration(createConfiguration())
                .locale(SupportedLocales.defaultLocale())
                .build()
                .yosql();
    }

    private RuntimeConfiguration createConfiguration() {
        return RuntimeConfiguration.builder()
                .setFiles(files.asConfiguration(project.getBasedir().getAbsolutePath()))
                .setAnnotations(annotations.asConfiguration())
                .setJava(java.asConfiguration())
                .setApi(api.asConfiguration())
                .setRepositories(repositories.asConfiguration())
                .setResources(resources.asConfiguration())
                .setJdbc(jdbc.asConfiguration())
                .build();
    }

}
