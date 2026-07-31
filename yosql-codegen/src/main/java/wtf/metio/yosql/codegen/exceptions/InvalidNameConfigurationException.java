/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the name configuration is invalid.
 */
public final class InvalidNameConfigurationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8418727559429742655L;

    /**
     * @param message The message to send.
     */
    public InvalidNameConfigurationException(final String message) {
        super(message);
    }

}
