/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import wtf.metio.yosql.models.immutables.*;
import wtf.metio.yosql.tooling.dagger.DaggerYoSQLComponent;

import java.util.List;
import java.util.Locale;

/**
 * Generate Java code by reading SQL code.
 */
public abstract class GenerateTask extends DefaultTask {

    private static final List<Locale> SUPPORTED_LOCALES = List.of(Locale.ENGLISH, Locale.GERMAN);

    /**
     * @return The file configuration to use.
     */
    @Input
    public abstract Property<FilesConfiguration> getFiles();

    /**
     * Generate Java code.
     */
    @TaskAction
    public void generateCode() {
        DaggerYoSQLComponent.builder()
                .runtimeConfiguration(createConfiguration())
                .locale(determineLocale())
                .build()
                .yosql();
    }

    private RuntimeConfiguration createConfiguration() {
        return RuntimeConfiguration.builder()
                .setFiles(getFiles().get())
                .setAnnotations(AnnotationsConfiguration.usingDefaults().build())
                .setJava(JavaConfiguration.usingDefaults().build())
                .setApi(ApiConfiguration.usingDefaults().build())
                .setRepositories(RepositoriesConfiguration.usingDefaults().build())
                .setResources(ResourcesConfiguration.usingDefaults().build())
                .setJdbc(JdbcConfiguration.usingDefaults().build())
                .build();
    }

    private Locale determineLocale() {
        return SUPPORTED_LOCALES.stream()
                .filter(Locale.getDefault()::equals)
                .findFirst()
                .orElse(Locale.ENGLISH);
    }

}
