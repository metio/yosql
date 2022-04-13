/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.benchmark.jdbc.slf4j;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmarks.common.Read;

/**
 * The JDBI implementation of the {@link Read} benchmarks using slf4j.
 */
public class JdbcSlf4jRead extends AbstractSlf4jBenchmark implements Read {

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
    public void readComplexRelationship() {
        projectRepository.findProjectsWithMinCosts(250);
    }

    @Override
    @Benchmark
    public void readMultipleEntitiesBasedOnCondition() {
        employeeRepository.findEmployeesWithMinSalary(500);
    }

}
