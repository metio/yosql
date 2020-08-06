/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.yeps;

import org.junit.jupiter.api.Assertions;
import wtf.metio.yosql.utils.AbstractDatabaseSteps;

/**
 * Defines steps for YEP-2.
 */
public final class Yep2Steps extends AbstractDatabaseSteps {

//    private PersonRepository repository;
//    private int updatedRows;

    /**
     * Creates the steps for YEP-2.
     */
    public Yep2Steps() {
        super(2);
        Given("database has schema", () -> {
//            repository = new PersonRepository(dataSource);
//            final var schemaRepository = new SchemaRepository(dataSource);
//            schemaRepository.dropTables();
//            schemaRepository.createTables();
        });
        When("the {word} read method is called", (final String methodType) -> {
//            switch (methodType) {
//                case "batch" -> updatedRows = repository.writePersonBatch(new int[]{2}, new String[]{"YEP-2"})[0];
//                default -> updatedRows = repository.writePerson(2, "YEP-2");
//            }
        });
        Then("data from the database should be returned", () -> {
            // Assertions.assertEquals(1, updatedRows);
        });
    }

}
