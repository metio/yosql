/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import wtf.metio.yosql.benchmark.jdbc.persistence.CompanyRepository;
import wtf.metio.yosql.benchmark.jdbc.persistence.PersonRepository;
import wtf.metio.yosql.benchmark.jdbc.persistence.SchemaRepository;

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

    @Setup
    public void setup() throws IOException {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:benchmarks-jdbc;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        schemaRepository = new SchemaRepository(dataSource);
        schemaRepository.createCompaniesTable();
        schemaRepository.createItemsTable();
        schemaRepository.createPersonsTable();
        companyRepository = new CompanyRepository(dataSource);
        companyRepository.insertCompany(1, "metio.wtf");
        companyRepository.insertCompany(2, "test");
        personRepository = new PersonRepository(dataSource);
    }

    @TearDown
    public void tearDown() throws IOException {
        schemaRepository.dropPersonsTable();
        schemaRepository.dropItemsTable();
        schemaRepository.dropCompaniesTable();
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
        schemaRepository = null;
        companyRepository = null;
    }

}
