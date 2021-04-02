/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.benchmark.jdbc.tinylog;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import wtf.metio.yosql.benchmark.jdbc.AbstractBenchmark;
import wtf.metio.yosql.benchmark.jdbc.slf4j.persistence.CompanyRepository;
import wtf.metio.yosql.benchmark.jdbc.slf4j.persistence.DepartmentRepository;
import wtf.metio.yosql.benchmark.jdbc.slf4j.persistence.EmployeeRepository;
import wtf.metio.yosql.benchmark.jdbc.slf4j.persistence.ProjectRepository;
import wtf.metio.yosql.benchmark.jdbc.tinylog.persistence.MaintenanceRepository;

/**
 * Abstract benchmark class for all benchmarks that use the tinylog API.
 */
abstract class AbstractTinylogBenchmark extends AbstractBenchmark {

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
        companyRepository.insertCompany("metio.wtf", "tinylog");
        companyRepository.insertCompany("other", "tests");
        departmentRepository.insertDepartment(1L, "benchmarks");
        projectRepository.insertProject("tinylog", NOW);
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
