/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.benchmark.dao.log4j;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmark.dao.common.Call;

/**
 * The JDBC implementation of the {@link Call} benchmarks using log4j.
 */
public class JdbcLog4jCall extends AbstractLog4jBenchmark implements Call {

    @Override
    @Benchmark
    public void callStoredProcedure() {
        maintenanceRepository.getVersion();
    }

}
