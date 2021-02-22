/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark.codegen;

import org.openjdk.jmh.annotations.Setup;

import java.io.IOException;

/**
 * JMH based micro benchmark for YoSQL with a large sample size.
 */
abstract class AbstractLargeSampleBenchmark extends AbstractDaggerBenchmark {

    /**
     * @throws IOException In case anything goes wrong while creating .sql files.
     */
    @Setup
    public final void generateSqlFiles() throws IOException {
        prepareRepositoriesForAllUseCases(500);
    }

}
