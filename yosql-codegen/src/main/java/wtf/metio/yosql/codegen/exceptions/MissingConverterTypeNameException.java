/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the type name of a converter was not configured
 */
public final class MissingConverterTypeNameException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6567915883544326419L;

}
