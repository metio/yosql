package de.xn__ho_hia.yosql.benchmark.full_lifecycle;

import org.openjdk.jmh.annotations.Setup;

import de.xn__ho_hia.yosql.benchmark.BenchmarkConfigurationModule;
import de.xn__ho_hia.yosql.benchmark.DaggerYoSqlBenchmarkComponent;

abstract class AbstractYoSqlFullLifecycleBenchmark extends AbstractFullLifecycleBenchmark {

    @Setup
    public final void setUpYoSql() {
        yosql = DaggerYoSqlBenchmarkComponent.builder()
                .benchmarkConfigurationModule(new BenchmarkConfigurationModule(inputDirectory, outputDirectory))
                .build()
                .yosql();
    }

}
