/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that no default converter was configured
 */
public final class MissingDefaultConverterException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1818036215670758893L;

}
