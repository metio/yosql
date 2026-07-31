/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.benchmark.codegen;

import org.openjdk.jmh.annotations.Setup;

/**
 * JMH based micro benchmark for YoSQL with a medium sample size.
 */
abstract class AbstractMediumSampleBenchmark extends AbstractDaggerBenchmark {

    /**
     * Generates SQL fies for a medium number of repositories.
     */
    @Setup
    public final void generateSqlFiles() {
        prepareRepositoriesForAllUseCases(25);
    }

}
