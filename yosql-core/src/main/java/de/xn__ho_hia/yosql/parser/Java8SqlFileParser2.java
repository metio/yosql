/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.SqlConfiguration;
import de.xn__ho_hia.yosql.model.SqlStatement;

/**
 * Parses SQL statements inside .sql files.
 */
public final class Java8SqlFileParser2 implements SqlFileParser {

    private static final String   NEWLINE = "\n"; //$NON-NLS-1$

    final ExecutionErrors         errors;
    final SqlConfigurationFactory factory;
    final ExecutionConfiguration  config;
    final PrintStream             out;

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
    @SuppressWarnings("resource")
    public Stream<SqlStatement> parse(final Path source) {
        try {
            final Charset charset = Charset.forName(config.sqlFilesCharset());
            final BufferedReader reader = Files.newBufferedReader(source, charset);
            return StreamSupport.stream(new SqlStatementSpliterator(reader, source),
                    false).onClose(() -> {
                        try {
                            reader.close();
                        } catch (final IOException exception) {
                            errors.add(exception);
                        }
                    });
        } catch (final Throwable exception) {
            errors.add(exception);
            return Stream.empty();
        }
    }

    private final class SqlStatementSpliterator implements Spliterator<SqlStatement> {

        private final AtomicInteger  counter = new AtomicInteger(0);
        private final BufferedReader reader;
        private final Path           source;
        private String               leftover;

        public SqlStatementSpliterator(final BufferedReader reader, final Path source) {
            this.reader = reader;
            this.source = source;
        }

        @Override
        public boolean tryAdvance(final Consumer<? super SqlStatement> action) {
            try {
                final StringBuilder yaml = new StringBuilder();
                final StringBuilder sql = new StringBuilder();
                if (leftover != null) {
                    splitApart(leftover, yaml, sql);
                    leftover = null;
                }
                String input;
                while ((input = reader.readLine()) != null) {
                    if (input.contains(config.sqlStatementSeparator())) {
                        final String[] split = input.split(config.sqlStatementSeparator());
                        if (split.length > 0) {
                            input = split[0];
                            if (split.length > 1) {
                                leftover = split[1];
                            }
                        }
                    }
                    splitApart(input, yaml, sql);
                    if (leftover != null) {
                        break;
                    }
                }
                final String rawSqlStatement = sql.toString();
                final String rawYaml = yaml.toString();
                if (!rawSqlStatement.isEmpty()) {
                    final Map<String, List<Integer>> parameterIndices = extractParameterIndices(rawSqlStatement);
                    SqlConfiguration configuration = null;
                    try {
                        configuration = factory.createStatementConfiguration(source, rawYaml,
                                parameterIndices, counter.getAndIncrement());
                    } catch (final Throwable exception) {
                        errors.add(exception);
                        return false;
                    }
                    if (out != null && configuration != null) {
                        out.printf("Parsed [%s#%s]", source, configuration.getName()); //$NON-NLS-1$
                        out.println();
                    }
                    action.accept(new SqlStatement(configuration, rawSqlStatement));
                    return true;
                }
                return false;
            } catch (final IOException exception) {
                errors.add(exception);
                return false;
            }
        }

        private void splitApart(final String string, final StringBuilder yaml, final StringBuilder sql) {
            if (string.startsWith("--")) { //$NON-NLS-1$
                yaml.append(string.substring(2));
                yaml.append(NEWLINE);
            } else {
                sql.append(string);
                sql.append(NEWLINE);
            }
        }

        @Override
        public Spliterator<SqlStatement> trySplit() {
            return null;
        }

        @Override
        public long estimateSize() {
            return Long.MAX_VALUE;
        }

        @Override
        public int characteristics() {
            return ORDERED | DISTINCT | NONNULL | IMMUTABLE;
        }

    }

}
