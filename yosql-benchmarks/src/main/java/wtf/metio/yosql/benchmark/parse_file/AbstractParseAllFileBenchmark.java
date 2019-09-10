/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark.parse_file;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import wtf.metio.yosql.benchmark.AbstractForAllUseCasesBenchmark;
import wtf.metio.yosql.files.SqlFileParser;

/**
 * Abstract benchmark for file parsing running against each .sql file individually.
 */
@State(Scope.Benchmark)
public abstract class AbstractParseAllFileBenchmark extends AbstractForAllUseCasesBenchmark {

    protected SqlFileParser parser;

    /**
     * Prepares a single repository for each supported use case.
     *
     * @throws Exception
     *             In case anything goes wrong during setup.
     */
    @Setup
    public abstract void setUpParser() throws Exception;

    /**
     * Benchmarks file parsing.
     */
    @Benchmark
    public final void benchmarkParseFiles() {
        SUPPORTED_USE_CASES
                .forEach(usecase -> parser.parse(inputDirectory.resolve(repositoryName(1)).resolve(usecase)));
    }

}
