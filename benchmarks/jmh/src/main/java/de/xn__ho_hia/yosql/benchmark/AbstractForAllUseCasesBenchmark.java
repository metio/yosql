package de.xn__ho_hia.yosql.benchmark;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

/**
 * Encapsulates common benchmark functionality for single use cases.
 */
@State(Scope.Benchmark)
public abstract class AbstractForAllUseCasesBenchmark extends AbstractBenchmark {

    /**
     * Prepares a single repository for each supported use case.
     */
    @Setup
    public void generateSqlFiles() {
        prepareRepositoriesForAllUseCases(1);
    }

}
