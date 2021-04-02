/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.example.maven.jdbc.java16;

import com.zaxxer.hikari.HikariDataSource;
import wtf.metio.yosql.example.maven.jdbc.java16.persistence.CompanyRepository;
import wtf.metio.yosql.example.maven.jdbc.java16.persistence.PersonRepository;
import wtf.metio.yosql.example.maven.jdbc.java16.persistence.SchemaRepository;
import wtf.metio.yosql.example.maven.jdbc.java16.persistence.util.ToResultRowConverter;

import javax.sql.DataSource;
import java.util.Arrays;

public class ExampleApp {

    public static void main(final String[] arguments) {
        if (match(arguments, "psql")) {
            // start psql first with 'docker-compose up -d postgres'
            final var dataSource = new HikariDataSource();
            dataSource.setJdbcUrl("jdbc:postgresql://localhost:50000/example");
            dataSource.setUsername("example");
            dataSource.setPassword("example");
            runTests(arguments, dataSource);
        } else if (match(arguments, "mysql")) {
            // start mysql first with 'docker-compose up -d mysql'
            final var dataSource = new HikariDataSource();
            dataSource.setJdbcUrl("jdbc:mysql://localhost:51000/example?useSSL=false");
            dataSource.setUsername("example");
            dataSource.setPassword("example");
            runTests(arguments, dataSource);
        } else {
            // Use in-memory database
            final var dataSource = new HikariDataSource();
            dataSource.setJdbcUrl("jdbc:h2:mem:example;DB_CLOSE_DELAY=-1");
            dataSource.setUsername("sa");
            runTests(arguments, dataSource);
        }
    }

    private static void runTests(final String[] arguments, final DataSource dataSource) {
        final var schemaRepository = new SchemaRepository(dataSource);
        final var resultRow = new ToResultRowConverter();
        final var companyRepository = new CompanyRepository(dataSource, resultRow);
        final var personRepository = new PersonRepository(dataSource, resultRow);

        if (match(arguments, "generic", "stream", "rxjava")) {
            initializeDatabase(schemaRepository, companyRepository, personRepository);
        }
        if (match(arguments, "generic")) {
            standardTests(companyRepository, personRepository);
        }
        if (match(arguments, "stream")) {
            streamTests(companyRepository, personRepository);
        }
    }

    private static boolean match(final String[] arguments, final String... values) {
        return Arrays.stream(arguments).anyMatch(argument -> Arrays.asList(values).contains(argument));
    }

    private static void initializeDatabase(
            final SchemaRepository schemaRepository,
            final CompanyRepository companyRepository,
            final PersonRepository personRepository) {
        createSchema(schemaRepository);
        writeCompanies(companyRepository);
    }

    private static void createSchema(final SchemaRepository schemaRepository) {
        schemaRepository.dropCompaniesTable();
        schemaRepository.dropPersonsTable();
        schemaRepository.dropItemsTable();

        schemaRepository.createCompaniesTable();
        schemaRepository.createPersonsTable();
        schemaRepository.createItemsTable();
    }

    private static void writeCompanies(final CompanyRepository companyRepository) {
        companyRepository.insertCompany(1, "test");
        companyRepository.insertCompany(2, "two");
        companyRepository.insertCompany(3,"three");
    }

    private static void standardTests(
            final CompanyRepository companyRepository,
            final PersonRepository personRepository) {
        companyRepository.queryAllCompanies();
        companyRepository.findCompanyByName("test");
        companyRepository.findCompanies(10, 20);
        personRepository.findPerson("test");
    }

    private static void streamTests(
            final CompanyRepository companyRepository,
            final PersonRepository personRepository) {
        companyRepository.queryAllCompanies();
    }

}
