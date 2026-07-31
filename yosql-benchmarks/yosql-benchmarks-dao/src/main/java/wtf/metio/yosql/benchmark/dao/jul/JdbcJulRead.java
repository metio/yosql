/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.benchmark.dao.jul;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmark.dao.common.Read;

/**
 * The JDBC implementation of the {@link Read} benchmarks using java.util.logging.
 */
public class JdbcJulRead extends AbstractJulBenchmark implements Read {

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
