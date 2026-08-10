/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.benchmark.dao.system;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmark.dao.AbstractBenchmark;
import wtf.metio.yosql.benchmark.dao.common.Write;

import java.time.Instant;

/**
 * The JDBC implementation of the {@link Write} benchmarks using System.Logger.
 */
public class JdbcSystemWrite extends AbstractSystemBenchmark implements Write {

    @Override
    @Benchmark
    public void writeMultipleEntities() {
        projectRepository.insertProjectBatch(AbstractBenchmark.NAMES_BATCH, AbstractBenchmark.TIMESTAMP_BATCH);
    }

    @Override
    @Benchmark
    public void writeSingleEntity() {
        projectRepository.insertProject("hot fuzz", Instant.now().toEpochMilli());
    }

    @Override
    public void updateOneToManyRelation() {
        departmentRepository.updateDepartmentsOfCompany(2L, "benchmarks", 1L);
    }

    @Override
    public void updateSingleEntity() {
        employeeRepository.updateEmployee(1L, "bob", "builder", "bob@example.com", 200L, 1L);
    }

    @Override
    @Benchmark
    public void deleteSingleEntityByPrimaryKey() {
        employeeRepository.deleteEmployee(1L);
    }

}
