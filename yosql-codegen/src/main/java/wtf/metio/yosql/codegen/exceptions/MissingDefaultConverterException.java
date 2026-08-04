/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that no default converter was configured
 */
public final class MissingDefaultConverterException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1818036215670758893L;

    public MissingDefaultConverterException() {
        super("A statement reached code generation without a default converter. Every statement is given one while "
                + "its file is read, so reaching this means YoSQL contradicted itself rather than the "
                + "SQL being wrong. Please report it, with the statement that triggered it.");
    }

}
