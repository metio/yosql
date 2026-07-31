/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.benchmark.dao.common;

/**
 * Describes all benchmark scenarios that call procedures inside your database.
 */
public interface Call {

    /**
     * Calls a stored procedure.
     */
    void callStoredProcedure();

}
