/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the name of a repository was not configured
 */
public final class MissingRepositoryNameException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 9036155498417209248L;

    public MissingRepositoryNameException() {
        super("A statement reached code generation without a repository name. Every statement is given one while "
                + "its file is read, so reaching this means YoSQL contradicted itself rather than the "
                + "SQL being wrong. Please report it, with the statement that triggered it.");
    }

}
