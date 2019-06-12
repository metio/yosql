package wtf.metio.yosql.utils;

import cucumber.api.java.After;
import cucumber.api.java8.En;

import javax.sql.DataSource;

public abstract class AbstractDatabaseSteps implements En {

  protected DataSource dataSource;

  /**
   * Creates the steps for a single YEP.
   */
  public AbstractDatabaseSteps(final int yep) {
    Given("database has data", () -> {
      dataSource = DataSources.createDataSource(yep);
      Schemata.initSchema(dataSource);
    });
  }

  /**
   * Executes after the entire scenario finishes.
   */
  @After
  public void afterScenario() {
    dataSource.close();
  }

}
