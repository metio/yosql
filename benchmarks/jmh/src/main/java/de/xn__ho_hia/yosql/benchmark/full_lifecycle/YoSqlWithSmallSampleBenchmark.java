package de.xn__ho_hia.yosql.benchmark.full_lifecycle;

import java.io.IOException;

import org.openjdk.jmh.annotations.Setup;

/**
 * JMH based micro benchmark for YoSQL with a small sample size.
 */
public class YoSqlWithSmallSampleBenchmark extends AbstractYoSqlFullLifecycleBenchmark {

    /**
     * @throws IOException
     *             In case anything goes wrong while creating .sql files.
     */
    @Setup
    public void generateSqlFiles() throws IOException {
        prepareRepositoriesForAllUseCases(3);
    }

}
