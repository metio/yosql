package com.github.sebhoss.yosql;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class SqlFileParser {

    public static final String                     PARAMETER_REGEX = "(?<!')(:[\\w]*)(?!')";
    public static final Pattern                    PATTERN         = Pattern.compile(PARAMETER_REGEX);

    private final PluginErrors                     pluginErrors;
    private final SqlStatementConfigurationFactory factory;

    @Inject
    public SqlFileParser(final PluginErrors pluginErrors, final PluginRuntimeConfig runtimeConfig,
            final SqlStatementConfigurationFactory factory) {
        this.pluginErrors = pluginErrors;
        this.factory = factory;
    }

    public SqlStatement parse(final SqlSourceFile source) {
        final StringBuilder yaml = new StringBuilder();
        final StringBuilder sql = new StringBuilder();

        splitUpYamlAndSql(source.getPathToSqlFile(), yaml::append, sql::append);

        final String rawSqlStatement = sql.toString();
        final String rawYaml = yaml.toString();

        final Map<String, List<Integer>> parameterIndices = extractParameterNames(rawSqlStatement);
        final SqlStatementConfiguration configuration = factory.createStatementConfiguration(source, rawYaml,
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
