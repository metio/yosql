package de.xn__ho_hia.yosql.benchmark;

import java.io.IOException;

import org.openjdk.jmh.annotations.Setup;

/**
 * JMH based micro benchmark for YoSQL with a medium sample size.
 */
public class YoSqlWithMediumSampleBenchmark extends AbstractGenerateFilesBenchmark {

    /**
     * @throws IOException
     *             In case anything goes wrong while creating a temporary directory.
     */
    @Setup
    public void generateSqlFiles() throws IOException {
        generateSqlFiles(100);
    }

}
