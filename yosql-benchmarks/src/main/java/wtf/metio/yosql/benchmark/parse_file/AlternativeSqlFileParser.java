/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark.parse_file;

import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.inject.Inject;

import wtf.metio.yosql.model.ExecutionConfiguration;
import wtf.metio.yosql.model.ExecutionErrors;
import wtf.metio.yosql.model.SqlConfiguration;
import wtf.metio.yosql.model.SqlStatement;
import wtf.metio.yosql.parser.SqlConfigurationFactory;
import wtf.metio.yosql.parser.SqlFileParser;

/**
 * Alternative implementation for the SqlFileParser. Used for benchmarking.
 */
final class AlternativeSqlFileParser implements SqlFileParser {

    private static final String           NEWLINE = "\n"; //$NON-NLS-1$

    private final ExecutionErrors         errors;
    private final SqlConfigurationFactory factory;
    private final ExecutionConfiguration  config;

    @Inject
    AlternativeSqlFileParser(
            final ExecutionErrors errors,
            final ExecutionConfiguration config,
            final SqlConfigurationFactory factory) {
        this.errors = errors;
        this.config = config;
        this.factory = factory;
    }

    @Override
    public Stream<SqlStatement> parse(final Path source) {
        try {
            final Charset charset = Charset.forName(config.sqlFilesCharset());
            final Path pathToSqlFile = source;
            final String rawText = new String(Files.readAllBytes(pathToSqlFile), charset);
            final String[] rawStatements = rawText.split(config.sqlStatementSeparator());
            final AtomicInteger counter = new AtomicInteger(0);
            return Arrays.stream(rawStatements)
                    // .parallel() // CHANGED FROM ORIGINAL
                    .map(statement -> convert(source, statement, counter.getAndIncrement()));
        } catch (final Throwable exception) {
            errors.add(exception);
            return Stream.empty();
        }
    }

    private SqlStatement convert(
            final Path source,
            final String rawStatement,
            final int statementInFile) {
        final StringBuilder yaml = new StringBuilder();
        final StringBuilder sql = new StringBuilder();

        splitUpYamlAndSql(rawStatement, yaml::append, sql::append);

        final String rawSqlStatement = sql.toString();
        final String rawYaml = yaml.toString();

        final Map<String, List<Integer>> parameterIndices = extractParameterIndices(rawSqlStatement);
        SqlConfiguration configuration = null;
        try {
            configuration = factory.createStatementConfiguration(source, rawYaml,
                    parameterIndices, statementInFile);
        } catch (final Throwable exception) {
            errors.add(exception);
        }

        return new SqlStatement(source, configuration, rawSqlStatement);
    }

    private static void splitUpYamlAndSql(final String rawStatement, final Consumer<String> yaml,
            final Consumer<String> sql) {
        new BufferedReader(new StringReader(rawStatement))
                .lines().forEach(line -> {
                    if (line.startsWith("--")) { //$NON-NLS-1$
                        yaml.accept(line.substring(2));
                        yaml.accept(NEWLINE);
                    } else {
                        sql.accept(line);
                        sql.accept(NEWLINE);
                    }
                });
    }

}
