/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.acceptance;

import java.util.List;
import java.util.stream.Collectors;

import com.zaxxer.hikari.HikariDataSource;

import org.junit.jupiter.api.Assertions;

import cucumber.api.java.After;
import cucumber.api.java8.En;
import yosql.yep_1.PersonRepository;
import yosql.yep_1.SchemaRepository;
import yosql.yep_1.util.ResultRow;

/**
 * Defines steps for YEP-1.
 */
@SuppressWarnings("nls")
public final class Yep1Steps implements En {

    private HikariDataSource dataSource;
    private PersonRepository repository;
    private List<ResultRow>  data;

    /**
     * Creates the steps for YEP-1.
     */
    public Yep1Steps() {
        Given("database has data", () -> {
            createDataSource(1);
            createRepository();
            initSchema();
        });
        When("the (\\w+) read method is called", (final String methodType) -> {
            switch (methodType) {
                case "stream lazy":
                    data = repository.readPersonStreamLazy().collect(Collectors.toList());
                    break;
                case "stream":
                    data = repository.readPersonStreamEager().collect(Collectors.toList());
                    break;
                case "rxjava":
                    data = repository.readPersonFlow().toList().blockingGet();
                    break;
                case "standard":
                default:
                    data = repository.readPerson();
            }
        });
        Then("data from the database should be returned", () -> {
            Assertions.assertFalse(data.isEmpty());
        });
    }

    /**
     * Executes after the entire scenario finishes.
     */
    @After
    public void afterScenario() {
        dataSource.close();
    }

    private void createRepository() {
        repository = new PersonRepository(dataSource);
    }

    private void initSchema() {
        final SchemaRepository schemaRepository = new SchemaRepository(dataSource);
        schemaRepository.dropTables();
        schemaRepository.createTables();
        repository.writePerson(1, "YEP-1");
    }

    private final void createDataSource(final int yep) {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:YEP-" + yep);
        dataSource.setUsername("sa");
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(1);
    }

}
