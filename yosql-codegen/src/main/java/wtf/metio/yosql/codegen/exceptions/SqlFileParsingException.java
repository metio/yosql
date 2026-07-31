/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that something went wrong during SQL file parsing.
 */
public final class SqlFileParsingException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4967281564674584404L;

    /**
     * @param message The message to send.
     */
    public SqlFileParsingException(final String message) {
        super(message);
    }

}
