/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.files;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.exceptions.UnusableStatementSeparatorException;
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
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * Default SQL statement parser.
 */
public final class DefaultSqlStatementParser implements SqlStatementParser {

    private static final String SQL_COMMENT_PREFIX = "--";
    private static final String BLOCK_COMMENT_START = "/*";
    private static final String BLOCK_COMMENT_END = "*/";
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
        defaultStatementSplitter = splitter(files.sqlStatementSeparator(), "files.sqlStatementSeparator");
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
            final var skippedText = withoutHeader(rawText);
            final var splitter = getStatementSplitter(rawText);
            // Sequential, and deliberately so. The index counts a statement's position in its file,
            // and a method's name is built from it when several share a name — so which statement
            // gets which number decides what the generated method is called. In parallel the
            // scheduler decides that, and the same file produces `getX2` for one statement body on
            // one run and for another on the next. The work per file is a string split; the walk
            // over files is already parallel, and that is where the parallelism belongs.
            final var counter = new AtomicInteger(1);
            return splitStatements(splitter, skippedText)
                    .map(String::strip)
                    .filter(not(String::isBlank))
                    .filter(line -> !line.startsWith(YOSQL_PRAGMA))
                    .map(statement -> parseStatement(source, statement, counter.getAndIncrement()))
                    .toList()
                    .stream();
        } catch (final IOException exception) {
            errors.add(exception);
            logger.debug(ApplicationErrors.FILE_PARSING_FAILED, source);
            return Stream.empty();
        }
    }

    /**
     * Cuts a file into statements at every separator that is really a separator.
     *
     * <p>A {@code ;} inside a string literal or a comment is a character of that literal, not the end
     * of a statement: {@code select split_part(x, ';', 1) from t} is one statement, and splitting the
     * text as written turns it into two halves that are each invalid SQL. The separator is therefore
     * matched against {@link SqlText#maskLiteralsAndComments(String)}, which blanks those stretches
     * while keeping every offset, and the cuts are taken from the statement the author wrote.</p>
     */
    // visible for testing
    static Stream<String> splitStatements(final Pattern splitter, final String text) {
        final var matcher = splitter.matcher(SqlText.maskLiteralsAndComments(text));
        final var statements = new ArrayList<String>();
        var start = 0;
        while (matcher.find()) {
            statements.add(text.substring(start, matcher.start()));
            start = matcher.end();
        }
        statements.add(text.substring(start));
        return statements.stream();
    }

    // visible for testing
    Pattern getStatementSplitter(final String text) {
        final var customStatementSeparator = CUSTOM_SQL_STATEMENT_SEPARATOR.matcher(text);
        return customStatementSeparator.find()
                ? splitter(customStatementSeparator.group(1), "the @yosql sqlStatementSeparator pragma")
                : defaultStatementSplitter;
    }

    /**
     * Drops the configured number of leading lines — a licence header, usually.
     *
     * <p>Walks to the cut point and takes one substring. Deleting from the front of a builder per
     * line shifts everything after it each time, which is quadratic in the file for no reason.</p>
     */
    /**
     * The separator is a regular expression, and a mistyped one otherwise arrives as a bare
     * {@link java.util.regex.PatternSyntaxException} from somewhere in the parser, naming neither
     * the setting nor the value.
     */
    private static Pattern splitter(final String separator, final String setting) {
        try {
            return Pattern.compile(separator);
        } catch (final PatternSyntaxException cause) {
            throw new UnusableStatementSeparatorException(setting, separator, cause);
        }
    }

    /**
     * Drops the licence header a file opens with.
     *
     * <p>Only a block comment, and only where the file starts. A {@code --} header cannot be told
     * apart from front matter — a licence line is as good a YAML mapping as {@code name: findTenant}
     * — so reading one would quietly turn a licence into configuration. A block comment further in
     * is left where it is, because that is where a vendor puts an optimizer hint.</p>
     */
    private static String withoutHeader(final String rawText) {
        var text = rawText;
        while (true) {
            final var start = text.stripLeading();
            if (!start.startsWith(BLOCK_COMMENT_START)) {
                return text;
            }
            final var end = start.indexOf(BLOCK_COMMENT_END);
            if (end < 0) {
                return text;
            }
            text = start.substring(end + BLOCK_COMMENT_END.length());
        }
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
        final var configuration = factory.createConfiguration(source, rawYaml, rawSqlStatement,
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
