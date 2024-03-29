/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.codegen.orchestration.OrchestrationObjectMother;
import wtf.metio.yosql.internals.testing.configs.FilesConfigurations;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;

/**
 * Object mother for classes in the .files package.
 */
public final class FilesObjectMother {

    public static MethodSettingsConfigurer methodSettingsConfigurer(final RepositoriesConfiguration repositories) {
        return new DefaultMethodSettingsConfigurer(repositories);
    }

    public static MethodNameConfigurer methodNameConfigurer(final RepositoriesConfiguration repositories) {
        return new DefaultMethodNameConfigurer(LoggingObjectMother.logger(), repositories);
    }

    public static MethodApiConfigurer methodApiConfigurer(final RepositoriesConfiguration repositories) {
        return new DefaultMethodApiConfigurer(repositories);
    }

    public static MethodParameterConfigurer methodParameterConfigurer() {
        return new DefaultMethodParameterConfigurer(
                LoggingObjectMother.logger(),
                OrchestrationObjectMother.executionErrors(),
                LoggingObjectMother.messages());
    }

    public static MethodNameValidator methodNameValidator(final RepositoriesConfiguration repositories) {
        return new DefaultMethodNameValidator(
                repositories,
                OrchestrationObjectMother.executionErrors(),
                LoggingObjectMother.messages());
    }

    public static MethodResultRowConverterConfigurer methodConverterConfigurer(final ConverterConfiguration converter) {
        return new DefaultMethodResultRowConverterConfigurer(converter);
    }

    public static RepositoryNameConfigurer repositoryNameConfigurer(final RepositoriesConfiguration repositories) {
        return new DefaultRepositoryNameConfigurer(
                LoggingObjectMother.logger(),
                FilesConfigurations.defaults(),
                repositories);
    }

    public static SqlConfigurationParser sqlConfigurationParser() {
        return new DefaultSqlConfigurationParser(OrchestrationObjectMother.executionErrors());
    }

    public static SqlConfigurationFactory sqlConfigurationFactory(
            final RepositoriesConfiguration repositories,
            final ConverterConfiguration converter) {
        return new DefaultSqlConfigurationFactory(
                LoggingObjectMother.logger(),
                sqlConfigurationParser(),
                methodSettingsConfigurer(repositories),
                methodNameConfigurer(repositories),
                methodNameValidator(repositories),
                methodApiConfigurer(repositories),
                methodParameterConfigurer(),
                methodConverterConfigurer(converter),
                repositoryNameConfigurer(repositories));
    }

    public static SqlStatementParser sqlStatementParser(
            final RepositoriesConfiguration repositories,
            final ConverterConfiguration converter) {
        return new DefaultSqlStatementParser(
                LoggingObjectMother.logger(),
                sqlConfigurationFactory(repositories, converter),
                FilesConfigurations.defaults(),
                OrchestrationObjectMother.executionErrors());
    }

    private FilesObjectMother() {
        // factory class
    }

}
