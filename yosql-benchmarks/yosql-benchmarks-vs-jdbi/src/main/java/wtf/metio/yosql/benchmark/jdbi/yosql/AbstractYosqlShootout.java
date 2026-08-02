/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.benchmark.jdbi.yosql;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import wtf.metio.yosql.benchmark.dao.noop.persistence.CompanyRepository;
import wtf.metio.yosql.benchmark.dao.noop.persistence.DepartmentRepository;
import wtf.metio.yosql.benchmark.dao.noop.persistence.EmployeeRepository;
import wtf.metio.yosql.benchmark.dao.noop.persistence.MaintenanceRepository;
import wtf.metio.yosql.benchmark.dao.noop.persistence.ProjectRepository;
import wtf.metio.yosql.benchmark.jdbi.AbstractShootout;

/**
 * The `YoSQL` half: repositories generated from the DAO benchmark's `.sql` files, with logging off
 * so that what is measured is reading a row rather than writing a log line.
 */
abstract class AbstractYosqlShootout extends AbstractShootout {

    protected CompanyRepository companies;
    protected DepartmentRepository departments;
    protected EmployeeRepository employees;
    protected ProjectRepository projects;
    protected MaintenanceRepository maintenance;

    @Override
    protected String databaseName() {
        return "shootout-yosql";
    }

    @Setup(Level.Trial)
    public void seed() {
        companies = new CompanyRepository(dataSource);
        departments = new DepartmentRepository(dataSource);
        employees = new EmployeeRepository(dataSource);
        projects = new ProjectRepository(dataSource);
        maintenance = new MaintenanceRepository(dataSource);

        companies.insertCompany("metio.wtf", "YoSQL");
        companies.insertCompany("other", "tests");
        departments.insertDepartment(1L, "benchmarks");
        projects.insertProject("YoSQL", NOW);
        employees.insertEmployee(1L, "bob", "example", "bob@example.com", 100L);
    }

}
