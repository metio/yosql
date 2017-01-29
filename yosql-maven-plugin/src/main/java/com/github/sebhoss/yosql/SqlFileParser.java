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
        if (configuration.getName() == null || configuration.getName().isEmpty()) {
            configuration.setName(fileName);
        }
        if (configuration.getBatchPrefix() == null || configuration.getBatchPrefix().isEmpty()) {
            configuration.setBatchPrefix(runtimeConfig.getBatchPrefix());
        }
        if (configuration.getBatchSuffix() == null || configuration.getBatchSuffix().isEmpty()) {
            configuration.setBatchSuffix(runtimeConfig.getBatchSuffix());
        }
        if (configuration.getStreamPrefix() == null || configuration.getStreamPrefix().isEmpty()) {
            configuration.setStreamPrefix(runtimeConfig.getStreamPrefix());
        }
        if (configuration.getStreamSuffix() == null || configuration.getStreamSuffix().isEmpty()) {
            configuration.setStreamSuffix(runtimeConfig.getStreamSuffix());
        }
        if (configuration.getReactivePrefix() == null || configuration.getReactivePrefix().isEmpty()) {
            configuration.setReactivePrefix(runtimeConfig.getReactivePrefix());
        }
        if (configuration.getReactiveSuffix() == null || configuration.getReactiveSuffix().isEmpty()) {
            configuration.setReactiveSuffix(runtimeConfig.getReactiveSuffix());
        }
        if (configuration.getLazyName() == null || configuration.getLazyName().isEmpty()) {
            configuration.setLazyName(runtimeConfig.getLazyName());
        }
        if (configuration.getEagerName() == null || configuration.getEagerName().isEmpty()) {
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
            configuration.setSingle(runtimeConfig.isGenerateBatchApi());
        }
        if (configuration.isUsingPluginBatchConfig()) {
            configuration.setBatch(runtimeConfig.isGenerateBatchApi());
        }
        if (configuration.isUsingPluginStreamEagerConfig()) {
            configuration.setStreamEager(runtimeConfig.isGenerateStreamEagerApi());
        }
        if (configuration.isUsingPluginStreamLazyConfig()) {
            configuration.setStreamLazy(runtimeConfig.isGenerateStreamLazyApi());
        }
        if (configuration.isUsingPluginRxJavaConfig()) {
            configuration.setGenerateRxJavaApi(runtimeConfig.isGenerateRxJavaApi());
        }
        if (configuration.getRepository() == null || configuration.getRepository().isEmpty()) {
            final Path relativePath = source.getBaseDirectory().relativize(source.getPathToSqlFile());
            final Path fullyQualifiedPath = relativePath.getParent();
            final String fullyQualifiedRepositoryName = fullyQualifiedPath.toString();
            configuration.setRepository(fullyQualifiedRepositoryName);
        } else {
            final String userGivenRepository = configuration.getRepository();
            final String cleanedRepository = userGivenRepository.replace(".", "/");
            configuration.setRepository(cleanedRepository);
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
