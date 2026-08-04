/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the name of a parameter was not configured
 */
public final class MissingParameterNameException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5969485204226744242L;

    public MissingParameterNameException() {
        super("A statement reached code generation without a name for one of its parameters. Every statement is given one while "
                + "its file is read, so reaching this means YoSQL contradicted itself rather than the "
                + "SQL being wrong. Please report it, with the statement that triggered it.");
    }

}
