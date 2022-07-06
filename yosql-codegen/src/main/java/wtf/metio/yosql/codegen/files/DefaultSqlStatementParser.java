/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.files;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.lifecycle.ApplicationErrors;
import wtf.metio.yosql.codegen.lifecycle.ParseLifecycle;
import wtf.metio.yosql.codegen.orchestration.ExecutionErrors;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * Default SQL statement parser.
 */
public final class DefaultSqlStatementParser implements SqlStatementParser {

    private static final String SQL_COMMENT_PREFIX = "--";
    private static final String NEWLINE = "\n";
    private static final String YOSQL_PRAGMA = SQL_COMMENT_PREFIX + "@yosql";
    private static final Pattern CUSTOM_SQL_STATEMENT_SEPARATOR = Pattern.compile(
            YOSQL_PRAGMA + " sqlStatementSeparator: ([^\\n]*)");

    private final Pattern defaultStatementSplitter;
    private final LocLogger logger;
    private final SqlConfigurationFactory factory;
    private final FilesConfiguration files;
    private final ExecutionErrors errors;

    public DefaultSqlStatementParser(
            final LocLogger logger,
            final SqlConfigurationFactory factory,
            final FilesConfiguration files,
            final ExecutionErrors errors) {
        defaultStatementSplitter = Pattern.compile(files.sqlStatementSeparator());
        this.logger = logger;
        this.factory = factory;
        this.files = files;
        this.errors = errors;
    }

    @Override
    public Stream<SqlStatement> parse(final Path source) {
        try {
            final var charset = files.sqlFilesCharset();
            final var rawText = Files.readString(source, charset);
            final var skippedText = skipLines(rawText);
            final var counter = new AtomicInteger(1);
            final var splitter = getStatementSplitter(rawText);
            return splitter.splitAsStream(skippedText)
                    .parallel()
                    .map(String::strip)
                    .filter(not(String::isBlank))
                    .filter(line -> !line.startsWith(YOSQL_PRAGMA))
                    .map(statement -> parseStatement(source, statement, counter.getAndIncrement()));
        } catch (final IOException exception) {
            errors.add(exception);
            logger.debug(ApplicationErrors.FILE_PARSING_FAILED, source);
            return Stream.empty();
        }
    }

    // visible for testing
    Pattern getStatementSplitter(final String text) {
        final var customStatementSeparator = CUSTOM_SQL_STATEMENT_SEPARATOR.matcher(text);
        return customStatementSeparator.find() ?
                Pattern.compile(customStatementSeparator.group(1))
                : defaultStatementSplitter;
    }

    private String skipLines(final String rawText) {
        final var builder = new StringBuilder(rawText);
        for (int index = 0; index < files.skipLines(); index++) {
            builder.delete(0, builder.indexOf(NEWLINE) + 1);
        }
        return builder.toString();
    }

    // visible for testing
    SqlStatement parseStatement(
            final Path source,
            final String rawStatement,
            final int statementInFile) {
        final var yaml = new StringBuilder();
        final var sql = new StringBuilder();

        logger.trace(ParseLifecycle.STATEMENT_PARSING_STARTING, rawStatement);
        splitUpYamlAndSql(rawStatement, yaml::append, sql::append);

        final String rawYaml = yaml.toString();
        final String rawSqlStatement = sql.toString();
        logger.trace(ParseLifecycle.STATEMENT_YAML_FRONT_MATTER_PARSED, rawYaml);
        logger.trace(ParseLifecycle.STATEMENT_PARSED, rawSqlStatement);

        final var parameterIndices = SqlStatementParser.extractParameterIndices(rawSqlStatement);
        final var configuration = factory.createConfiguration(source, rawYaml,
                parameterIndices, statementInFile);
        logger.debug(ParseLifecycle.STATEMENT_PARSING_FINISHED, source, configuration.name().orElse("unknown name"));

        return SqlStatement.builder()
                .setSourcePath(source)
                .setConfiguration(configuration)
                .setRawStatement(rawSqlStatement)
                .build();
    }

    private void splitUpYamlAndSql(
            final String rawStatement,
            final Consumer<String> yaml,
            final Consumer<String> sql) {
        try (final var reader = new StringReader(rawStatement);
             final var buffer = new BufferedReader(reader)) {
            buffer.lines()
                    .filter(line -> !line.isBlank())
                    .filter(line -> !SQL_COMMENT_PREFIX.equals(line.strip()))
                    .filter(line -> !line.startsWith(YOSQL_PRAGMA))
                    .forEach(line -> split(yaml, sql, line));
        } catch (final IOException exception) {
            errors.add(exception);
        }
    }

    private static void split(
            final Consumer<String> yaml,
            final Consumer<String> sql,
            final String line) {
        if (line.startsWith(SQL_COMMENT_PREFIX)) {
            yaml.accept(line.substring(2));
            yaml.accept(NEWLINE);
        } else {
            sql.accept(line);
            sql.accept(NEWLINE);
        }
    }

}
