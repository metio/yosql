/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.parser;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.yaml.snakeyaml.Yaml;

import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.ResultRowConverter;
import de.xn__ho_hia.yosql.model.SqlConfiguration;
import de.xn__ho_hia.yosql.model.SqlParameter;
import de.xn__ho_hia.yosql.model.SqlType;

@SuppressWarnings({ "javadoc", "nls" })
public class SqlConfigurationFactory {

    private final ExecutionErrors        errors;
    private final ExecutionConfiguration config;

    @Inject
    public SqlConfigurationFactory(final ExecutionErrors errors, final ExecutionConfiguration config) {
        this.errors = errors;
        this.config = config;
    }

    public SqlConfiguration createStatementConfiguration(
            final Path source,
            final String yaml,
            final Map<String, List<Integer>> parameterIndices,
            final int statementInFile) {
        final SqlConfiguration configuration = loadConfig(new Yaml(), yaml);

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
            configuration.setName(statementInFile == 1 ? fileName : fileName + statementInFile);
        }
    }

    private void batchNamePrefix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodBatchPrefix())) {
            configuration.setMethodBatchPrefix(config.methodBatchPrefix());
        }
    }

    private void batchNameSuffix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodBatchSuffix())) {
            configuration.setMethodBatchSuffix(config.methodBatchSuffix());
        }
    }

    private void streamNamePrefix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodStreamPrefix())) {
            configuration.setMethodStreamPrefix(config.methodStreamPrefix());
        }
    }

    private void streamNameSuffix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodStreamSuffix())) {
            configuration.setMethodStreamSuffix(config.methodStreamSuffix());
        }
    }

    private void rxJavaNamePrefix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodReactivePrefix())) {
            configuration.setMethodReactivePrefix(config.methodRxJavaPrefix());
        }
    }

    private void rxJavaNameSuffix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodReactiveSuffix())) {
            configuration.setMethodReactiveSuffix(config.methodRxJavaSuffix());
        }
    }

    private void lazyName(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodLazyName())) {
            configuration.setMethodLazyName(config.methodLazyName());
        }
    }

    private void eagerName(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodEagerName())) {
            configuration.setMethodEagerName(config.methodEagerName());
        }
    }

    private void type(final SqlConfiguration configuration) {
        if (configuration.getType() == null) {
            if (startsWith(configuration.getName(), config.allowedWritePrefixes())) {
                configuration.setType(SqlType.WRITING);
            } else if (startsWith(configuration.getName(), config.allowedReadPrefixes())) {
                configuration.setType(SqlType.READING);
            } else if (startsWith(configuration.getName(), config.allowedCallPrefixes())) {
                configuration.setType(SqlType.CALLING);
            }
        }
    }

    private void validateNames(final Path source, final SqlConfiguration configuration) {
        if (config.validateMethodNamePrefixes()) {
            switch (configuration.getType()) {
                case READING:
                    if (!startsWith(configuration.getName(), config.allowedReadPrefixes())) {
                        invalidPrefix(source, SqlType.READING, configuration.getName());
                    }
                    break;
                case WRITING:
                    if (!startsWith(configuration.getName(), config.allowedWritePrefixes())) {
                        invalidPrefix(source, SqlType.WRITING, configuration.getName());
                    }
                    break;
                case CALLING:
                    if (!startsWith(configuration.getName(), config.allowedCallPrefixes())) {
                        invalidPrefix(source, SqlType.CALLING, configuration.getName());
                    }
                    break;
                default:
                    errors.illegalArgument("[%s] has unsupported type [%s]",
                            source, configuration.getType());
                    break;
            }
        }
    }

    private void invalidPrefix(final Path source, final SqlType sqlType, final String name) {
        errors.illegalArgument("[%s] has invalid %s prefix in its name [%s]", source, sqlType, name);
    }

    private void standard(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginStandardConfig()) {
            configuration.setMethodStandardApi(config.generateStandardApi());
        }
    }

    private void batch(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginBatchConfig()) {
            if (SqlType.READING == configuration.getType()) {
                configuration.setMethodBatchApi(false);
            } else {
                configuration.setMethodBatchApi(config.generateBatchApi());
            }
        }
    }

    private void streamEager(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginStreamEagerConfig()) {
            if (SqlType.WRITING == configuration.getType()) {
                configuration.setMethodStreamEagerApi(false);
            } else {
                configuration.setMethodStreamEagerApi(config.generateStreamEagerApi());
            }
        }
    }

    private void streamLazy(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginStreamLazyConfig()) {
            if (SqlType.WRITING == configuration.getType()) {
                configuration.setMethodStreamLazyApi(false);
            } else {
                configuration.setMethodStreamLazyApi(config.generateStreamLazyApi());
            }
        }
    }

    private void rxJava(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginRxJavaConfig()) {
            if (SqlType.WRITING == configuration.getType()) {
                configuration.setMethodRxJavaApi(false);
            } else {
                configuration.setMethodRxJavaApi(config.generateRxJavaApi());
            }
        }
    }

    private void catchAndRethrow(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginCatchAndRethrowConfig()) {
            configuration.setMethodCatchAndRethrow(config.methodCatchAndRethrow());
        }
    }

    private void repository(final Path source, final SqlConfiguration configuration) {
        if (configuration.hasRepository()) {
            final String repositoryName = calculateRepositoryNameFromUserInput(configuration);
            configuration.setRepository(repositoryName);
        } else {
            final String fullRepositoryName = calculateRepositoryNameFromParentFolder(source);
            configuration.setRepository(fullRepositoryName);
        }
    }

    private String calculateRepositoryNameFromParentFolder(final Path source) {
        final Path relativePathToSqlFile = config.inputBaseDirectory().relativize(source);
        final String rawRepositoryName = relativePathToSqlFile.getParent().toString();
        final String dottedRepositoryName = rawRepositoryName.replace("/", ".");
        final String upperCaseName = upperCaseFirstLetterInLastSegment(dottedRepositoryName);
        final String actualRepository = repositoryInBasePackage(upperCaseName);
        return repositoryWithNameSuffix(actualRepository);
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
        final String userGivenRepository = configuration.getRepository();
        final String actualRepository = repositoryInBasePackage(userGivenRepository);
        return repositoryWithNameSuffix(actualRepository);
    }

    private String repositoryWithNameSuffix(final String repository) {
        return repository.endsWith(config.repositoryNameSuffix())
                ? repository
                : repository + config.repositoryNameSuffix();
    }

    private String repositoryInBasePackage(final String repository) {
        return repository.startsWith(config.basePackageName())
                ? repository
                : config.basePackageName() + "." + repository;
    }

    private void parameters(final Path source, final Map<String, List<Integer>> parameterIndices,
            final SqlConfiguration configuration) {
        if (parametersAreValid(source, parameterIndices, configuration)) {
            for (final Entry<String, List<Integer>> entry : parameterIndices.entrySet()) {
                final String parameterName = entry.getKey();
                if (isMissingParameter(configuration, parameterName)) {
                    final SqlParameter sqlParameter = new SqlParameter();
                    sqlParameter.setName(parameterName);
                    configuration.getParameters().add(sqlParameter);
                }
                updateIndices(configuration.getParameters(), entry.getValue(), parameterName);
            }
        }
    }

    private static void updateIndices(
            final List<SqlParameter> parameters,
            final List<Integer> indices,
            final String parameterName) {
        parameters.stream()
                .filter(nameMatches(parameterName))
                .forEach(parameter -> parameter.setIndices(asArray(indices)));
    }

    private static int[] asArray(final List<Integer> numbers) {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    private static boolean isMissingParameter(final SqlConfiguration configuration, final String parameterName) {
        return !configuration.getParameters().stream()
                .anyMatch(nameMatches(parameterName));
    }

    private static Predicate<? super SqlParameter> nameMatches(final String parameterName) {
        return parameter -> parameterName.equals(parameter.getName());
    }

    private void resultConverter(final SqlConfiguration configuration) {
        if (configuration.getResultRowConverter() == null) {
            getDefaultRowConverter().ifPresent(configuration::setResultRowConverter);
        } else {
            final ResultRowConverter resultConverter = configuration.getResultRowConverter();
            if (resultConverter.getAlias() == null || resultConverter.getAlias().isEmpty()) {
                resultConverter.setAlias(getDefaultAlias(resultConverter));
            }
            if (resultConverter.getConverterType() == null || resultConverter.getConverterType().isEmpty()) {
                resultConverter.setConverterType(getDefaultConverterType(resultConverter));
            }
            if (resultConverter.getResultType() == null || resultConverter.getResultType().isEmpty()) {
                resultConverter.setResultType(getDefaultResultType(resultConverter));
            }
        }
    }

    private String getDefaultAlias(final ResultRowConverter resultConverter) {
        return getConverterFieldOrEmptyString(
                converter -> converterTypeMatches(resultConverter, converter),
                ResultRowConverter::getAlias);
    }

    private String getDefaultConverterType(final ResultRowConverter resultConverter) {
        return getConverterFieldOrEmptyString(
                converter -> aliasMatches(resultConverter, converter),
                ResultRowConverter::getConverterType);
    }

    private String getDefaultResultType(final ResultRowConverter resultConverter) {
        return getConverterFieldOrEmptyString(
                converter -> aliasMatches(resultConverter, converter)
                        || converterTypeMatches(resultConverter, converter),
                ResultRowConverter::getResultType);
    }

    private static boolean aliasMatches(final ResultRowConverter resultConverter, final ResultRowConverter converter) {
        return converter.getAlias().equals(resultConverter.getAlias());
    }

    private static boolean converterTypeMatches(final ResultRowConverter resultConverter,
            final ResultRowConverter converter) {
        return converter.getConverterType().equals(resultConverter.getConverterType());
    }

    private Optional<ResultRowConverter> getDefaultRowConverter() {
        return getRowConverter(this::isDefaultConverter);
    }

    private boolean isDefaultConverter(final ResultRowConverter converter) {
        return config.defaultRowConverter().equals(converter.getAlias())
                || config.defaultRowConverter().equals(converter.getConverterType());
    }

    private Optional<ResultRowConverter> getRowConverter(final Predicate<ResultRowConverter> predicate) {
        return Optional.ofNullable(config.resultRowConverters())
                .map(List::stream)
                .orElse(Stream.empty())
                .filter(predicate)
                .findFirst();
    }

    private String getConverterFieldOrEmptyString(
            final Predicate<ResultRowConverter> predicate,
            final Function<ResultRowConverter, String> mapper) {
        return config.resultRowConverters().stream()
                .filter(predicate)
                .map(mapper)
                .findFirst().orElse("");
    }

    private static boolean nullOrEmpty(final String object) {
        return object == null || object.isEmpty();
    }

    private static boolean startsWith(final String fileName, final List<String> prefixes) {
        return prefixes != null && prefixes.stream()
                .anyMatch(fileName::startsWith);
    }

    private boolean parametersAreValid(
            final Path source,
            final Map<String, List<Integer>> parameterIndices,
            final SqlConfiguration configuration) {
        final List<String> parameterErrors = configuration.getParameters().stream()
                .filter(param -> !parameterIndices.containsKey(param.getName()))
                .map(param -> String.format("[%s] declares unknown parameter [%s]",
                        source, param.getName()))
                .peek(msg -> errors.add(new IllegalArgumentException(msg)))
                // .peek(msg -> pluginConfig.getLogger().error(msg))
                .collect(Collectors.toList());
        return parameterErrors == null || parameterErrors.isEmpty();
    }

    private static SqlConfiguration loadConfig(final Yaml yamlParser, final String yaml) {
        SqlConfiguration configuration = yamlParser.loadAs(yaml,
                SqlConfiguration.class);
        if (configuration == null) {
            configuration = new SqlConfiguration();
        }
        return configuration;
    }

    private static String getFileNameWithoutExtension(final Path path) {
        final String fileName = path.getFileName().toString();
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

}
