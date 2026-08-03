/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import wtf.metio.yosql.internals.jdk.SupportedLocales;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.DaggerYoSQLComponent;

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
     */
    @Input
    public Provider<String> getConfigurationFingerprint() {
        return getRuntimeConfiguration().map(Object::toString);
    }

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
