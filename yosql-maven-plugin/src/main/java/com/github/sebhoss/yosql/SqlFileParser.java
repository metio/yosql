package com.github.sebhoss.yosql;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.yaml.snakeyaml.Yaml;

@Named
@Singleton
public class SqlFileParser {

    public static final String        PARAMETER_REGEX = "(?<!')(:[\\w]*)(?!')";
    public static final Pattern       PATTERN         = Pattern.compile(PARAMETER_REGEX);

    private final Yaml                yamlParser      = new Yaml();
    private final PluginErrors        pluginErrors;
    private final PluginRuntimeConfig runtimeConfig;

    @Inject
    public SqlFileParser(final PluginErrors pluginErrors, final PluginRuntimeConfig runtimeConfig) {
        this.pluginErrors = pluginErrors;
        this.runtimeConfig = runtimeConfig;
    }

    public SqlStatement parse(final SqlSourceFile source) {
        final StringBuilder yaml = new StringBuilder();
        final StringBuilder sql = new StringBuilder();

        splitUpYamlAndSql(source.getPathToSqlFile(), yaml::append, sql::append);

        final String rawSqlStatement = sql.toString();
        final String rawYaml = yaml.toString();

        final Map<String, List<Integer>> parameterIndices = extractParameterNames(rawSqlStatement);
        final SqlStatementConfiguration configuration = createStatementConfiguration(source, rawYaml,
                parameterIndices);

        return new SqlStatement(configuration, rawSqlStatement);
    }

    private Map<String, List<Integer>> extractParameterNames(final String sqlStatement) {
        final Map<String, List<Integer>> indices = new LinkedHashMap<>();
        final Matcher matcher = PATTERN.matcher(sqlStatement);
        int counter = 0;
        while (matcher.find()) {
            counter++;
            final String parameterName = matcher.group().substring(1);
            if (!indices.containsKey(parameterName)) {
                indices.put(parameterName, new ArrayList<>());
            }
            indices.get(parameterName).add(Integer.valueOf(counter));
        }
        return indices;
    }

    private SqlStatementConfiguration createStatementConfiguration(final SqlSourceFile source, final String yaml,
            final Map<String, List<Integer>> parameterIndices) {
        final SqlStatementConfiguration configuration = loadOrCreateConfig(yaml);
        final String fileName = getFileNameWithoutExtension(source.getPathToSqlFile());
        if (nullOrEmpty(configuration.getName())) {
            configuration.setName(fileName);
        }
        if (nullOrEmpty(configuration.getBatchPrefix())) {
            configuration.setBatchPrefix(runtimeConfig.getBatchPrefix());
        }
        if (nullOrEmpty(configuration.getBatchSuffix())) {
            configuration.setBatchSuffix(runtimeConfig.getBatchSuffix());
        }
        if (nullOrEmpty(configuration.getStreamPrefix())) {
            configuration.setStreamPrefix(runtimeConfig.getStreamPrefix());
        }
        if (nullOrEmpty(configuration.getStreamSuffix())) {
            configuration.setStreamSuffix(runtimeConfig.getStreamSuffix());
        }
        if (nullOrEmpty(configuration.getReactivePrefix())) {
            configuration.setReactivePrefix(runtimeConfig.getReactivePrefix());
        }
        if (nullOrEmpty(configuration.getReactiveSuffix())) {
            configuration.setReactiveSuffix(runtimeConfig.getReactiveSuffix());
        }
        if (nullOrEmpty(configuration.getLazyName())) {
            configuration.setLazyName(runtimeConfig.getLazyName());
        }
        if (nullOrEmpty(configuration.getEagerName())) {
            configuration.setEagerName(runtimeConfig.getEagerName());
        }
        if (configuration.getType() == null) {
            if (startsWith(configuration.getName(), runtimeConfig.getAllowedWritePrefixes())) {
                configuration.setType(SqlStatementType.WRITING);
            } else {
                configuration.setType(SqlStatementType.READING);
            }
        }
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
        if (configuration.isUsingPluginSingleConfig()) {
            configuration.setSingle(runtimeConfig.isGenerateSingleQueryApi());
        }
        if (configuration.isUsingPluginBatchConfig()) {
            if (SqlStatementType.READING == configuration.getType()) {
                configuration.setBatch(false);
            } else {
                configuration.setBatch(runtimeConfig.isGenerateBatchApi());
            }
        }
        if (configuration.isUsingPluginStreamEagerConfig()) {
            if (SqlStatementType.WRITING == configuration.getType()) {
                configuration.setStreamEager(false);
            } else {
                configuration.setStreamEager(runtimeConfig.isGenerateStreamEagerApi());
            }
        }
        if (configuration.isUsingPluginStreamLazyConfig()) {
            if (SqlStatementType.WRITING == configuration.getType()) {
                configuration.setStreamLazy(false);
            } else {
                configuration.setStreamLazy(runtimeConfig.isGenerateStreamLazyApi());
            }
        }
        if (configuration.isUsingPluginRxJavaConfig()) {
            if (SqlStatementType.WRITING == configuration.getType()) {
                configuration.setGenerateRxJavaApi(false);
            } else {
                configuration.setGenerateRxJavaApi(runtimeConfig.isGenerateRxJavaApi());
            }
        }
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
        if (validateParameterConfig(source, parameterIndices, configuration)) {
            for (final Entry<String, List<Integer>> entry : parameterIndices.entrySet()) {
                final String parameterName = entry.getKey();
                if (!configuration.getParameters().stream()
                        .filter(parameter -> parameterName.equals(parameter.getName()))
                        .findAny().isPresent()) {
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
        return configuration;
    }

    private boolean nullOrEmpty(final String object) {
        return object == null || object.isEmpty();
    }

    private boolean startsWith(final String fileName, final String... prefixes) {
        return Arrays.stream(prefixes)
                .anyMatch(fileName::startsWith);
    }

    private boolean validateParameterConfig(
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

    private void splitUpYamlAndSql(final Path path, final Consumer<String> yaml, final Consumer<String> sql) {
        try (final BufferedReader reader = Files.newBufferedReader(path)) {
            for (String line; (line = reader.readLine()) != null;) {
                if (line.startsWith("--")) {
                    yaml.accept(line.substring(2));
                    yaml.accept("\n");
                } else {
                    sql.accept(line);
                    sql.accept("\n");
                }
            }
        } catch (final IOException exception) {
            pluginErrors.add(exception);
        }
    }

}
