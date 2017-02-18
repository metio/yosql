package com.github.sebhoss.yosql.parser;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.yaml.snakeyaml.Yaml;

import com.github.sebhoss.yosql.model.ResultRowConverter;
import com.github.sebhoss.yosql.model.SqlConfiguration;
import com.github.sebhoss.yosql.model.SqlParameter;
import com.github.sebhoss.yosql.model.SqlSourceFile;
import com.github.sebhoss.yosql.model.SqlType;
import com.github.sebhoss.yosql.plugin.PluginConfig;
import com.github.sebhoss.yosql.plugin.PluginErrors;

@Named
@Singleton
public class SqlConfigurationFactory {

    private final Yaml         yamlParser = new Yaml();
    private final PluginErrors pluginErrors;
    private final PluginConfig pluginConfig;

    @Inject
    public SqlConfigurationFactory(final PluginErrors pluginErrors, final PluginConfig pluginConfig) {
        this.pluginErrors = pluginErrors;
        this.pluginConfig = pluginConfig;
    }

    public SqlConfiguration createStatementConfiguration(
            final SqlSourceFile source,
            final String yaml,
            final Map<String, List<Integer>> parameterIndices,
            final int statementInFile) {
        final SqlConfiguration configuration = loadOrCreateConfig(yaml);
        final String fileName = getFileNameWithoutExtension(source.getPathToSqlFile());

        name(configuration, fileName, statementInFile);
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

    private void name(final SqlConfiguration configuration, final String fileName, final int statementInFile) {
        if (nullOrEmpty(configuration.getName())) {
            configuration.setName(statementInFile == 0 ? fileName : fileName + statementInFile);
        }
    }

    private void batchNamePrefix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodBatchPrefix())) {
            configuration.setMethodBatchPrefix(pluginConfig.getMethodBatchPrefix());
        }
    }

    private void batchNameSuffix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodBatchSuffix())) {
            configuration.setMethodBatchSuffix(pluginConfig.getMethodBatchSuffix());
        }
    }

    private void streamNamePrefix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodStreamPrefix())) {
            configuration.setMethodStreamPrefix(pluginConfig.getMethodStreamPrefix());
        }
    }

    private void streamNameSuffix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodStreamSuffix())) {
            configuration.setMethodStreamSuffix(pluginConfig.getMethodStreamSuffix());
        }
    }

    private void rxJavaNamePrefix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodReactivePrefix())) {
            configuration.setMethodReactivePrefix(pluginConfig.getMethodRxJavaPrefix());
        }
    }

    private void rxJavaNameSuffix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodReactiveSuffix())) {
            configuration.setMethodReactiveSuffix(pluginConfig.getMethodRxJavaSuffix());
        }
    }

    private void lazyName(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodLazyName())) {
            configuration.setMethodLazyName(pluginConfig.getMethodLazyName());
        }
    }

    private void eagerName(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodEagerName())) {
            configuration.setMethodEagerName(pluginConfig.getMethodEagerName());
        }
    }

    private void type(final SqlConfiguration configuration) {
        if (configuration.getType() == null) {
            if (startsWith(configuration.getName(), pluginConfig.getAllowedWritePrefixes())) {
                configuration.setType(SqlType.WRITING);
            } else if (startsWith(configuration.getName(), pluginConfig.getAllowedReadPrefixes())) {
                configuration.setType(SqlType.READING);
            } else if (startsWith(configuration.getName(), pluginConfig.getAllowedCallPrefixes())) {
                configuration.setType(SqlType.CALLING);
            }
        }
    }

    private void validateNames(final SqlSourceFile source, final SqlConfiguration configuration) {
        if (pluginConfig.shouldValidateMethodNamePrefixes()) {
            switch (configuration.getType()) {
            case READING:
                if (!startsWith(configuration.getName(), pluginConfig.getAllowedReadPrefixes())) {
                    final String msg = invalidPrefix(source, SqlType.READING, configuration);
                    pluginErrors.add(new IllegalArgumentException(msg));
                    pluginConfig.getLogger().error(msg);
                }
                break;
            case WRITING:
                if (!startsWith(configuration.getName(), pluginConfig.getAllowedWritePrefixes())) {
                    final String msg = invalidPrefix(source, SqlType.WRITING, configuration);
                    pluginErrors.add(new IllegalArgumentException(msg));
                    pluginConfig.getLogger().error(msg);
                }
                break;
            case CALLING:
                if (!startsWith(configuration.getName(), pluginConfig.getAllowedCallPrefixes())) {
                    final String msg = invalidPrefix(source, SqlType.CALLING, configuration);
                    pluginErrors.add(new IllegalArgumentException(msg));
                    pluginConfig.getLogger().error(msg);
                }
                break;
            default:
                final String msg = String.format("[%s] has unsupported type [%s]",
                        source.getPathToSqlFile(), configuration.getType());
                pluginErrors.add(new IllegalArgumentException(msg));
                pluginConfig.getLogger().error(msg);
                break;
            }
        }
    }

    private String invalidPrefix(
            final SqlSourceFile source,
            final SqlType type,
            final SqlConfiguration configuration) {
        return String.format("[%s] has invalid %s prefix in its name [%s]",
                source.getPathToSqlFile(), type, configuration.getName());
    }

    private void standard(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginStandardConfig()) {
            configuration.setMethodStandardApi(pluginConfig.isGenerateStandardApi());
        }
    }

    private void batch(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginBatchConfig()) {
            if (SqlType.READING == configuration.getType()) {
                configuration.setMethodBatchApi(false);
            } else {
                configuration.setMethodBatchApi(pluginConfig.isGenerateBatchApi());
            }
        }
    }

    private void streamEager(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginStreamEagerConfig()) {
            if (SqlType.WRITING == configuration.getType()) {
                configuration.setMethodStreamEagerApi(false);
            } else {
                configuration.setMethodStreamEagerApi(pluginConfig.isGenerateStreamEagerApi());
            }
        }
    }

    private void streamLazy(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginStreamLazyConfig()) {
            if (SqlType.WRITING == configuration.getType()) {
                configuration.setMethodStreamLazyApi(false);
            } else {
                configuration.setMethodStreamLazyApi(pluginConfig.isGenerateStreamLazyApi());
            }
        }
    }

    private void rxJava(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginRxJavaConfig()) {
            if (SqlType.WRITING == configuration.getType()) {
                configuration.setMethodRxJavaApi(false);
            } else {
                configuration.setMethodRxJavaApi(pluginConfig.isGenerateRxJavaApi());
            }
        }
    }

    private void catchAndRethrow(final SqlConfiguration configuration) {
        if (configuration.shouldUsePluginCatchAndRethrowConfig()) {
            configuration.setMethodCatchAndRethrow(pluginConfig.isMethodCatchAndRethrow());
        }
    }

    private void repository(final SqlSourceFile source, final SqlConfiguration configuration) {
        if (configuration.hasRepository()) {
            final String repositoryName = calculateRepositoryNameFromUserInput(configuration);
            configuration.setRepository(repositoryName);
        } else {
            final String fullRepositoryName = calculateRepositoryNameFromParentFolder(source);
            configuration.setRepository(fullRepositoryName);
        }
    }

    private String calculateRepositoryNameFromParentFolder(final SqlSourceFile source) {
        final Path relativePathToSqlFile = source.getBaseDirectory().relativize(source.getPathToSqlFile());
        final String rawRepositoryName = relativePathToSqlFile.getParent().toString();
        final String dottedRepositoryName = rawRepositoryName.replace("/", ".");
        final String upperCaseName = upperCaseFirstLetter(dottedRepositoryName);
        final String actualRepository = repositoryInBasePackage(upperCaseName);
        final String fullRepositoryName = repositoryWithNameSuffix(actualRepository);
        return fullRepositoryName;
    }

    private String upperCaseFirstLetter(final String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
    }

    private String calculateRepositoryNameFromUserInput(final SqlConfiguration configuration) {
        final String userGivenRepository = configuration.getRepository();
        final String actualRepository = repositoryInBasePackage(userGivenRepository);
        return repositoryWithNameSuffix(actualRepository);
    }

    private String repositoryWithNameSuffix(final String repository) {
        return repository.endsWith(pluginConfig.getRepositoryNameSuffix())
                ? repository
                : repository + pluginConfig.getRepositoryNameSuffix();
    }

    private String repositoryInBasePackage(final String repository) {
        return repository.startsWith(pluginConfig.getBasePackageName())
                ? repository : pluginConfig.getBasePackageName() + "." + repository;
    }

    private void parameters(final SqlSourceFile source, final Map<String, List<Integer>> parameterIndices,
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

    private void updateIndices(
            final List<SqlParameter> parameters,
            final List<Integer> numbers,
            final String parameterName) {
        parameters.stream()
                .filter(nameMatches(parameterName))
                .forEach(parameter -> parameter.setIndices(asArray(numbers)));
    }

    private int[] asArray(final List<Integer> numbers) {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    private boolean isMissingParameter(final SqlConfiguration configuration, final String parameterName) {
        return !configuration.getParameters().stream()
                .anyMatch(nameMatches(parameterName));
    }

    private Predicate<? super SqlParameter> nameMatches(final String parameterName) {
        return parameter -> parameterName.equals(parameter.getName());
    }

    private void resultConverter(final SqlConfiguration configuration) {
        if (configuration.getResultConverter() == null) {
            getDefaultRowConverter().ifPresent(configuration::setResultConverter);
        } else {
            final ResultRowConverter resultConverter = configuration.getResultConverter();
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

    private boolean aliasMatches(final ResultRowConverter resultConverter, final ResultRowConverter converter) {
        return converter.getAlias().equals(resultConverter.getAlias());
    }

    private boolean converterTypeMatches(final ResultRowConverter resultConverter, final ResultRowConverter converter) {
        return converter.getConverterType().equals(resultConverter.getConverterType());
    }

    private Optional<ResultRowConverter> getDefaultRowConverter() {
        return getRowConverter(this::isDefaultConverter);
    }

    private boolean isDefaultConverter(final ResultRowConverter converter) {
        return pluginConfig.getDefaultRowConverter().equals(converter.getAlias())
                || pluginConfig.getDefaultRowConverter().equals(converter.getConverterType());
    }

    private Optional<ResultRowConverter> getRowConverter(final Predicate<ResultRowConverter> predicate) {
        return Optional.ofNullable(pluginConfig.getResultRowConverters())
                .map(List::stream)
                .orElse(Stream.empty())
                .filter(predicate)
                .findFirst();
    }

    private String getConverterFieldOrEmptyString(
            final Predicate<ResultRowConverter> predicate,
            final Function<ResultRowConverter, String> mapper) {
        return pluginConfig.getResultRowConverters().stream()
                .filter(predicate)
                .map(mapper)
                .findFirst().orElse("");
    }

    private boolean nullOrEmpty(final String object) {
        return object == null || object.isEmpty();
    }

    private boolean startsWith(final String fileName, final String... prefixes) {
        return prefixes != null && Arrays.stream(prefixes)
                .anyMatch(fileName::startsWith);
    }

    private boolean parametersAreValid(
            final SqlSourceFile source,
            final Map<String, List<Integer>> parameterIndices,
            final SqlConfiguration configuration) {
        final List<String> errors = configuration.getParameters().stream()
                .filter(param -> !parameterIndices.containsKey(param.getName()))
                .map(param -> String.format("[%s] declares unknown parameter [%s]",
                        source.getPathToSqlFile(), param.getName()))
                .peek(msg -> pluginErrors.add(new IllegalArgumentException(msg)))
                .peek(msg -> pluginConfig.getLogger().error(msg))
                .collect(Collectors.toList());
        return errors == null || errors.isEmpty();
    }

    private SqlConfiguration loadOrCreateConfig(final String yaml) {
        SqlConfiguration configuration = yamlParser.loadAs(yaml,
                SqlConfiguration.class);
        if (configuration == null) {
            configuration = new SqlConfiguration();
        }
        return configuration;
    }

    private String getFileNameWithoutExtension(final Path path) {
        final String fileName = path.getFileName().toString();
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

}
