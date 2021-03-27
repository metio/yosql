/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark.codegen;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import wtf.metio.yosql.codegen.api.YoSQL;

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
