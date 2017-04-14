package de.xn__ho_hia.yosql.benchmark;

import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

/**
 * Encapsulates common benchmark functionality for each use case invidually.
 */
@State(Scope.Benchmark)
public abstract class AbstractForEachUseCaseBenchmark extends AbstractBenchmark {

    /**
     * The supported use cases for file parsing.
     */
    @Param({
        //@formatter:off
        "callFunction.sql"
        ,"callFunctionMultiple.sql"
        ,"insertData.sql"
        ,"insertDataMultiple.sql"
        ,"readData.sql"
        ,"readDataMultiple.sql"
        ,"updateData.sql"
        ,"updateDataMultiple.sql"
        //@formatter:on
    })
    public String usecase;

    /**
     * Prepares a single repository for a single supported use case.
     */
    @Setup
    public void prepareRepositoryForUseCase() {
        prepareRepositories(1, usecase);
    }

}
