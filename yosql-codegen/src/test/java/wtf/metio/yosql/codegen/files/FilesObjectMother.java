/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.codegen.orchestration.OrchestrationObjectMother;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.testing.configs.FilesConfigurations;

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

    public static MethodConverterConfigurer methodConverterConfigurer(final ConverterConfiguration converter) {
        return new DefaultMethodConverterConfigurer(converter);
    }

    public static RepositoryNameConfigurer repositoryNameConfigurer(final RepositoriesConfiguration repositories) {
        return new DefaultRepositoryNameConfigurer(
                LoggingObjectMother.logger(),
                FilesConfigurations.defaults(),
                repositories);
    }

    public static SqlConfigurationParser sqlConfigurationParser() {
        return new DefaultSqlConfigurationParser();
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
