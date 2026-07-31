/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.benchmark.dao.noop;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmark.dao.common.Read;

/**
 * The JDBC implementation of the {@link Read} benchmarks using no logging statements.
 */
public class JdbcRead extends AbstractNoOpBenchmark implements Read {

    @Override
    @Benchmark
    public void readSingleEntityByPrimaryKey() {
        companyRepository.findCompany(1L);
    }

    @Override
    @Benchmark
    public void readOneToManyRelation() {
        departmentRepository.findDepartmentsByCompany(1L);
    }

    @Override
    @Benchmark
    public void readManyToOneRelation() {
        companyRepository.findCompanyByDepartment(1L);
    }

    @Override
    @Benchmark
    public void readMultipleEntities() {
        companyRepository.findCompanies();
    }

    @Override
    @Benchmark
    public void readMultipleEntitiesBasedOnCondition() {
        employeeRepository.findEmployeesWithMinSalary(500);
    }

}
