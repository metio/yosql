package de.xn__ho_hia.yosql.acceptance;

import com.zaxxer.hikari.HikariDataSource;

import org.junit.jupiter.api.Assertions;

import cucumber.api.java.After;
import cucumber.api.java8.En;
import yosql.yep_2.PersonRepository;
import yosql.yep_2.SchemaRepository;

/**
 * Defines steps for YEP-2.
 */
@SuppressWarnings("nls")
public final class Yep2Steps implements En {

    private HikariDataSource dataSource;
    private PersonRepository repository;
    private int              updatedRows;

    /**
     * Creates the steps for YEP-2.
     */
    public Yep2Steps() {
        Given("database has schema", () -> {
            createDatSource(2);
            createRepository();
            initSchema();
        });
        When("the (\\w+) write method is called", (final String methodType) -> {
            switch (methodType) {
                case "batch":
                    updatedRows = repository.writePersonBatch(new int[] { 2 }, new String[] { "YEP-2" })[0];
                    break;
                case "standard":
                default:
                    updatedRows = repository.writePerson(2, "YEP-2");
            }
        });
        Then("data from the database should be returned", () -> {
            Assertions.assertEquals(1, updatedRows);
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
    }

    private final void createDatSource(final int yep) {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:YEP-" + yep);
        dataSource.setUsername("sa");
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(1);
    }

}
