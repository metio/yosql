package de.xn__ho_hia.yosql.acceptance;

import cucumber.api.java8.En;

/**
 * Defines steps for YEP-1.
 */
@SuppressWarnings("nls")
public class Yep1Steps implements En {

    /**
     * Creates the steps for YEP-1.
     */
    public Yep1Steps() {
        Given("A repository with a standard read method exists", () -> {
            // TODO: create repository
        });
        When("the method is called", () -> {
            // TODO: call standard read method
        });
        Then("data from a database should be returned", () -> {
            // TODO: verify returned data
        });
    }

}
