/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.models.configuration;

/**
 * Enumeration of possible SQL statement types.
 */
public enum SqlStatementType {

    /**
     * Statement reads data from a database.
     */
    READING,

    /**
     * Statement writes data to a database.
     */
    WRITING,

    /**
     * Statement calls a (stored) procedure in a database.
     */
    CALLING,

}
