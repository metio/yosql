/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.benchmark.jdbc.log4j;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmarks.common.Call;

/**
 * The JDBI implementation of the {@link Call} benchmarks using log4j.
 */
public class JdbcLog4jCall extends AbstractLog4jBenchmark implements Call {

    @Override
    @Benchmark
    public void callStoredProcedure() {
        maintenanceRepository.getVersion();
    }

}
