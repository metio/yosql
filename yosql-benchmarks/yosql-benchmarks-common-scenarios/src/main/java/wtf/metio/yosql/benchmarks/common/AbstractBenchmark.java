/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmarks.common;

import com.zaxxer.hikari.HikariDataSource;
import org.openjdk.jmh.annotations.*;
import wtf.metio.yosql.benchmark.common.persistence.*;
import wtf.metio.yosql.testing.schema.SchemaRepository;

import java.io.IOException;

/**
 * Encapsulates common benchmark functionality.
 */
@State(Scope.Benchmark)
public abstract class AbstractBenchmark {

    protected HikariDataSource dataSource;
    protected SchemaRepository schemaRepository;
    protected CompanyRepository companyRepository;
    protected DepartmentRepository departmentRepository;
    protected EmployeeRepository employeeRepository;
    protected ProjectRepository projectRepository;
    protected ProjectEmployeeRepository projectEmployeeRepository;

    @Setup(Level.Trial)
    public void setup() throws IOException {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:benchmarks-jdbc;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        schemaRepository = new SchemaRepository(dataSource);
        companyRepository = new CompanyRepository(dataSource);
        departmentRepository = new DepartmentRepository(dataSource);
        schemaRepository.createCompaniesTable();
        schemaRepository.createProjectsTable();
        schemaRepository.createDepartmentsTable();
        schemaRepository.createEmployeesTable();
        schemaRepository.createProjectEmployeesTable();
        companyRepository.insertCompany("metio.wtf", "www");
        companyRepository.insertCompany("test", "musterstraße");
    }

    @TearDown(Level.Trial)
    public void tearDown() throws IOException {
        schemaRepository.dropProjectEmployeesTable();
        schemaRepository.dropEmployeesTable();
        schemaRepository.dropDepartmentTable();
        schemaRepository.dropProjectsTable();
        schemaRepository.dropCompaniesTable();
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
        schemaRepository = null;
        companyRepository = null;
    }

}
