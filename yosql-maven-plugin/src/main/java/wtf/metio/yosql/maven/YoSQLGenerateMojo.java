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
import java.util.List;
import java.util.Locale;

/**
 * The generate goal generates Java code based on SQL files.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class YoSQLGenerateMojo extends AbstractMojo {

    private static final List<Locale> SUPPORTED_LOCALES = List.of(Locale.ENGLISH, Locale.GERMAN);

    @Parameter(required = true, defaultValue = "${classObject}")
    private Files files;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Annotations annotations;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Java java;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Api api;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Repositories repositories;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Resources resources;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Utilities utilities;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Results results;

    @Parameter(required = true, defaultValue = "${classObject}")
    private Jdbc jdbc;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    MavenProject project;

    @Override
    public void execute() {
        buildYoSQL().generateCode();
    }

    private YoSQL buildYoSQL() {
        return DaggerYoSQLComponent.builder()
                .runtimeConfiguration(createConfiguration())
                .locale(determineLocale())
                .build()
                .yosql();
    }

    private RuntimeConfiguration createConfiguration() {
        final var utilityPackage = utilities.getUtilityPackageName();
        return RuntimeConfiguration.builder()
                .setFiles(files.asConfiguration(project.getBasedir().toPath(), Paths.get(project.getBuild().getOutputDirectory())))
                .setAnnotations(annotations.asConfiguration())
                .setJava(java.asConfiguration())
                .setApi(api.asConfiguration(project))
                .setRepositories(repositories.asConfiguration())
                .setResources(resources.asConfiguration())
                .setUtility(utilities.asConfiguration())
                .setResult(results.asConfiguration(utilityPackage))
                .setJdbc(jdbc.asConfiguration())
                .build();
    }

    private Locale determineLocale() {
        if (SUPPORTED_LOCALES.contains(Locale.getDefault())) {
            return Locale.getDefault();
        }
        return Locale.ENGLISH;
    }

}
