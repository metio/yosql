/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.yeps;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;

import wtf.metio.yosql.utils.AbstractDatabaseSteps;
import yosql.yep_1.PersonRepository;
import yosql.yep_1.util.ResultRow;

import javax.sql.DataSource;

/**
 * Defines steps for YEP-1.
 */
public final class Yep1Steps extends AbstractDatabaseSteps {

    private PersonRepository repository;
    private List<ResultRow>  data;

    /**
     * Creates the steps for YEP-1.
     */
    public Yep1Steps() {
      super(1);
        Given("database has data", () -> {
          repository = new PersonRepository(dataSource);
        });
        When("the standard read method is called", () -> {
          data = repository.readPerson();
        });
        Then("data should be returned from the database", () -> {
            Assertions.assertFalse(data.isEmpty());
        });
    }

}
