package de.xn__ho_hia.yosql.benchmark.full_lifecycle;

import java.io.IOException;

import org.openjdk.jmh.annotations.Setup;

/**
 * JMH based micro benchmark for YoSQL with a medium sample size.
 */
public class YoSql2WithMediumSampleBenchmark extends AbstractYoSql2FullLifecycleBenchmark {

    /**
     * @throws IOException
     *             In case anything goes wrong while creating .sql files.
     */
    @Setup
    public void generateSqlFiles() throws IOException {
        prepareRepositoriesForAllUseCases(50);
    }

}
