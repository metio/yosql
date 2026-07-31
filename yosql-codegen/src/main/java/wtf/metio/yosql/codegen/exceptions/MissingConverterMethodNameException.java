/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the method name of a converter was not configured
 */
public final class MissingConverterMethodNameException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2276862104152455950L;

}
