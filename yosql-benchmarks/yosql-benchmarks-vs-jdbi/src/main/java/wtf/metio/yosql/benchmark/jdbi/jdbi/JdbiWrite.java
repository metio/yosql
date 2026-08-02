/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.benchmark.jdbi.jdbi;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmark.dao.common.Write;

import java.time.Instant;

/**
 * Writing through JDBI.
 */
public class JdbiWrite extends AbstractJdbiShootout implements Write {

    @Override
    @Benchmark
    public void writeMultipleEntities() {
        dao.insertProjectBatch(NAMES_BATCH, TIMESTAMP_BATCH);
    }

    @Override
    @Benchmark
    public void writeSingleEntity() {
        dao.insertProject("hot fuzz", Instant.now().toEpochMilli());
    }

    @Override
    @Benchmark
    public void updateOneToManyRelation() {
        dao.updateDepartmentsOfCompany(2L, 1L, "benchmarks");
    }

    @Override
    @Benchmark
    public void updateSingleEntity() {
        dao.updateEmployee(1L, 1L, "bob", "builder", "bob@example.com", 200L);
    }

    @Override
    @Benchmark
    public void deleteSingleEntityByPrimaryKey() {
        dao.deleteEmployee(1L);
    }

}
