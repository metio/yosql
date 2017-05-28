/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.benchmark.full_lifecycle;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import de.xn__ho_hia.yosql.YoSql;
import de.xn__ho_hia.yosql.benchmark.AbstractBenchmark;

@State(Scope.Benchmark)
abstract class AbstractFullLifecycleBenchmark extends AbstractBenchmark {

    protected YoSql yosql;

    /**
     * Benchmarks code generation.
     *
     * @throws Exception
     *             In case anything goes wrong while generating files.
     */
    @Benchmark
    public final void benchmarkGenerateFiles() throws Exception {
        yosql.generateCode();
    }

}
