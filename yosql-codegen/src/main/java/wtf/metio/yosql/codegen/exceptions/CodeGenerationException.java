/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that something went wrong during code generation.
 */
public final class CodeGenerationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7360689175873441476L;

    /**
     * @param message The message to send.
     */
    public CodeGenerationException(final String message) {
        super(message);
    }

}
