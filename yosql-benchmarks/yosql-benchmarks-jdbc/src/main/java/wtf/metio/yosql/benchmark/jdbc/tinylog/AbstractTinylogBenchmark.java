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

/**
 * Abstract benchmark class for all benchmarks that use the tinylog API.
 */
abstract class AbstractTinylogBenchmark extends AbstractBenchmark {

    protected CompanyRepository companyRepository;
    protected DepartmentRepository departmentRepository;
    protected EmployeeRepository employeeRepository;
    protected ProjectRepository projectRepository;

    @Setup(Level.Trial)
    public void createRepositories() {
        companyRepository = new CompanyRepository(dataSource);
        departmentRepository = new DepartmentRepository(dataSource);
        employeeRepository = new EmployeeRepository(dataSource);
        projectRepository = new ProjectRepository(dataSource);
    }

    @TearDown(Level.Trial)
    public void destroyRepositories() {
        companyRepository = null;
        departmentRepository = null;
        employeeRepository = null;
        projectRepository = null;
    }

}
