/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
