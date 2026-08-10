/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.benchmark.dao.log4j;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmark.dao.common.Write;

import java.time.Instant;

/**
 * The JDBC implementation of the {@link Write} benchmarks using log4j.
 */
public class JdbcLog4jWrite extends AbstractLog4jBenchmark implements Write {

    @Override
    @Benchmark
    public void writeMultipleEntities() {
        projectRepository.insertProjectBatch(NAMES_BATCH, TIMESTAMP_BATCH);
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
