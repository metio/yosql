/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.benchmark.jdbi.yosql;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmark.dao.common.Write;

import java.time.Instant;

/**
 * Writing through repositories `YoSQL` generated.
 */
public class YosqlWrite extends AbstractYosqlShootout implements Write {

    @Override
    @Benchmark
    public void writeMultipleEntities() {
        projects.insertProjectBatch(NAMES_BATCH, TIMESTAMP_BATCH);
    }

    @Override
    @Benchmark
    public void writeSingleEntity() {
        projects.insertProject("hot fuzz", Instant.now().toEpochMilli());
    }

    @Override
    @Benchmark
    public void updateOneToManyRelation() {
        departments.updateDepartmentsOfCompany(2L, "benchmarks", 1L);
    }

    @Override
    @Benchmark
    public void updateSingleEntity() {
        employees.updateEmployee(1L, "bob", "builder", "bob@example.com", 200L, 1L);
    }

    @Override
    @Benchmark
    public void deleteSingleEntityByPrimaryKey() {
        employees.deleteEmployee(1L);
    }

}
