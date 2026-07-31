/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the type name of a parameter was not configured
 */
public final class MissingParameterTypeNameException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8221959215404531043L;

}
