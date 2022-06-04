/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.files;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.internals.jdk.FileNames;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of the {@link SqlConfigurationFactory}.
 */
public final class DefaultSqlConfigurationFactory implements SqlConfigurationFactory {

    private final LocLogger logger;
    private final SqlConfigurationParser configParser;
    private final MethodSettingsConfigurer methodSettings;
    private final MethodNameConfigurer methodNames;
    private final MethodNameValidator methodNameValidator;
    private final MethodApiConfigurer methodApis;
    private final MethodParameterConfigurer methodParameters;
    private final MethodConverterConfigurer methodConverter;
    private final RepositoryNameConfigurer repositoryName;

    /**
     * @param logger              The logger to use.
     * @param configParser        The configuration parser to use.
     * @param methodSettings      How method settings are configured.
     * @param methodNames         How method names are configured.
     * @param methodNameValidator Validator for method names.
     * @param methodApis          How method APIs are configured.
     * @param methodParameters    How method parameters are configured.
     * @param methodConverter     How method converters are configured.
     * @param repositoryName      How repository names are configured.
     */
    public DefaultSqlConfigurationFactory(
            final LocLogger logger,
            final SqlConfigurationParser configParser,
            final MethodSettingsConfigurer methodSettings,
            final MethodNameConfigurer methodNames,
            final MethodNameValidator methodNameValidator,
            final MethodApiConfigurer methodApis,
            final MethodParameterConfigurer methodParameters,
            final MethodConverterConfigurer methodConverter,
            final RepositoryNameConfigurer repositoryName) {
        this.methodParameters = methodParameters;
        this.logger = logger;
        this.configParser = configParser;
        this.methodSettings = methodSettings;
        this.methodNames = methodNames;
        this.methodNameValidator = methodNameValidator;
        this.methodApis = methodApis;
        this.methodConverter = methodConverter;
        this.repositoryName = repositoryName;
    }

    /**
     * @param source           The source file of the statement.
     * @param yaml             The YAML front matter of the statement.
     * @param parameterIndices The parameter indices (if any) of the statement.
     * @param statementInFile  The counter for statements with the same name in the same source file.
     * @return The resulting configuration.
     */
    @Override
    public SqlConfiguration createConfiguration(
            final Path source,
            final String yaml,
            final Map<String, List<Integer>> parameterIndices,
            final int statementInFile) {
        var configuration = configParser.parseConfig(yaml);
        logger.debug("SQL configuration: {}", configuration);
        configuration = methodNames.configureNames(configuration, FileNames.withoutExtension(source), statementInFile);
        configuration = methodSettings.configureSettings(configuration);
        configuration = methodApis.configureApis(configuration);
        configuration = repositoryName.configureNames(configuration, source);
        configuration = methodParameters.configureParameters(configuration, source, parameterIndices);
        configuration = methodConverter.configureConverter(configuration);
        logger.debug("SQL configuration: {}", configuration);
        methodNameValidator.validateNames(configuration, source);
        return configuration;
    }

}
