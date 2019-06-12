/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark.full_lifecycle;

import java.io.IOException;

import org.openjdk.jmh.annotations.Setup;

/**
 * JMH based micro benchmark for YoSQL with a big sample size.
 */
public class YoSqlWithBigSampleBenchmark extends AbstractYoSqlFullLifecycleBenchmark {

    /**
     * @throws IOException
     *             In case anything goes wrong while creating .sql files.
     */
    @Setup
    public void generateSqlFiles() throws IOException {
        prepareRepositoriesForAllUseCases(100);
    }

}
