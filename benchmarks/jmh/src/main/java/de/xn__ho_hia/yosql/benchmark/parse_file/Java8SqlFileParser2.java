/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.benchmark.parse_file;

import java.io.BufferedReader;
import java.io.PrintStream;
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

import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.SqlConfiguration;
import de.xn__ho_hia.yosql.model.SqlStatement;
import de.xn__ho_hia.yosql.parser.Java8SqlFileParser;
import de.xn__ho_hia.yosql.parser.SqlConfigurationFactory;
import de.xn__ho_hia.yosql.parser.SqlFileParser;

/**
 * Alternative implementation for the {@link Java8SqlFileParser}. Used for benchmarking.
 */
public final class Java8SqlFileParser2 implements SqlFileParser {

    private static final String           NEWLINE = "\n"; //$NON-NLS-1$

    private final ExecutionErrors         errors;
    private final SqlConfigurationFactory factory;
    private final ExecutionConfiguration  config;
    private final PrintStream             out;

    /**
     * @param errors
     *            The error collection to use.
     * @param config
     *            The configuration to use.
     * @param factory
     *            The SQL configuration factory to use.
     * @param out
     *            The output stream to use.
     */
    @Inject
    public Java8SqlFileParser2(
            final ExecutionErrors errors,
            final ExecutionConfiguration config,
            final SqlConfigurationFactory factory,
            final PrintStream out) {
        this.errors = errors;
        this.config = config;
        this.factory = factory;
        this.out = out;
    }

    /**
     * @param source
     *            The source file to parse.
     * @return All parsed statements inside that source file.
     */
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

        if (out != null && configuration != null) {
            out.printf("Parsed [%s#%s]", source, configuration.getName()); //$NON-NLS-1$
            out.println();
        }
        return new SqlStatement(configuration, rawSqlStatement);
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
