/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.benchmark.jdbi.yosql;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmark.dao.common.Read;

/**
 * Reading through repositories `YoSQL` generated.
 */
public class YosqlRead extends AbstractYosqlShootout implements Read {

    @Override
    @Benchmark
    public void readSingleEntityByPrimaryKey() {
        companies.findCompany(1L);
    }

    @Override
    @Benchmark
    public void readOneToManyRelation() {
        departments.findDepartmentsByCompany(1L);
    }

    @Override
    @Benchmark
    public void readManyToOneRelation() {
        companies.findCompanyByDepartment(1L);
    }

    @Override
    @Benchmark
    public void readMultipleEntities() {
        companies.findCompanies();
    }

    @Override
    @Benchmark
    public void readMultipleEntitiesBasedOnCondition() {
        employees.findEmployeesWithMinSalary(500);
    }

}
