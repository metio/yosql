/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the result type of a converter was not configured
 */
public final class MissingConverterResultTypeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8054961753325570336L;

}
