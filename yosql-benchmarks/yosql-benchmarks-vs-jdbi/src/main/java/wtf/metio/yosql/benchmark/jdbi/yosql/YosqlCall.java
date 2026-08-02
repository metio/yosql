/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.benchmark.jdbi.yosql;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmark.dao.common.Call;

/**
 * Calling a stored procedure through a repository `YoSQL` generated.
 */
public class YosqlCall extends AbstractYosqlShootout implements Call {

    @Override
    @Benchmark
    public void callStoredProcedure() {
        maintenance.getVersion();
    }

}
