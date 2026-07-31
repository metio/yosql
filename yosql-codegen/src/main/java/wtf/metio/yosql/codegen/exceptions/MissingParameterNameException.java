/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the name of a parameter was not configured
 */
public final class MissingParameterNameException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5969485204226744242L;

}
