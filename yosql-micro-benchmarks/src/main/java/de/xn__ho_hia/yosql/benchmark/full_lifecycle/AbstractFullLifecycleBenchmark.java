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
        yosql.generateFiles();
    }

}
