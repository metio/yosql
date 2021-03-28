/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.benchmark.jdbc.noop;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmarks.common.Write;

import java.time.Instant;

/**
 * The JDBI implementation of the {@link Write} benchmarks using no logging statements.
 */
public class JdbcWrite extends AbstractNoOpBenchmark implements Write {

    @Override
    @Benchmark
    public void writeMultipleEntities() {
        final var now = Instant.now().toEpochMilli();
        projectRepository.insertProjectBatch(new String[]{"first", "second"}, new Long[]{now, now});
    }

    @Override
    @Benchmark
    public void writeSingleEntity() {
        projectRepository.insertProject("hot fuzz", Instant.now().toEpochMilli());
    }

    @Override
    public void updateOneToManyRelation() {
        // TODO: implement benchmark
    }

    @Override
    public void updateManyToOneRelation() {
        // TODO: implement benchmark
    }

    @Override
    @Benchmark
    public void deleteSingleEntityByPrimaryKey() {
        employeeRepository.deleteEmployee(1L);
    }

}
