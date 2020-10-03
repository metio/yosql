/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.files;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.model.configuration.FileConfiguration;
import wtf.metio.yosql.model.errors.ExecutionErrors;
import wtf.metio.yosql.model.internal.ApplicationEvents;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * Default SQL file parser.
 */
final class DefaultSqlFileParser implements SqlFileParser {

    private static final String SQL_COMMENT_PREFIX = "--";
    private static final String NEWLINE = "\n";

    private final Pattern statementSplitter;
    private final LocLogger logger;
    private final SqlConfigurationFactory factory;
    private final FileConfiguration fileConfiguration;
    private final ExecutionErrors errors;

    DefaultSqlFileParser(
            final LocLogger logger,
            final SqlConfigurationFactory factory,
            final FileConfiguration fileConfiguration,
            final ExecutionErrors errors) {
        statementSplitter = Pattern.compile(fileConfiguration.sqlStatementSeparator());
        this.logger = logger;
        this.factory = factory;
        this.fileConfiguration = fileConfiguration;
        this.errors = errors;
    }

    @Override
    public Stream<SqlStatement> parse(final Path source) {
        try {
            final var charset = fileConfiguration.sqlFilesCharset();
            final var rawText = Files.readString(source, charset);
            final var counter = new AtomicInteger(1);
            return statementSplitter.splitAsStream(rawText)
                    .parallel()
                    .filter(Objects::nonNull)
                    .map(String::strip)
                    .filter(not(String::isBlank))
                    .map(statement -> parseStatement(source, statement, counter.getAndIncrement()));
        } catch (final IOException exception) {
            errors.add(exception);
            logger.debug(ApplicationEvents.FILE_PARSING_FAILED, source);
            return Stream.empty();
        }
    }

    private SqlStatement parseStatement(
            final Path source,
            final String rawStatement,
            final int statementInFile) {
        final var yaml = new StringBuilder();
        final var sql = new StringBuilder();

        splitUpYamlAndSql(rawStatement, yaml::append, sql::append);

        logger.trace(ApplicationEvents.STATEMENT_PARSING_STARTING, rawStatement);
        final String rawYaml = yaml.toString();
        final String rawSqlStatement = sql.toString();
        logger.trace(ApplicationEvents.STATEMENT_YAML_FRONT_MATTER_PARSED, rawYaml);
        logger.trace(ApplicationEvents.STATEMENT_PARSED, rawSqlStatement);

        final var parameterIndices = extractParameterIndices(rawSqlStatement);
        final var configuration = factory.createStatementConfiguration(source, rawYaml,
                parameterIndices, statementInFile);
        logger.debug(ApplicationEvents.STATEMENT_PARSING_FINISHED, source, configuration.getName());
        return new SqlStatement(source, configuration, rawSqlStatement);
    }

    private void splitUpYamlAndSql(
            final String rawStatement,
            final Consumer<String> yaml,
            final Consumer<String> sql) {
        try (final var reader = new StringReader(rawStatement);
             final var buffer = new BufferedReader(reader)) {
            buffer.lines()
                    .filter(line -> !line.strip().isEmpty())
                    .filter(line -> !SQL_COMMENT_PREFIX.equals(line.strip()))
                    .forEach(line -> {
                        if (line.startsWith(SQL_COMMENT_PREFIX)) {
                            yaml.accept(line.substring(2));
                            yaml.accept(NEWLINE);
                        } else {
                            sql.accept(line);
                            sql.accept(NEWLINE);
                        }
                    });
        } catch (final IOException exception) {
            errors.add(exception);
        }
    }

}
