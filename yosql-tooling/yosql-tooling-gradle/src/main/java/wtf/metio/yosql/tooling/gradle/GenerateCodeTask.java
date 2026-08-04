/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.IgnoreEmptyDirectories;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import wtf.metio.yosql.internals.jdk.SupportedLocales;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.DaggerYoSQLComponent;

import java.util.regex.Pattern;

/**
 * Generate Java code by reading SQL code.
 *
 * <p>The input and output directories are declared so that Gradle can answer the question it asks
 * about every task: does this need to run at all. Without them the answer is always yes, and a
 * project that has not touched its SQL regenerates every build.</p>
 */
@CacheableTask
public abstract class GenerateCodeTask extends DefaultTask {

    /**
     * @return The runtime configuration to use.
     */
    @Internal
    public abstract Property<RuntimeConfiguration> getRuntimeConfiguration();

    /**
     * @return the configuration rendered as text, which is what Gradle compares between runs.
     *         A {@code RuntimeConfiguration} is not serializable and cannot be fingerprinted
     *         directly; its {@code toString} names every setting, so two runs whose text matches
     *         were configured identically and the second has nothing to do.
     *
     *         <p>Without the directories in it. They are absolute, so including them keys the cache
     *         to where the checkout sits — which is the opposite of what {@code RELATIVE} path
     *         sensitivity and {@code CacheableTask} are for: a machine that builds under a different
     *         path would miss every entry the build server wrote. The directories are declared
     *         separately, and Gradle fingerprints them the way it should.</p>
     */
    @Input
    public Provider<String> getConfigurationFingerprint() {
        return getRuntimeConfiguration().map(GenerateCodeTask::withoutDirectories);
    }

    /**
     * Every absolute path in a configuration's text, replaced by the setting's name alone. A change
     * of directory still invalidates the task through the directory properties themselves.
     */
    private static String withoutDirectories(final RuntimeConfiguration configuration) {
        return ABSOLUTE_PATH.matcher(configuration.toString()).replaceAll("=<path>");
    }

    private static final Pattern ABSOLUTE_PATH = Pattern.compile("=/[^,)\\]]*");

    /**
     * @return the Java sources the record scanner reads to build converters. Declared as an input
     *         because they decide what is generated: editing a record adds a component, and without
     *         this the task stays up to date and the stale converter is compiled against the new
     *         record.
     */
    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    @IgnoreEmptyDirectories
    public abstract ConfigurableFileCollection getSourceDirectories();

    /**
     * @return the DDL the schema is read from, when it lives somewhere other than among the
     *         statements. Empty unless {@code schema.sqlStatementsDirectory} is set.
     *
     *         <p>An input for the same reason the source directories are: the DDL decides what is
     *         generated. A column added by a migration changes a generated record's components, and
     *         without this the task stays up to date across that edit — and, being a
     *         {@code CacheableTask}, hands out a cache entry keyed on a schema it never read.</p>
     */
    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    @IgnoreEmptyDirectories
    public abstract ConfigurableFileCollection getSchemaDirectories();

    /**
     * @return where the `.sql` files are read from. RELATIVE path sensitivity: moving the whole
     *         project should not invalidate the result, but moving a file within it should.
     */
    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    public abstract DirectoryProperty getInputDirectory();

    /**
     * @return where the generated Java is written, which is what makes the result cacheable.
     */
    @OutputDirectory
    public abstract DirectoryProperty getOutputDirectory();

    /**
     * Generate Java code.
     */
    @TaskAction
    public void generateCode() {
        try {
            DaggerYoSQLComponent.builder()
                    .runtimeConfiguration(getRuntimeConfiguration().get())
                    .locale(SupportedLocales.defaultLocale())
                    .build()
                    .yosql()
                    .generateCode();
        } catch (final Exception exception) {
            // Deliberately not Throwable: an OutOfMemoryError says the JVM is in trouble, and
            // rewrapping it as "failure to generate code" hides that.
            throw new GradleException("Failure to generate code", exception);
        }
    }

}
