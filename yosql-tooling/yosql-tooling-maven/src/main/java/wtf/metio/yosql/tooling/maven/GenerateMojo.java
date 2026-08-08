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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
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
            final var settings = settingsOfLastRun();
            if (buildContext.hasDelta(regeneratingInputs()) || settingsChanged(settings, configuration)) {
                buildYoSQL(configuration).generateCode();
                recordSettings(settings, configuration);
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

    /**
     * Where the settings the last run generated from are remembered.
     *
     * <p>Under {@code target}, so that a clean build starts without one and generates, and so that
     * nothing lands among the sources this plugin writes.</p>
     */
    private Path settingsOfLastRun() {
        return Path.of(project.getBuild().getDirectory(), "yosql", "settings-of-last-run.txt");
    }

    /**
     * Whether the settings decide something different from what they decided last time.
     *
     * <p>What is generated depends on the files above and on the configuration, and only the files
     * were being asked about. Changing a setting and nothing else — a base package, a converter
     * class, the schema's vendor — left an incremental build with no delta to find and the output of
     * the old settings on disk, which then compiles: the change appears to have done nothing at all.
     * A run without a delta is not the same question as a run that would produce the same code.</p>
     *
     * <p>The whole configuration is written out rather than hashed, so that a maintainer looking at
     * why a build regenerated can diff two of these and read the answer.</p>
     */
    // Fully qualified: this package declares its own `Files`, the mojo parameter holding the file
    // settings, and importing java.nio.file.Files here would shadow it.
    // visible for testing
    static boolean settingsChanged(final Path settings, final RuntimeConfiguration configuration) {
        try {
            return !configuration.toString().equals(java.nio.file.Files.readString(settings, StandardCharsets.UTF_8));
        } catch (final IOException _) {
            // Never written, or unreadable. Generating is the answer that cannot be wrong.
            return true;
        }
    }

    // visible for testing
    static void recordSettings(final Path settings, final RuntimeConfiguration configuration) {
        try {
            java.nio.file.Files.createDirectories(settings.getParent());
            java.nio.file.Files.writeString(settings, configuration.toString(), StandardCharsets.UTF_8);
        } catch (final IOException _) {
            // Only costs the next build a regeneration it did not need, which is the safe direction,
            // and failing the build over a note to itself would not be.
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
                .setLogging(logging.asConfiguration())
                .setRepositories(repositories.asConfiguration())
                .setResources(resources.asConfiguration())
                .setConverter(converter.asConfiguration())
                .setSchema(schema.asConfiguration(project.getBasedir().toPath()))
                .build();
    }

}
