/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.dagger.files;

import ch.qos.cal10n.IMessageConveyor;
import dagger.Module;
import dagger.Provides;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.files.*;
import wtf.metio.yosql.codegen.orchestration.ExecutionErrors;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.annotations.Parser;
import wtf.metio.yosql.tooling.dagger.annotations.Reader;

import javax.inject.Singleton;

/**
 * Dagger module that provides all necessary classes to parse files.
 */
@Module
public class DefaultFilesModule {

    @Provides
    @Singleton
    SqlConfigurationParser provideSqlConfigurationParser(final ExecutionErrors errors) {
        return new DefaultSqlConfigurationParser(errors);
    }

    @Provides
    @Singleton
    MethodSettingsConfigurer provideMethodSettingsConfigurer(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultMethodSettingsConfigurer(runtimeConfiguration.repositories());
    }

    @Provides
    @Singleton
    MethodNameConfigurer provideMethodNameConfigurer(
            @Parser final LocLogger logger,
            final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultMethodNameConfigurer(logger, runtimeConfiguration.repositories());
    }

    @Provides
    @Singleton
    MethodNameValidator provideMethodNameValidator(
            final RuntimeConfiguration runtimeConfiguration,
            final ExecutionErrors errors,
            final IMessageConveyor messages) {
        return new DefaultMethodNameValidator(runtimeConfiguration.repositories(), errors, messages);
    }

    @Provides
    @Singleton
    MethodApiConfigurer provideMethodApiConfigurer(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultMethodApiConfigurer(runtimeConfiguration.repositories());
    }

    @Provides
    @Singleton
    MethodParameterConfigurer provideMethodParameterConfigurer(
            @Parser final LocLogger logger,
            final ExecutionErrors errors,
            final IMessageConveyor messages) {
        return new DefaultMethodParameterConfigurer(logger, errors, messages);
    }

    @Provides
    @Singleton
    MethodResultRowConverterConfigurer provideMethodConverterConfigurer(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultMethodResultRowConverterConfigurer(runtimeConfiguration.converter());
    }

    @Provides
    @Singleton
    RepositoryNameConfigurer provideRepositoryNameConfigurer(
            @Parser final LocLogger logger,
            final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultRepositoryNameConfigurer(logger, runtimeConfiguration.files(), runtimeConfiguration.repositories());
    }

    @Provides
    @Singleton
    SqlConfigurationFactory provideSqlConfigurationFactory(
            @Parser final LocLogger logger,
            final SqlConfigurationParser configParser,
            final MethodSettingsConfigurer methodSettings,
            final MethodNameConfigurer methodNames,
            final MethodNameValidator methodNameValidator,
            final MethodApiConfigurer methodApis,
            final MethodParameterConfigurer methodParameters,
            final MethodResultRowConverterConfigurer methodConverter,
            final RepositoryNameConfigurer repositoryName) {
        return new DefaultSqlConfigurationFactory(
                logger,
                configParser,
                methodSettings,
                methodNames,
                methodNameValidator,
                methodApis,
                methodParameters,
                methodConverter,
                repositoryName);
    }

    @Provides
    @Singleton
    SqlStatementParser provideSqlFileParser(
            @Parser final LocLogger logger,
            final SqlConfigurationFactory factory,
            final RuntimeConfiguration runtimeConfiguration,
            final ExecutionErrors errors) {
        return new DefaultSqlStatementParser(logger, factory, runtimeConfiguration.files(), errors);
    }

    @Provides
    @Singleton
    FileParser provideFileParser(
            @Reader final LocLogger logger,
            final RuntimeConfiguration runtimeConfiguration,
            final ExecutionErrors errors,
            final SqlStatementParser fileParser) {
        return new DefaultFileParser(
                logger,
                runtimeConfiguration.files(),
                errors,
                fileParser);
    }

    @Provides
    @Singleton
    CodegenPreconditions provideParserPreconditions(
            final ExecutionErrors errors,
            final IMessageConveyor messages) {
        return new DefaultCodegenPreconditions(errors, messages);
    }

}
