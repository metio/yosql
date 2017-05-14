/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.parser;

import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.slf4j.cal10n.LocLogger;

import de.xn__ho_hia.yosql.model.ApplicationEvents;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.SqlConfiguration;
import de.xn__ho_hia.yosql.model.SqlStatement;

/**
 * Default .sql file parser.
 */
final class DefaultSqlFileParser implements SqlFileParser {

    private static final String           SQL_COMMENT_PREFIX = "--"; //$NON-NLS-1$
    private static final String           NEWLINE            = "\n"; //$NON-NLS-1$

    private final ExecutionErrors         errors;
    private final SqlConfigurationFactory factory;
    private final ExecutionConfiguration  config;
    private final Pattern                 statementSplitter;
    private final LocLogger               logger;

    DefaultSqlFileParser(
            final ExecutionErrors errors,
            final ExecutionConfiguration config,
            final SqlConfigurationFactory factory,
            final LocLogger logger) {
        this.errors = errors;
        this.config = config;
        this.factory = factory;
        this.logger = logger;
        statementSplitter = Pattern.compile(config.sqlStatementSeparator());
    }

    @Override
    public Stream<SqlStatement> parse(final Path source) {
        try {
            final Charset charset = Charset.forName(config.sqlFilesCharset());
            final String rawText = new String(Files.readAllBytes(source), charset);
            final AtomicInteger counter = new AtomicInteger(1);
            return statementSplitter.splitAsStream(rawText)
                    .parallel()
                    .filter(text -> text != null && !text.trim().isEmpty())
                    .map(statement -> convert(source, statement, counter.getAndIncrement()))
                    .filter(Objects::nonNull);
        } catch (final Throwable exception) {
            errors.add(exception);
            logger.debug(ApplicationEvents.FILE_PARSING_FAILED, source);
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

        logger.trace(ApplicationEvents.FILE_PARSING_STARTING, rawStatement);
        final String rawSqlStatement = sql.toString();
        final String rawYaml = yaml.toString();

        final Map<String, List<Integer>> parameterIndices = extractParameterIndices(rawSqlStatement);
        final SqlConfiguration configuration = factory.createStatementConfiguration(source, rawYaml,
                parameterIndices, statementInFile);
        logger.debug(ApplicationEvents.FILE_PARSING_FINISHED, source, configuration.getName());
        return new SqlStatement(configuration, rawSqlStatement);
    }

    private static void splitUpYamlAndSql(
            final String rawStatement,
            final Consumer<String> yaml,
            final Consumer<String> sql) {
        new BufferedReader(new StringReader(rawStatement))
                .lines()
                .filter(line -> !line.trim().isEmpty())
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
