/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.files;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.internal.ApplicationEvents;
import wtf.metio.yosql.model.errors.ExecutionErrors;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Default SQL file parser.
 */
final class DefaultSqlFileParser implements SqlFileParser {

    private static final String SQL_COMMENT_PREFIX = "--";
    private static final String NEWLINE = "\n";

    private final Pattern statementSplitter;
    private final LocLogger logger;
    private final SqlConfigurationFactory factory;
    private final RuntimeConfiguration runtime;
    private final ExecutionErrors errors;

    DefaultSqlFileParser(
            final LocLogger logger,
            final SqlConfigurationFactory factory,
            final RuntimeConfiguration runtime,
            final ExecutionErrors errors) {
        statementSplitter = Pattern.compile(runtime.files().sqlStatementSeparator());
        this.logger = logger;
        this.factory = factory;
        this.runtime = runtime;
        this.errors = errors;
    }

    @Override
    public Stream<SqlStatement> parse(final Path source) {
        try {
            final var charset = Charset.forName(runtime.files().sqlFilesCharset());
            final var rawText = Files.readString(source, charset);
            final var counter = new AtomicInteger(1);
            return statementSplitter.splitAsStream(rawText)
                    .parallel()
                    .filter(text -> text != null && !text.trim().isEmpty())
                    .map(statement -> convert(source, statement, counter.getAndIncrement()));
        } catch (final IOException exception) {
            // ToDO: use some factory method of 'errors'
            errors.add(exception);
            logger.debug(ApplicationEvents.FILE_PARSING_FAILED, source);
            return Stream.empty();
        }
    }

    private SqlStatement convert(
            final Path source,
            final String rawStatement,
            final int statementInFile) {
        final var yaml = new StringBuilder();
        final var sql = new StringBuilder();

        splitUpYamlAndSql(rawStatement, yaml::append, sql::append);

        logger.trace(ApplicationEvents.FILE_PARSING_STARTING, rawStatement);
        final String rawYaml = yaml.toString();
        final String rawSqlStatement = sql.toString();
        logger.trace(ApplicationEvents.FILE_YAML_FRONTMATTER_PARSED, rawYaml);
        logger.trace(ApplicationEvents.FILE_SQL_STATEMENT_PARSED, rawSqlStatement);

        final var parameterIndices = extractParameterIndices(rawSqlStatement);
        final var configuration = factory.createStatementConfiguration(source, rawYaml,
                parameterIndices, statementInFile);
        logger.debug(ApplicationEvents.FILE_PARSING_FINISHED, source, configuration.getName());
        return new SqlStatement(source, configuration, rawSqlStatement);
    }

    private static void splitUpYamlAndSql(
            final String rawStatement,
            final Consumer<String> yaml,
            final Consumer<String> sql) {
        new BufferedReader(new StringReader(rawStatement))
                .lines()
                .filter(line -> !line.trim().isEmpty() && !SQL_COMMENT_PREFIX.equals(line.trim()))
                .forEach(line -> {
                    if (line.startsWith(SQL_COMMENT_PREFIX)) {
                        yaml.accept(line.substring(2));
                        yaml.accept(NEWLINE);
                    } else {
                        sql.accept(line);
                        sql.accept(NEWLINE);
                    }
                });
    }

}
