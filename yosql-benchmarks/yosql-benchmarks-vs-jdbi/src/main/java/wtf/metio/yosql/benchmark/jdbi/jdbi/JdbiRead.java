/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.benchmark.jdbi.jdbi;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmark.dao.common.Read;

/**
 * Reading through JDBI.
 */
public class JdbiRead extends AbstractJdbiShootout implements Read {

    @Override
    @Benchmark
    public void readSingleEntityByPrimaryKey() {
        dao.findCompany(1L);
    }

    @Override
    @Benchmark
    public void readOneToManyRelation() {
        dao.findDepartmentsByCompany(1L);
    }

    @Override
    @Benchmark
    public void readManyToOneRelation() {
        dao.findCompanyByDepartment(1L);
    }

    @Override
    @Benchmark
    public void readMultipleEntities() {
        dao.findCompanies();
    }

    @Override
    @Benchmark
    public void readMultipleEntitiesBasedOnCondition() {
        dao.findEmployeesWithMinSalary(500);
    }

}
