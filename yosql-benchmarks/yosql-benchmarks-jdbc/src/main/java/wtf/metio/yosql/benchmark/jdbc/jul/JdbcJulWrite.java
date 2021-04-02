/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.benchmark.jdbc.jul;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmarks.common.Write;

/**
 * The JDBI implementation of the {@link Write} benchmarks using java.util.logging.
 */
public class JdbcJulWrite extends AbstractJulBenchmark implements Write {

    @Override
    @Benchmark
    public void writeMultipleEntities() {
        projectRepository.insertProjectBatch(NAMES_BATCH, TIMESTAMP_BATCH);
    }

    @Override
    @Benchmark
    public void writeSingleEntity() {
        projectRepository.insertProject("hot fuzz", NOW);
    }

    @Override
    public void updateOneToManyRelation() {
        // TODO: implement benchmark
    }

    @Override
    public void updateSingleEntity() {
        employeeRepository.updateEmployee(1L, 1L, "bob", "builder", "bob@example.com", 200L);
    }

    @Override
    @Benchmark
    public void deleteSingleEntityByPrimaryKey() {
        employeeRepository.deleteEmployee(1L);
    }

}
