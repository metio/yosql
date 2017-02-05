package com.github.sebhoss.yosql;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.yaml.snakeyaml.Yaml;

@Named
@Singleton
public class SqlStatementConfigurationFactory {

    private final Yaml                yamlParser = new Yaml();
    private final PluginErrors        pluginErrors;
    private final PluginRuntimeConfig runtimeConfig;

    @Inject
    public SqlStatementConfigurationFactory(final PluginErrors pluginErrors, final PluginRuntimeConfig runtimeConfig) {
        this.pluginErrors = pluginErrors;
        this.runtimeConfig = runtimeConfig;
    }

    public SqlStatementConfiguration createStatementConfiguration(
            final SqlSourceFile source,
            final String yaml,
            final Map<String, List<Integer>> parameterIndices,
            final int statementInFile) {
        final SqlStatementConfiguration configuration = loadOrCreateConfig(yaml);
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
        // parameterConverters();
        resultConverter(configuration);

        return configuration;
    }

    private void name(final SqlStatementConfiguration configuration, final String fileName, final int statementInFile) {
        if (nullOrEmpty(configuration.getName())) {
            configuration.setName(statementInFile == 0 ? fileName : fileName + statementInFile);
        }
    }

    private void batchNamePrefix(final SqlStatementConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodBatchPrefix())) {
            configuration.setMethodBatchPrefix(runtimeConfig.getMethodBatchPrefix());
        }
    }

    private void batchNameSuffix(final SqlStatementConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodBatchSuffix())) {
            configuration.setMethodBatchSuffix(runtimeConfig.getMethodBatchSuffix());
        }
    }

    private void streamNamePrefix(final SqlStatementConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodStreamPrefix())) {
            configuration.setMethodStreamPrefix(runtimeConfig.getMethodStreamPrefix());
        }
    }

    private void streamNameSuffix(final SqlStatementConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodStreamSuffix())) {
            configuration.setMethodStreamSuffix(runtimeConfig.getMethodStreamSuffix());
        }
    }

    private void rxJavaNamePrefix(final SqlStatementConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodReactivePrefix())) {
            configuration.setMethodReactivePrefix(runtimeConfig.getMethodRxJavaPrefix());
        }
    }

    private void rxJavaNameSuffix(final SqlStatementConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodReactiveSuffix())) {
            configuration.setMethodReactiveSuffix(runtimeConfig.getMethodRxJavaSuffix());
        }
    }

    private void lazyName(final SqlStatementConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodLazyName())) {
            configuration.setMethodLazyName(runtimeConfig.getMethodLazyName());
        }
    }

    private void eagerName(final SqlStatementConfiguration configuration) {
        if (nullOrEmpty(configuration.getMethodEagerName())) {
            configuration.setMethodEagerName(runtimeConfig.getMethodEagerName());
        }
    }

    private void type(final SqlStatementConfiguration configuration) {
        if (configuration.getType() == null) {
            if (startsWith(configuration.getName(), runtimeConfig.getAllowedWritePrefixes())) {
                configuration.setType(SqlStatementType.WRITING);
            } else {
                configuration.setType(SqlStatementType.READING);
            }
        }
    }

    private void validateNames(final SqlSourceFile source, final SqlStatementConfiguration configuration) {
        if (runtimeConfig.isValidateMethodNamePrefixes()) {
            switch (configuration.getType()) {
            case READING:
                if (!startsWith(configuration.getName(), runtimeConfig.getAllowedReadPrefixes())) {
                    final String msg = String.format("[%s] has invalid READ prefix in its name [%s]",
                            source.getPathToSqlFile(), configuration.getName());
                    pluginErrors.add(new IllegalArgumentException(msg));
                    runtimeConfig.getLogger().error(msg);
                }
                break;
            case WRITING:
                if (!startsWith(configuration.getName(), runtimeConfig.getAllowedWritePrefixes())) {
                    final String msg = String.format("[%s] has invalid WRITE prefix in its name [%s]",
                            source.getPathToSqlFile(), configuration.getName());
                    pluginErrors.add(new IllegalArgumentException(msg));
                    runtimeConfig.getLogger().error(msg);
                }
                break;
            }
        }
    }

    private void standard(final SqlStatementConfiguration configuration) {
        if (configuration.isUsingPluginSingleConfig()) {
            configuration.setGenerateStandardApi(runtimeConfig.isGenerateStandardApi());
        }
    }

    private void batch(final SqlStatementConfiguration configuration) {
        if (configuration.isUsingPluginBatchConfig()) {
            if (SqlStatementType.READING == configuration.getType()) {
                configuration.setGenerateBatchApi(false);
            } else {
                configuration.setGenerateBatchApi(runtimeConfig.isGenerateBatchApi());
            }
        }
    }

    private void streamEager(final SqlStatementConfiguration configuration) {
        if (configuration.isUsingPluginStreamEagerConfig()) {
            if (SqlStatementType.WRITING == configuration.getType()) {
                configuration.setGenerateStreamEagerApi(false);
            } else {
                configuration.setGenerateStreamEagerApi(runtimeConfig.isGenerateStreamEagerApi());
            }
        }
    }

    private void streamLazy(final SqlStatementConfiguration configuration) {
        if (configuration.isUsingPluginStreamLazyConfig()) {
            if (SqlStatementType.WRITING == configuration.getType()) {
                configuration.setGenerateStreamLazyApi(false);
            } else {
                configuration.setGenerateStreamLazyApi(runtimeConfig.isGenerateStreamLazyApi());
            }
        }
    }

    private void rxJava(final SqlStatementConfiguration configuration) {
        if (configuration.isUsingPluginRxJavaConfig()) {
            if (SqlStatementType.WRITING == configuration.getType()) {
                configuration.setGenerateRxJavaApi(false);
            } else {
                configuration.setGenerateRxJavaApi(runtimeConfig.isGenerateRxJavaApi());
            }
        }
    }

    private void catchAndRethrow(final SqlStatementConfiguration configuration) {
        if (configuration.isUsingPluginCatchAndRethrowConfig()) {
            configuration.setMethodCatchAndRethrow(runtimeConfig.isMethodCatchAndRethrow());
        }
    }

    private void repository(final SqlSourceFile source, final SqlStatementConfiguration configuration) {
        if (configuration.getRepository() == null || configuration.getRepository().isEmpty()) {
            final Path relativePathToSqlFile = source.getBaseDirectory().relativize(source.getPathToSqlFile());
            final String rawRepositoryName = relativePathToSqlFile.getParent().toString();
            final String dottedRepositoryName = rawRepositoryName.replace("/", ".");
            final String upperCaseName = dottedRepositoryName.substring(0, 1).toUpperCase() +
                    dottedRepositoryName.substring(1, dottedRepositoryName.length());
            final String fullRepositoryName = upperCaseName.endsWith(runtimeConfig.getRepositoryNameSuffix())
                    ? upperCaseName
                    : upperCaseName + runtimeConfig.getRepositoryNameSuffix();
            configuration.setRepository(runtimeConfig.getBasePackageName() + "." + fullRepositoryName);
        } else {
            final String userGivenRepository = configuration.getRepository();
            final String actualRepository = userGivenRepository.startsWith(runtimeConfig.getBasePackageName())
                    ? userGivenRepository : runtimeConfig.getBasePackageName() + "." + userGivenRepository;
            final String fullRepositoryName = actualRepository.endsWith(runtimeConfig.getRepositoryNameSuffix())
                    ? actualRepository
                    : actualRepository + runtimeConfig.getRepositoryNameSuffix();
            configuration.setRepository(fullRepositoryName);
        }
    }

    private void parameters(final SqlSourceFile source, final Map<String, List<Integer>> parameterIndices,
            final SqlStatementConfiguration configuration) {
        if (parametersAreValid(source, parameterIndices, configuration)) {
            for (final Entry<String, List<Integer>> entry : parameterIndices.entrySet()) {
                final String parameterName = entry.getKey();
                if (!configuration.getParameters().stream()
                        .anyMatch(parameter -> parameterName.equals(parameter.getName()))) {
                    final SqlParameter sqlParameter = new SqlParameter();
                    sqlParameter.setName(parameterName);
                    configuration.getParameters().add(sqlParameter);
                }
                configuration.getParameters().stream()
                        .filter(parameter -> parameterName.equals(parameter.getName()))
                        .forEach(parameter -> parameter.setIndices(entry.getValue().stream()
                                .mapToInt(Integer::intValue)
                                .toArray()));
            }
        }
    }

    private void resultConverter(final SqlStatementConfiguration configuration) {
        if (configuration.getResultConverter() == null) {
            runtimeConfig.getResultRowConverters().stream()
                    // TODO: expose "resultRow" as mojo parameter
                    .filter(config -> "resultRow".equals(config.getAlias()))
                    .limit(1)
                    .forEach(config -> {
                        final ResultRowConverter converter = new ResultRowConverter();
                        converter.name = config.getAlias();
                        converter.converterType = config.getConverterClass();
                        converter.resultType = config.getResultType();
                        configuration.setResultConverter(converter);
                    });
        }
    }

    private boolean nullOrEmpty(final String object) {
        return object == null || object.isEmpty();
    }

    private boolean startsWith(final String fileName, final String... prefixes) {
        return Arrays.stream(prefixes)
                .anyMatch(fileName::startsWith);
    }

    private boolean parametersAreValid(
            final SqlSourceFile source,
            final Map<String, List<Integer>> parameterIndices,
            final SqlStatementConfiguration configuration) {
        final List<String> errors = configuration.getParameters().stream()
                .filter(param -> !parameterIndices.containsKey(param.getName()))
                .map(param -> String.format("[%s] declares unknown parameter [%s]",
                        source.getPathToSqlFile(), param.getName()))
                .peek(msg -> pluginErrors.add(new IllegalArgumentException(msg)))
                .peek(msg -> runtimeConfig.getLogger().error(msg))
                .collect(Collectors.toList());
        return errors == null || errors.isEmpty();
    }

    private SqlStatementConfiguration loadOrCreateConfig(final String yaml) {
        SqlStatementConfiguration configuration = yamlParser.loadAs(yaml,
                SqlStatementConfiguration.class);
        if (configuration == null) {
            configuration = new SqlStatementConfiguration();
        }
        return configuration;
    }

    private String getFileNameWithoutExtension(final Path path) {
        final String fileName = path.getFileName().toString();
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

}
