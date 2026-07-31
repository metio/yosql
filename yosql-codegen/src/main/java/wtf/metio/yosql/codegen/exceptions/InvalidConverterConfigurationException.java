/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the converter configuration is invalid.
 */
public final class InvalidConverterConfigurationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8398365245482924503L;

    /**
     * @param message The message to send.
     */
    public InvalidConverterConfigurationException(final String message) {
        super(message);
    }

}
