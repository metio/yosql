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
import wtf.metio.yosql.DaggerYoSqlComponent;
import wtf.metio.yosql.YoSql;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.errors.ExecutionErrors;

import javax.inject.Inject;

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
    private Names names;

    @Parameter
    private Results results;

    @Parameter
    private JdbcNames jdbcNames;

    @Parameter
    private RxJava rxJava;
    // TODO: Support Spring React
    // TODO: Support Microprofile SmallRye

    @Override
    public void execute() {
        final var configuration = createConfiguration();
        final var yosql = createYoSql(configuration);

        yosql.generateCode();
    }

    private YoSql createYoSql(final RuntimeConfiguration configuration) {
        // Allow to pass in config
        return DaggerYoSqlComponent.builder().build().yosql();
    }

    private RuntimeConfiguration createConfiguration() {
        final var utilityPackage = names.getUtilityPackageName();
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
                .setNames(names.asConfiguration())
                .setResult(results.asConfiguration(utilityPackage))
                .setJdbcNames(jdbcNames.asConfiguration())
                .setRxJava(rxJava.asConfiguration(utilityPackage))
                .build();
    }

}
