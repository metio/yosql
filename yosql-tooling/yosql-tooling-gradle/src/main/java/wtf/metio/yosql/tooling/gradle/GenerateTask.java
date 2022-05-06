/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import wtf.metio.yosql.internals.jdk.SupportedLocales;
import wtf.metio.yosql.models.immutables.*;
import wtf.metio.yosql.tooling.dagger.DaggerYoSQLComponent;

/**
 * Generate Java code by reading SQL code.
 */
public abstract class GenerateTask extends DefaultTask {

    /**
     * @return The annotations configuration to use.
     */
    @Input
    public abstract Property<AnnotationsConfiguration> getAnnotations();

    /**
     * @return The API configuration to use.
     */
    @Input
    public abstract Property<ApiConfiguration> getApi();

    /**
     * @return The file configuration to use.
     */
    @Input
    public abstract Property<FilesConfiguration> getFiles();

    /**
     * @return The JDBC configuration to use.
     */
    @Input
    public abstract Property<ConverterConfiguration> getConverter();

    /**
     * @return The Java configuration to use.
     */
    @Input
    public abstract Property<JavaConfiguration> getJava();

    /**
     * @return The repositories configuration to use.
     */
    @Input
    public abstract Property<RepositoriesConfiguration> getRepositories();

    /**
     * @return The resources configuration to use.
     */
    @Input
    public abstract Property<ResourcesConfiguration> getResources();

    /**
     * Generate Java code.
     */
    @TaskAction
    public void generateCode() {
        DaggerYoSQLComponent.builder()
                .runtimeConfiguration(createConfiguration())
                .locale(SupportedLocales.defaultLocale())
                .build()
                .yosql()
                .generateCode();
    }

    private RuntimeConfiguration createConfiguration() {
        return RuntimeConfiguration.builder()
                .setFiles(getFiles().get())
                .setConverter(getConverter().get())
                .setJava(getJava().get())
                .setRepositories(getRepositories().get())
                .setApi(getApi().get())
                .setAnnotations(getAnnotations().get())
                .setResources(getResources().get())
                .build();
    }

}
