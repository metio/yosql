package de.xn__ho_hia.yosql.benchmark;

import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

/**
 * Encapsulates common benchmark functionality for single use cases.
 */
@State(Scope.Benchmark)
public abstract class AbstractForEachUseCaseBenchmark extends AbstractBenchmark {

    /**
     * The supported use cases for file parsing.
     */
    @Param({ "callFunction.sql", "insertData.sql", "readData.sql", "updateData.sql" })
    public String usecase;

    /**
     * Prepares a single repository for each supported use case.
     */
    @Setup
    public void generateSqlFiles() {
        prepareRepositories(1, usecase);
    }

}
