/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the type name of a converter was not configured
 */
public final class MissingConverterTypeNameException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6567915883544326419L;

    public MissingConverterTypeNameException() {
        super("A statement reached code generation without the type name of the converter it reads rows with. Every statement is given one while "
                + "its file is read, so reaching this means YoSQL contradicted itself rather than the "
                + "SQL being wrong. Please report it, with the statement that triggered it.");
    }

}
