/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.benchmark.dao.slf4j;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmark.dao.common.Call;

/**
 * The JDBC implementation of the {@link Call} benchmarks using slf4j.
 */
public class JdbcSlf4jCall extends AbstractSlf4jBenchmark implements Call {

    @Override
    @Benchmark
    public void callStoredProcedure() {
        maintenanceRepository.getVersion();
    }

}
