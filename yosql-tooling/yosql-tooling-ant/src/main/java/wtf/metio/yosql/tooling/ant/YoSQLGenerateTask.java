/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import wtf.metio.yosql.codegen.orchestration.YoSQL;
import wtf.metio.yosql.internals.jdk.SupportedLocales;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.DaggerYoSQLComponent;

/**
 * Ant task that uses YoSQL to read SQL files and generate Java code.
 */
public class YoSQLGenerateTask extends Task {

    private Annotations annotations = new Annotations();
    private Converter converter = new Converter();
    private Files files = new Files();
    private Java java = new Java();
    private Logging logging = new Logging();
    private Names names = new Names();
    private Repositories repositories = new Repositories();
    private Resources resources = new Resources();

    @Override
    public void execute() {
        try {
            buildYoSQL().generateCode();
        } catch (final Throwable throwable) {
            throw new BuildException("Failure to generate code", throwable);
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
                .setFiles(files.asConfiguration(getProject().getBaseDir().toPath()))
                .setAnnotations(annotations.asConfiguration())
                .setJava(java.asConfiguration())
                .setLogging(logging.asConfiguration())
                .setNames(names.asConfiguration())
                .setRepositories(repositories.asConfiguration())
                .setResources(resources.asConfiguration())
                .setConverter(converter.asConfiguration())
                .build();
    }

    //region Ant setters

    public void addConfiguredAnnotations(final Annotations annotations) {
        this.annotations = annotations;
    }

    public void addConfiguredConverter(final Converter converter) {
        this.converter = converter;
    }

    public void addConfiguredFiles(final Files files) {
        this.files = files;
    }

    public void addConfiguredJava(final Java java) {
        this.java = java;
    }

    public void addConfiguredLogging(final Logging logging) {
        this.logging = logging;
    }

    public void addConfiguredNames(final Names names) {
        this.names = names;
    }

    public void addConfiguredRepositories(final Repositories repositories) {
        this.repositories = repositories;
    }

    public void addConfiguredResources(final Resources resources) {
        this.resources = resources;
    }

    //endregion

}
