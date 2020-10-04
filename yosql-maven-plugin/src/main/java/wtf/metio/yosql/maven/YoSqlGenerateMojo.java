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
import wtf.metio.yosql.DaggerConfigurableYoSqlComponent;
import wtf.metio.yosql.YoSql;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;

/**
 * The *generate* goal generates Java code based on SQL files.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class YoSqlGenerateMojo extends AbstractMojo {

    @Parameter
    private Files files;

    @Parameter
    private Annotations annotations;

    @Parameter
    private Java java;

    @Parameter
    private Logging logging;

    @Parameter
    private Methods methods;

    @Parameter
    private Repositories repositories;

    @Parameter
    private Statements statements;

    @Parameter
    private Resources resources;

    @Parameter
    private Variables variables;

    @Parameter
    private Packages packages;

    @Parameter
    private Results results;

    @Parameter
    private Jdbc jdbc;

    @Parameter
    private RxJava rxJava;

    @Override
    public void execute() {
        final var configuration = createConfiguration();
        final var yosql = createYoSql(configuration);

        yosql.generateCode();
    }

    private YoSql createYoSql(final RuntimeConfiguration configuration) {
        return DaggerConfigurableYoSqlComponent.builder()
                .runtimeConfiguration(configuration)
                .build()
                .yosql();
    }

    private RuntimeConfiguration createConfiguration() {
        final var utilityPackage = packages.getUtilityPackageName();
        return RuntimeConfiguration.builder()
                .setFiles(files.asConfiguration())
                .setAnnotations(annotations.asConfiguration())
                .setJava(java.asConfiguration())
                .setLogging(logging.asConfiguration())
                .setMethods(methods.asConfiguration())
                .setRepositories(repositories.asConfiguration())
                .setResources(resources.asConfiguration())
                .setVariables(variables.asConfiguration())
                .setStatements(statements.asConfiguration())
                .setNames(packages.asConfiguration())
                .setResult(results.asConfiguration(utilityPackage))
                .setJdbcNames(jdbc.namesConfiguration())
                .setJdbcFields(jdbc.fieldsConfiguration())
                .setRxJava(rxJava.asConfiguration(utilityPackage))
                .build();
    }

}
