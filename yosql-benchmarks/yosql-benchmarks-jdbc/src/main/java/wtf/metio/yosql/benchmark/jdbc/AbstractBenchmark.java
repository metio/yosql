/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.openjdk.jmh.annotations.*;
import wtf.metio.yosql.benchmark.jdbc.persistence.CompanyRepository;
import wtf.metio.yosql.benchmark.jdbc.persistence.PersonRepository;
import wtf.metio.yosql.testing.schema.SchemaRepository;

import java.io.IOException;

/**
 * Encapsulates common benchmark functionality.
 */
@State(Scope.Benchmark)
abstract class AbstractBenchmark {
    
    protected HikariDataSource dataSource;
    protected SchemaRepository schemaRepository;
    protected CompanyRepository companyRepository;
    protected PersonRepository personRepository;

    @Setup(Level.Trial)
    public void setup() throws IOException {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:benchmarks-jdbc;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        schemaRepository = new SchemaRepository(dataSource);
        companyRepository = new CompanyRepository(dataSource);
        personRepository = new PersonRepository(dataSource);
        schemaRepository.createCompaniesTable();
        schemaRepository.createProjectsTable();
        schemaRepository.createDepartmentsTable();
        schemaRepository.createEmployeesTable();
        schemaRepository.createProjectEmployeesTable();
        companyRepository.insertCompany("metio.wtf");
        companyRepository.insertCompany("test");
    }

    @TearDown(Level.Trial)
    public void tearDown() throws IOException {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
        schemaRepository = null;
        companyRepository = null;
        personRepository = null;
    }

}
