/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.benchmark.dao.jul;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmark.dao.common.Call;

/**
 * The JDBC implementation of the {@link Call} benchmarks using java.util.logging.
 */
public class JdbcJulCall extends AbstractJulBenchmark implements Call {

    @Override
    @Benchmark
    public void callStoredProcedure() {
        maintenanceRepository.getVersion();
    }

}
