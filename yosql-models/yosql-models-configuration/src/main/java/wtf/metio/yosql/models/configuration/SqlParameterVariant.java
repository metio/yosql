/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.models.configuration;

/**
 * Parameter variants that are mostly used by callable statements.
 */
public enum SqlParameterVariant {

    /**
     * The parameter is used as an input for a statement.
     */
    IN,

    /**
     * The parameter is used to capture an output of a statement.
     */
    OUT,

    /**
     * The parameter is both used as input and output of a statement.
     */
    INOUT,

}
