/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package com.example.app;

import com.zaxxer.hikari.HikariDataSource;

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
            // stop psql with 'docker-compose stop postgres'
        } else if (match(arguments, "mysql")) {
            // start mysql first with 'docker-compose up -d mysql'
            final var dataSource = new HikariDataSource();
            dataSource.setJdbcUrl("jdbc:mysql://localhost:51000/example?useSSL=false");
            dataSource.setUsername("example");
            dataSource.setPassword("example");
            runTests(arguments, dataSource);
            // stop mysql with 'docker-compose stop mysql'
        } else {
            // Use in-memory database
            final var dataSource = new HikariDataSource();
            dataSource.setJdbcUrl("jdbc:h2:mem:example;DB_CLOSE_DELAY=-1");
            dataSource.setUsername("sa");
            runTests(arguments, dataSource);
        }
    }

    private static void runTests(final String[] arguments, final DataSource dataSource) {
//        final var schemaRepository = new SchemaRepository(dataSource);
//        final var companyRepository = new CompanyRepository(dataSource);
//        final var personRepository = new PersonRepository(dataSource);

        if (match(arguments, "generic", "stream", "rxjava")) {
            // initializeDatabase(schemaRepository, companyRepository, personRepository);
        }

        if (match(arguments, "generic")) {
            // standardTests(companyRepository, personRepository);
        }
        if (match(arguments, "stream")) {
            // streamTests(companyRepository, personRepository);
        }
        if (match(arguments, "rxjava")) {
            // rxJavaTests(companyRepository, personRepository);
        }
    }

    private static boolean match(final String[] arguments, final String... values) {
        return Arrays.stream(arguments).anyMatch(argument -> Arrays.asList(values).contains(argument));
    }

}
