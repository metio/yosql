/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
