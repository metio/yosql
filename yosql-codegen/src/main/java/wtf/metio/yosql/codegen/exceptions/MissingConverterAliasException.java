/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the alias of a converter was not configured
 */
public final class MissingConverterAliasException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6770649153285787283L;

}
