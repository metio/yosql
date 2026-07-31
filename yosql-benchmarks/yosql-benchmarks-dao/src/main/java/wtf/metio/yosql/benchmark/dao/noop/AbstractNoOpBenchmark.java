/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.benchmark.dao.noop;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import wtf.metio.yosql.benchmark.dao.AbstractBenchmark;
import wtf.metio.yosql.benchmark.dao.noop.persistence.*;

/**
 * Abstract benchmark class for all benchmarks that use no logging statements.
 */
abstract class AbstractNoOpBenchmark extends AbstractBenchmark {

    protected CompanyRepository companyRepository;
    protected DepartmentRepository departmentRepository;
    protected EmployeeRepository employeeRepository;
    protected ProjectRepository projectRepository;
    protected MaintenanceRepository maintenanceRepository;

    @Setup(Level.Trial)
    public void createRepositories() {
        companyRepository = new CompanyRepository(dataSource);
        departmentRepository = new DepartmentRepository(dataSource);
        employeeRepository = new EmployeeRepository(dataSource);
        projectRepository = new ProjectRepository(dataSource);
        maintenanceRepository = new MaintenanceRepository(dataSource);
        companyRepository.insertCompany("metio.wtf", "NoOp");
        companyRepository.insertCompany("other", "tests");
        departmentRepository.insertDepartment(1L, "benchmarks");
        projectRepository.insertProject("NoOp", NOW);
        employeeRepository.insertEmployee(1L, "bob", "example", "bob@example.com", 100L);
    }

    @TearDown(Level.Trial)
    public void destroyRepositories() {
        companyRepository = null;
        departmentRepository = null;
        employeeRepository = null;
        projectRepository = null;
    }

}
