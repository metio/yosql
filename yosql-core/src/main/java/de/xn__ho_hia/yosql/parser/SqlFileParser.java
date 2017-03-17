/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.parser;

import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.SqlConfiguration;
import de.xn__ho_hia.yosql.model.SqlSourceFile;
import de.xn__ho_hia.yosql.model.SqlStatement;

@Named
@Singleton
@SuppressWarnings({ "javadoc", "nls" })
public class SqlFileParser {

    public static final String            PARAMETER_REGEX = "(?<!')(:[\\w]*)(?!')";
    public static final Pattern           PATTERN         = Pattern.compile(PARAMETER_REGEX);

    private static final String           NEWLINE         = "\n";

    private final ExecutionErrors            pluginErrors;
    private final SqlConfigurationFactory factory;
    private final ExecutionConfiguration            pluginConfig;

    @Inject
    public SqlFileParser(
            final ExecutionErrors pluginErrors,
            final ExecutionConfiguration pluginConfig,
            final SqlConfigurationFactory factory) {
        this.pluginErrors = pluginErrors;
        this.pluginConfig = pluginConfig;
        this.factory = factory;
    }

    public Stream<SqlStatement> parse(final SqlSourceFile source) {
        try {
            final Charset charset = Charset.forName(pluginConfig.getSqlFilesCharset());
            final Path pathToSqlFile = source.getPathToSqlFile();
            final String rawText = Files.lines(pathToSqlFile, charset)
                    .filter(this::isNotEmpty)
                    .collect(joining(NEWLINE));
            final String[] rawStatements = rawText.split(pluginConfig.getSqlStatementSeparator());
            final AtomicInteger counter = new AtomicInteger(0);
            return Arrays.stream(rawStatements).map(statement -> convert(source, statement, counter.getAndIncrement()));
        } catch (final Throwable exception) {
            pluginErrors.add(exception);
            return Stream.empty();
        }
    }

    private boolean isNotEmpty(final String input) {
        return input != null && !input.trim().isEmpty();
    }

    private SqlStatement convert(
            final SqlSourceFile source,
            final String rawStatement,
            final int statementInFile) {
        final StringBuilder yaml = new StringBuilder();
        final StringBuilder sql = new StringBuilder();

        splitUpYamlAndSql(rawStatement, yaml::append, sql::append);

        final String rawSqlStatement = sql.toString();
        final String rawYaml = yaml.toString();

        final Map<String, List<Integer>> parameterIndices = extractParameterIndices(rawSqlStatement);
        final SqlConfiguration configuration = factory.createStatementConfiguration(source, rawYaml,
                parameterIndices, statementInFile);

        return new SqlStatement(configuration, rawSqlStatement);
    }

    private static void splitUpYamlAndSql(final String rawStatement, final Consumer<String> yaml,
            final Consumer<String> sql) {
        new BufferedReader(new StringReader(rawStatement))
                .lines().forEach(line -> {
                    if (line.startsWith("--")) {
                        yaml.accept(line.substring(2));
                        yaml.accept(NEWLINE);
                    } else {
                        sql.accept(line);
                        sql.accept(NEWLINE);
                    }
                });
    }

    private static Map<String, List<Integer>> extractParameterIndices(final String sqlStatement) {
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

}
