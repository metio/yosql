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

import java.util.ArrayList;
import java.util.List;

/**
 * The 'generate' goal generates Java code based on SQL files.
 */
/*
 * Per-lookup, which is Maven's default and what threadSafe promises. A singleton hands one instance
 * to every module that binds the goal, so under `mvn -T` two modules populate the same configuration
 * fields at once and either can generate with the other's output directory and base package.
 */
@Mojo(
        name = "generate",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        threadSafe = true
)
public class GenerateMojo extends AbstractMojo {

    @Parameter(required = true, defaultValue = "${classObject}")
    Files files;

    @Parameter(required = true, defaultValue = "${classObject}")
    Annotations annotations;

    @Parameter(required = true, defaultValue = "${classObject}")
    Logging logging;

    @Parameter(required = true, defaultValue = "${classObject}")
    Repositories repositories;

    @Parameter(required = true, defaultValue = "${classObject}")
    Resources resources;

    @Parameter(required = true, defaultValue = "${classObject}")
    Converter converter;

    @Parameter(required = true, defaultValue = "${classObject}")
    Schema schema;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    MavenProject project;

    @Component
    BuildContext buildContext;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            final var configuration = createConfiguration();
            // Registered whether or not anything is generated this run: an incremental build skips
            // generation when nothing changed, and the sources from the previous run still have to
            // be compiled. Doing this only inside the delta check makes the second build of an
            // unchanged project compile nothing.
            project.addCompileSourceRoot(configuration.files().outputBaseDirectory().toString());
            if (buildContext.hasDelta(regeneratingInputs())) {
                buildYoSQL(configuration).generateCode();
                buildContext.refresh(configuration.files().outputBaseDirectory().toFile());
            }
        } catch (final Exception exception) {
            // Deliberately not Throwable: an OutOfMemoryError or a StackOverflowError says the JVM
            // is in trouble, and rewrapping it as "failure to generate code" hides that.
            throw new MojoExecutionException("Failure to generate code", exception);
        }
    }

    /**
     * Everything an edit to which changes what is generated.
     *
     * <p>The statements are the obvious one, and were the only one asked about — but the records the
     * converter generator reads decide a converter's shape, and the DDL decides what a generated
     * result row type holds. An incremental build that asks only about the SQL leaves both stale, and
     * this plugin opts into incremental builds explicitly, so IDEs take it at its word.</p>
     */
    private List<String> regeneratingInputs() {
        final var inputs = new ArrayList<String>();
        inputs.add(files.inputBaseDirectory);
        inputs.add(files.sourceDirectory);
        if (schema.sqlStatementsDirectory != null && !schema.sqlStatementsDirectory.isBlank()) {
            inputs.add(schema.sqlStatementsDirectory);
        }
        return inputs;
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
                .setLogging(logging.asConfiguration())
                .setRepositories(repositories.asConfiguration())
                .setResources(resources.asConfiguration())
                .setConverter(converter.asConfiguration())
                .setSchema(schema.asConfiguration(project.getBasedir().toPath()))
                .build();
    }

}
