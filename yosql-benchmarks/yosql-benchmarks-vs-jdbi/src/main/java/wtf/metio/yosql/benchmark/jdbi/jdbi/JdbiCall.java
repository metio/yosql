/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.benchmark.jdbi.jdbi;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmark.dao.common.Call;

/**
 * Calling a stored procedure through JDBI.
 */
public class JdbiCall extends AbstractJdbiShootout implements Call {

    @Override
    @Benchmark
    public void callStoredProcedure() {
        dao.getVersion();
    }

}
