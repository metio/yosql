/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.benchmark.jdbc.jul;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import wtf.metio.yosql.benchmark.jdbc.AbstractBenchmark;
import wtf.metio.yosql.benchmark.jdbc.jul.persistence.CompanyRepository;
import wtf.metio.yosql.benchmark.jdbc.jul.persistence.DepartmentRepository;
import wtf.metio.yosql.benchmark.jdbc.jul.persistence.EmployeeRepository;
import wtf.metio.yosql.benchmark.jdbc.jul.persistence.ProjectRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.LogManager;

/**
 * Abstract benchmark class for all benchmarks that use the java.util.logging API.
 */
abstract class AbstractJulBenchmark extends AbstractBenchmark {

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

    @Setup(Level.Trial)
    public void setupJUL() throws IOException {
        final var classloader = Thread.currentThread().getContextClassLoader();
        final var julProperties = classloader.getResourceAsStream("logging.properties");
        LogManager.getLogManager().readConfiguration(julProperties);
    }

    @TearDown(Level.Trial)
    public void destroyRepositories() {
        companyRepository = null;
        departmentRepository = null;
        employeeRepository = null;
        projectRepository = null;
    }

}
