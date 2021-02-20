/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

/**
 * Encapsulates common benchmark functionality for all use cases.
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
