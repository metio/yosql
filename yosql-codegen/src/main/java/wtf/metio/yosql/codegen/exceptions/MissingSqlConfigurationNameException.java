/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the name of a {@link wtf.metio.yosql.models.immutables.SqlConfiguration} was not configured
 */
public final class MissingSqlConfigurationNameException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6258250741167136706L;

    public MissingSqlConfigurationNameException() {
        super("A statement reached code generation without a name. Every statement is given one while "
                + "its file is read, so reaching this means YoSQL contradicted itself rather than the "
                + "SQL being wrong. Please report it, with the statement that triggered it.");
    }

}
