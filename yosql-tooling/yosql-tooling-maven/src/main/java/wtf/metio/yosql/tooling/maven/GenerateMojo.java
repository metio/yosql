/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.tooling.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.build.BuildContext;
import wtf.metio.yosql.codegen.orchestration.YoSQL;
import wtf.metio.yosql.internals.jdk.SupportedLocales;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.DaggerYoSQLComponent;

/**
 * The 'generate' goal generates Java code based on SQL files.
 */
@Mojo(
        name = "generate",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        instantiationStrategy = InstantiationStrategy.SINGLETON,
        threadSafe = true
)
public class GenerateMojo extends AbstractMojo {

    @Parameter(required = true, defaultValue = "${classObject}")
    Files files;

    @Parameter(required = true, defaultValue = "${classObject}")
    Annotations annotations;

    @Parameter(required = true, defaultValue = "${classObject}")
    Java java;

    @Parameter(required = true, defaultValue = "${classObject}")
    Logging logging;

    @Parameter(required = true, defaultValue = "${classObject}")
    Names names;

    @Parameter(required = true, defaultValue = "${classObject}")
    Repositories repositories;

    @Parameter(required = true, defaultValue = "${classObject}")
    Resources resources;

    @Parameter(required = true, defaultValue = "${classObject}")
    Converter converter;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    MavenProject project;

    @Component
    BuildContext buildContext;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            if (buildContext.hasDelta(files.inputBaseDirectory)) {
                final var configuration = createConfiguration();
                buildYoSQL(configuration).generateCode();
                buildContext.refresh(configuration.files().outputBaseDirectory().toFile());
            }
        } catch (final Throwable throwable) {
            throw new MojoExecutionException("Failure to generate code", throwable);
        }
    }

    private static YoSQL buildYoSQL(final RuntimeConfiguration configuration) {
        return DaggerYoSQLComponent.builder()
                .runtimeConfiguration(configuration)
                .locale(SupportedLocales.defaultLocale())
                .build()
                .yosql();
    }

    private RuntimeConfiguration createConfiguration() {
        return RuntimeConfiguration.builder()
                .setFiles(files.asConfiguration(project.getBasedir().toPath()))
                .setAnnotations(annotations.asConfiguration())
                .setJava(java.asConfiguration())
                .setLogging(logging.asConfiguration())
                .setNames(names.asConfiguration())
                .setRepositories(repositories.asConfiguration())
                .setResources(resources.asConfiguration())
                .setConverter(converter.asConfiguration())
                .build();
    }

}
