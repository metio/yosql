package wtf.metio.yosql.utils;

import io.cucumber.java8.En;

import javax.sql.DataSource;

public abstract class AbstractDatabaseSteps implements En {

    protected DataSource dataSource;

    /**
     * Creates the steps for a single YEP.
     */
    public AbstractDatabaseSteps(final int yep) {
        Given("database has schema", () -> {
            dataSource = DataSources.createDataSource(yep);
            Schemata.initSchema(dataSource);
        });
        Given("database has data", () -> {
            // TODO: write test data
        });
    }

}
