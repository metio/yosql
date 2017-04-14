package de.xn__ho_hia.yosql.benchmark.parse_file;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import de.xn__ho_hia.yosql.benchmark.AbstractForEachUseCaseBenchmark;
import de.xn__ho_hia.yosql.parser.SqlFileParser;

/**
 * Abstract benchmark for file parsing running against each .sql file individually.
 */
@State(Scope.Benchmark)
public abstract class AbstractParseEachFileBenchmark extends AbstractForEachUseCaseBenchmark {

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
    public final void benchmarkParseFile() {
        parser.parse(inputDirectory.resolve(repositoryName(1)).resolve(usecase));
    }

}
