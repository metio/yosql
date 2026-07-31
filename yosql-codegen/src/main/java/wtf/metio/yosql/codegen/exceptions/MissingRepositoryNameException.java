/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the name of a repository was not configured
 */
public final class MissingRepositoryNameException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 9036155498417209248L;

}
