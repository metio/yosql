/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.configuration;

/**
 * Enumeration of possible return modes.
 */
public enum ReturningMode {

    /**
     * Statement returns no data.
     */
    NONE,

    /**
     * Statement returns 0..1 results.
     */
    SINGLE,

    /**
     * Statement returns 0..n results.
     */
    MULTIPLE,

    /**
     * Statement lazy returns 0..n results.
     */
    CURSOR;

}
