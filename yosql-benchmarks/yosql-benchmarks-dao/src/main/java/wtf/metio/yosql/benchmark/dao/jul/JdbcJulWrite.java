/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.benchmark.dao.jul;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmark.dao.common.Write;

/**
 * The JDBC implementation of the {@link Write} benchmarks using java.util.logging.
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
        departmentRepository.updateDepartmentsOfCompany(2L, 1L, "benchmarks");
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
