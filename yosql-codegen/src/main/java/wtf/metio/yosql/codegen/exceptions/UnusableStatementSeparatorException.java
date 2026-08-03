/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the configured statement separator is not a usable regular expression.
 *
 * <p>The separator is compiled as a pattern, which is worth knowing: a value like {@code (} or
 * {@code [} is a syntax error rather than a literal. Saying so beats a {@code PatternSyntaxException}
 * from inside the parser, which names neither the setting nor the value that broke it.</p>
 */
public final class UnusableStatementSeparatorException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnusableStatementSeparatorException(
            final String setting,
            final String separator,
            final Throwable cause) {
        super("'%s' is set to [%s], which is not a valid regular expression. The separator is read as one, so a literal '%s' has to be escaped."
                .formatted(setting, separator, separator), cause);
    }

}
