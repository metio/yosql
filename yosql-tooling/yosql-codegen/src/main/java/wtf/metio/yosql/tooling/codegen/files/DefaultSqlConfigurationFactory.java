/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.codegen.files;

import ch.qos.cal10n.IMessageConveyor;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.slf4j.cal10n.LocLogger;
import org.yaml.snakeyaml.Yaml;
import wtf.metio.yosql.tooling.codegen.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.tooling.codegen.model.errors.ExecutionErrors;
import wtf.metio.yosql.tooling.codegen.model.errors.ValidationErrors;
import wtf.metio.yosql.tooling.codegen.model.internal.ApplicationEvents;
import wtf.metio.yosql.tooling.codegen.model.sql.*;
import wtf.metio.yosql.tooling.codegen.utils.Strings;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DefaultSqlConfigurationFactory implements SqlConfigurationFactory {

    private final LocLogger logger;
    private final RuntimeConfiguration runtimeConfiguration;
    private final ExecutionErrors errors;
    private final IMessageConveyor messages;

    /**
     * @param logger               The logger to use.
     * @param runtimeConfiguration The runtime config to use.
     * @param errors               The error collector to use.
     * @param messages             The messages to use.
     */
    public DefaultSqlConfigurationFactory(
            final LocLogger logger,
            final RuntimeConfiguration runtimeConfiguration,
            final ExecutionErrors errors, IMessageConveyor messages) {
        this.runtimeConfiguration = runtimeConfiguration;
        this.errors = errors;
        this.logger = logger;
        this.messages = messages;
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
        final var configuration = loadConfig(new Yaml(), yaml);

        name(configuration, source, statementInFile);
        batchNamePrefix(configuration);
        batchNameSuffix(configuration);
        streamNamePrefix(configuration);
        streamNameSuffix(configuration);
        rxJavaNamePrefix(configuration);
        rxJavaNameSuffix(configuration);
        lazyName(configuration);
        eagerName(configuration);
        type(configuration);
        returningMode(configuration);

        validateNames(source, configuration);

        standard(configuration);
        batch(configuration);
        streamEager(configuration);
        streamLazy(configuration);
        rxJava(configuration);
        catchAndRethrow(configuration);
        repository(source, configuration);
        parameters(source, parameterIndices, configuration);
        // parameterConverters(); // TODO: Configure parameter converters
        resultConverter(configuration);

        return configuration;
    }

    private static void name(final SqlConfiguration configuration, final Path source, final int statementInFile) {
        if (nullOrEmpty(configuration.getName())) {
            final String fileName = getFileNameWithoutExtension(source);
            configuration.setName(statementInFile > 1 ? fileName + statementInFile : fileName);
        }
    }

    private void batchNamePrefix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodBatchPrefix())) {
            configuration.setMethodBatchPrefix(runtimeConfiguration.repositories().methodBatchPrefix());
        }
    }

    private void batchNameSuffix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodBatchSuffix())) {
            configuration.setMethodBatchSuffix(runtimeConfiguration.repositories().methodBatchSuffix());
        }
    }

    private void streamNamePrefix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodStreamPrefix())) {
            configuration.setMethodStreamPrefix(runtimeConfiguration.repositories().methodStreamPrefix());
        }
    }

    private void streamNameSuffix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodStreamSuffix())) {
            configuration.setMethodStreamSuffix(runtimeConfiguration.repositories().methodStreamSuffix());
        }
    }

    private void rxJavaNamePrefix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodReactivePrefix())) {
            configuration.setMethodReactivePrefix(runtimeConfiguration.repositories().methodRxJavaPrefix());
        }
    }

    private void rxJavaNameSuffix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodReactiveSuffix())) {
            configuration.setMethodReactiveSuffix(runtimeConfiguration.repositories().methodRxJavaSuffix());
        }
    }

    private void lazyName(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodLazyName())) {
            configuration.setMethodLazyName(runtimeConfiguration.repositories().methodLazyName());
        }
    }

    private void eagerName(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodEagerName())) {
            configuration.setMethodEagerName(runtimeConfiguration.repositories().methodEagerName());
        }
    }

    private void type(final SqlConfiguration configuration) {
        if (configuration.getType() == null) {
            if (startsWith(configuration.getName(), runtimeConfiguration.repositories().allowedWritePrefixes())) {
                configuration.setType(SqlType.WRITING);
            } else if (startsWith(configuration.getName(), runtimeConfiguration.repositories().allowedReadPrefixes())) {
                configuration.setType(SqlType.READING);
            } else if (startsWith(configuration.getName(), runtimeConfiguration.repositories().allowedCallPrefixes())) {
                configuration.setType(SqlType.CALLING);
            } else {
                configuration.setType(SqlType.UNKNOWN);
            }
        }
    }

    private void returningMode(final SqlConfiguration configuration) {
        switch (configuration.getType()) {
            case READING -> configuration.setReturningMode(ReturningMode.LIST);
            case WRITING -> configuration.setReturningMode(ReturningMode.ONE);
            default -> configuration.setReturningMode(ReturningMode.NONE);
        }
    }

    private void validateNames(final Path source, final SqlConfiguration configuration) {
        if (runtimeConfiguration.repositories().validateMethodNamePrefixes()) {
            switch (configuration.getType()) {
                case READING:
                    if (!startsWith(configuration.getName(), runtimeConfiguration.repositories().allowedReadPrefixes())) {
                        invalidPrefix(source, SqlType.READING, configuration.getName());
                    }
                    break;
                case WRITING:
                    if (!startsWith(configuration.getName(), runtimeConfiguration.repositories().allowedWritePrefixes())) {
                        invalidPrefix(source, SqlType.WRITING, configuration.getName());
                    }
                    break;
                case CALLING:
                    if (!startsWith(configuration.getName(), runtimeConfiguration.repositories().allowedCallPrefixes())) {
                        invalidPrefix(source, SqlType.CALLING, configuration.getName());
                    }
                    break;
                default:
                    errors.illegalArgument(messages.getMessage(ValidationErrors.UNSUPPORTED_TYPE, source, configuration.getType()));
                    break;
            }
        }
    }

    private void invalidPrefix(final Path source, final SqlType sqlType, final String name) {
        errors.illegalArgument(messages.getMessage(ValidationErrors.INVALID_PREFIX, source, sqlType, name));
    }

    private void standard(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginStandardConfig()) {
            configuration.setMethodStandardApi(runtimeConfiguration.repositories().generateStandardApi());
        }
    }

    private void batch(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginBatchConfig()) {
            if (SqlType.READING == configuration.getType()) {
                configuration.setMethodBatchApi(false);
            } else {
                configuration.setMethodBatchApi(runtimeConfiguration.repositories().generateBatchApi());
            }
        }
    }

    private void streamEager(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginStreamEagerConfig()) {
            if (SqlType.WRITING == configuration.getType()) {
                configuration.setMethodStreamEagerApi(false);
            } else {
                configuration.setMethodStreamEagerApi(runtimeConfiguration.repositories().generateStreamEagerApi());
            }
        }
    }

    private void streamLazy(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginStreamLazyConfig()) {
            if (SqlType.WRITING == configuration.getType()) {
                configuration.setMethodStreamLazyApi(false);
            } else {
                configuration.setMethodStreamLazyApi(runtimeConfiguration.repositories().generateStreamLazyApi());
            }
        }
    }

    private void rxJava(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginRxJavaConfig()) {
            if (SqlType.WRITING == configuration.getType()) {
                configuration.setMethodRxJavaApi(false);
            } else {
                configuration.setMethodRxJavaApi(runtimeConfiguration.repositories().generateRxJavaApi());
            }
        }
    }

    private void catchAndRethrow(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginCatchAndRethrowConfig()) {
            configuration.setMethodCatchAndRethrow(runtimeConfiguration.repositories().methodCatchAndRethrow());
        }
    }

    private void repository(final Path source, final SqlConfiguration configuration) {
        if (configuration.hasRepository()) {
            configuration.setRepository(calculateRepositoryNameFromUserInput(configuration));
        } else {
            configuration.setRepository(calculateRepositoryNameFromParentFolder(source));
        }
    }

    private String calculateRepositoryNameFromParentFolder(final Path source) {
        logger.debug(ApplicationEvents.REPOSITORY_NAME_CALC_INPUT, runtimeConfiguration.files().inputBaseDirectory());
        logger.debug(ApplicationEvents.REPOSITORY_NAME_CALC_SOURCE, source);
        final var relativePathToSqlFile = runtimeConfiguration.files().inputBaseDirectory().relativize(source);
        logger.debug(ApplicationEvents.REPOSITORY_NAME_CALC_RELATIVE, relativePathToSqlFile);
        final var rawRepositoryName = relativePathToSqlFile.getParent().toString();
        logger.debug(ApplicationEvents.REPOSITORY_NAME_CALC_RAW, rawRepositoryName);
        final var dottedRepositoryName = rawRepositoryName.replace(File.separator, ".");
        logger.debug(ApplicationEvents.REPOSITORY_NAME_CALC_DOTTED, dottedRepositoryName);
        final var upperCaseName = upperCaseFirstLetterInLastSegment(dottedRepositoryName);
        logger.debug(ApplicationEvents.REPOSITORY_NAME_CALC_UPPER, upperCaseName);
        final var actualRepository = repositoryInBasePackage(upperCaseName);
        logger.debug(ApplicationEvents.REPOSITORY_NAME_CALC_ACTUAL, actualRepository);
        final var repository = repositoryWithNameSuffix(actualRepository);
        logger.debug(ApplicationEvents.REPOSITORY_NAME_CALC_NAME, repository);
        return repository;
    }

    private static String upperCaseFirstLetterInLastSegment(final String name) {
        if (name.contains(".")) {
            return name.substring(0, name.lastIndexOf('.') + 1)
                    + name.substring(name.lastIndexOf('.') + 1, name.lastIndexOf('.') + 2).toUpperCase()
                    + name.substring(name.lastIndexOf('.') + 2);
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private String calculateRepositoryNameFromUserInput(final SqlConfiguration configuration) {
        final var userGivenRepository = configuration.getRepository();
        final var actualRepository = repositoryInBasePackage(userGivenRepository);
        return repositoryWithNameSuffix(actualRepository);
    }

    private String repositoryWithNameSuffix(final String repository) {
        return repository.endsWith(runtimeConfiguration.repositories().repositoryNameSuffix())
                ? repository
                : repository + runtimeConfiguration.repositories().repositoryNameSuffix();
    }

    private String repositoryInBasePackage(final String repository) {
        return repository.startsWith(runtimeConfiguration.repositories().basePackageName())
                ? repository
                : runtimeConfiguration.repositories().basePackageName() + "." + repository;
    }

    private static boolean isMissingParameter(final SqlConfiguration configuration, final String parameterName) {
        return Stream.ofNullable(configuration.getParameters()).flatMap(Collection::stream).noneMatch(nameMatches(parameterName));
    }

    private static List<SqlParameter> updateIndices(
            final List<SqlParameter> parameters,
            final List<Integer> indices,
            final String parameterName) {
        return parameters.stream()
                .filter(nameMatches(parameterName))
                .map(parameter -> SqlParameter.builder()
                        .setName(parameterName)
                        .setIndices(asIntArray(indices))
                        .setType(parameter.type())
                        .setConverter(parameter.converter())
                        .build())
                .collect(Collectors.toList());
    }

    private static int[] asIntArray(final List<Integer> numbers) {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    private static boolean methodNamesMatch(
            final ResultRowConverter resultConverter,
            final ResultRowConverter converter) {
        return converter.methodName().equals(resultConverter.methodName());
    }

    private static Predicate<? super SqlParameter> nameMatches(final String parameterName) {
        return parameter -> parameterName.equals(parameter.name());
    }

    private static SqlConfiguration loadConfig(final Yaml yamlParser, final String yaml) {
        SqlConfiguration configuration = null;
        try {
            if (!yaml.isBlank()) {
                final var yoSqlModule = new SimpleModule();
                yoSqlModule.addDeserializer(SqlParameter.class, new SqlParameterDeserializer());
                yoSqlModule.addDeserializer(ResultRowConverter.class, new ResultRowConverterDeserializer());
                var mapper = YAMLMapper.builder().addModule(yoSqlModule).build();
                configuration = mapper.readValue(yaml, SqlConfiguration.class);
            }
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
        if (configuration == null) {
            configuration = new SqlConfiguration();
        }
        return configuration;
    }

    private void parameters(
            final Path source,
            final Map<String, List<Integer>> parameterIndices,
            final SqlConfiguration configuration) {
        if (parametersAreValid(source, parameterIndices, configuration)) {
            for (final var entry : parameterIndices.entrySet()) {
                final var parameterName = entry.getKey();
                if (isMissingParameter(configuration, parameterName)) {
                    final var sqlParameter = SqlParameter.builder()
                            .setName(parameterName)
                            .setIndices(asIntArray(entry.getValue()))
                            .setType(Object.class.getName())
                            .setConverter("defaultConverter")
                            .build();
                    if (configuration.getParameters() != null) {
                        configuration.getParameters().add(sqlParameter);
                    } else {
                        configuration.setParameters(List.of(sqlParameter));
                    }
                }
                final var currentParameters = configuration.getParameters();
                configuration.setParameters(updateIndices(currentParameters, entry.getValue(), parameterName));
            }
        }
    }

    private String getDefaultAlias(final ResultRowConverter resultConverter) {
        return getConverterFieldOrEmptyString(
                converter -> converterTypeMatches(resultConverter, converter),
                ResultRowConverter::alias);
    }

    private String getDefaultConverterType(final ResultRowConverter resultConverter) {
        return getConverterFieldOrEmptyString(
                converter -> aliasMatches(resultConverter, converter),
                ResultRowConverter::converterType);
    }

    private String getDefaultResultType(final ResultRowConverter resultConverter) {
        return getConverterFieldOrEmptyString(
                converter -> aliasMatches(resultConverter, converter)
                        || converterTypeMatches(resultConverter, converter),
                ResultRowConverter::resultType);
    }

    private static boolean aliasMatches(
            final ResultRowConverter resultConverter,
            final ResultRowConverter converter) {
        return converter.alias().equals(resultConverter.alias());
    }

    private void resultConverter(final SqlConfiguration configuration) {
        if (configuration.getResultRowConverter() == null) {
            getDefaultRowConverter().ifPresent(configuration::setResultRowConverter);
        } else {
            final var currentConverter = configuration.getResultRowConverter();
            final var converter = ResultRowConverter.builder()
                    .setAlias(Strings.isBlank(currentConverter.alias()) ? getDefaultAlias(currentConverter) : currentConverter.alias())
                    .setConverterType(Strings.isBlank(currentConverter.converterType()) ? getDefaultConverterType(currentConverter) : currentConverter.converterType())
                    .setResultType(Strings.isBlank(currentConverter.resultType()) ? getDefaultResultType(currentConverter) : currentConverter.resultType())
                    .setMethodName(Strings.isBlank(currentConverter.methodName()) ?
                            getDefaultMethodName(currentConverter) : currentConverter.methodName())
                    .build();
            configuration.setResultRowConverter(converter);
        }
    }

    private static boolean converterTypeMatches(final ResultRowConverter resultConverter,
                                                final ResultRowConverter converter) {
        return converter.converterType().equals(resultConverter.converterType());
    }

    private Optional<ResultRowConverter> getDefaultRowConverter() {
        return getRowConverter(this::isDefaultConverter);
    }

    private boolean isDefaultConverter(final ResultRowConverter converter) {
        final var defaultConverter = runtimeConfiguration.converter().defaultConverter();
        final var defaultAlias = defaultConverter.alias();
        final var defaultType = defaultConverter.converterType();
        return converter.alias().equals(defaultAlias) || converter.converterType().equals(defaultType);
    }

    private Optional<ResultRowConverter> getRowConverter(final Predicate<ResultRowConverter> predicate) {
        return runtimeConfiguration.converter().converters().stream()
                .filter(predicate)
                .findFirst();
    }

    private String getConverterFieldOrEmptyString(
            final Predicate<ResultRowConverter> predicate,
            final Function<ResultRowConverter, String> mapper) {
        return runtimeConfiguration.converter().converters().stream()
                .filter(predicate)
                .map(mapper)
                .findFirst()
                .orElse("");
    }

    private static boolean nullOrEmpty(final String object) {
        return object == null || object.isEmpty();
    }

    private static boolean startsWith(final String fileName, final List<String> prefixes) {
        return prefixes != null && prefixes.stream().anyMatch(fileName::startsWith);
    }

    private String getDefaultMethodName(final ResultRowConverter resultConverter) {
        return getConverterFieldOrEmptyString(
                converter -> methodNamesMatch(resultConverter, converter),
                ResultRowConverter::alias);
    }

    private boolean parametersAreValid(
            final Path source,
            final Map<String, List<Integer>> parameterIndices,
            final SqlConfiguration configuration) {
        final var parameterErrors = Stream.ofNullable(configuration.getParameters())
                .flatMap(Collection::stream)
                .filter(param -> !parameterIndices.containsKey(param.name()))
                .map(param -> messages.getMessage(ValidationErrors.UNKNOWN_PARAMETER, source, param.name()))
                .peek(errors::illegalArgument)
                .peek(logger::error)
                .collect(Collectors.toList());
        return parameterErrors.isEmpty();
    }

    private static String getFileNameWithoutExtension(final Path path) {
        final var fileName = path.getFileName().toString();
        final var dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
    }

}
