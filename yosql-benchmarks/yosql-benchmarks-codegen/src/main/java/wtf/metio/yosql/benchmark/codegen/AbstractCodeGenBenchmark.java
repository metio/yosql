/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.benchmark.codegen;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.codegen.orchestration.YoSQL;

/**
 * Performs a full code generation lifecycle, which contains file parsing, type generation, and file writing.
 */
abstract class AbstractCodeGenBenchmark extends AbstractBenchmark {

    /**
     * Shared YoSQL instance that is supposed to be created by subclasses.
     */
    protected YoSQL yosql;

    /**
     * Runs the full code generation lifecycle.
     */
    @Benchmark
    public final void benchmarkGenerateCode() {
        yosql.generateCode();
    }

}
