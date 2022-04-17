/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.files;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.errors.ExecutionErrors;
import wtf.metio.yosql.codegen.lifecycle.ApplicationErrors;
import wtf.metio.yosql.codegen.lifecycle.ParseLifecycle;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
public final class DefaultSqlFileParser implements SqlFileParser {

    private static final String SQL_COMMENT_PREFIX = "--";
    private static final String NEWLINE = "\n";

    private final Pattern statementSplitter;
    private final LocLogger logger;
    private final SqlConfigurationFactory factory;
    private final FilesConfiguration files;
    private final ExecutionErrors errors;

    public DefaultSqlFileParser(
            final LocLogger logger,
            final SqlConfigurationFactory factory,
            final FilesConfiguration files,
            final ExecutionErrors errors) {
        statementSplitter = Pattern.compile(files.sqlStatementSeparator());
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
            return statementSplitter.splitAsStream(skippedText)
                    .parallel()
                    .filter(Objects::nonNull)
                    .map(String::strip)
                    .filter(not(String::isBlank))
                    .map(statement -> parseStatement(source, statement, counter.getAndIncrement()));
        } catch (final IOException exception) {
            errors.add(exception);
            logger.debug(ApplicationErrors.FILE_PARSING_FAILED, source);
            return Stream.empty();
        }
    }

    private String skipLines(final String rawText) {
        final var builder = new StringBuilder(rawText);
        for (int index = 0; index < files.skipLines(); index++) {
            builder.delete(0, builder.indexOf("\n") + 1);
        }
        return builder.toString();
    }

    private SqlStatement parseStatement(
            final Path source,
            final String rawStatement,
            final int statementInFile) {
        final var yaml = new StringBuilder();
        final var sql = new StringBuilder();

        splitUpYamlAndSql(rawStatement, yaml::append, sql::append);

        logger.trace(ParseLifecycle.STATEMENT_PARSING_STARTING, rawStatement);
        final String rawYaml = yaml.toString();
        final String rawSqlStatement = sql.toString();
        logger.trace(ParseLifecycle.STATEMENT_YAML_FRONT_MATTER_PARSED, rawYaml);
        logger.trace(ParseLifecycle.STATEMENT_PARSED, rawSqlStatement);

        final var parameterIndices = extractParameterIndices(rawSqlStatement);
        final var configuration = factory.createConfiguration(source, rawYaml,
                parameterIndices, statementInFile);
        logger.debug(ParseLifecycle.STATEMENT_PARSING_FINISHED, source, configuration.name());
        return SqlStatement.builder()
                .setConfiguration(configuration)
                .setRawStatement(rawSqlStatement)
                .setSourcePath(source)
                .build();
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
                    .forEach(line -> split(yaml, sql, line));
        } catch (final IOException exception) {
            errors.add(exception);
        }
    }

    private void split(
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
