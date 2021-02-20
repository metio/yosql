/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark.full_lifecycle;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import wtf.metio.yosql.tooling.codegen.YoSQL;
import wtf.metio.yosql.benchmark.AbstractBenchmark;

@State(Scope.Benchmark)
abstract class AbstractFullLifecycleBenchmark extends AbstractBenchmark {

    protected YoSQL yosql;

    /**
     * Benchmarks code generation.
     */
    @Benchmark
    public final void benchmarkGenerateFiles() {
        yosql.generateCode();
    }

}
