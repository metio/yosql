/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the vendor of a {@link wtf.metio.yosql.models.immutables.SqlConfiguration} was not configured
 */
public final class MissingSqlConfigurationVendorException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6387844039015540263L;

}
