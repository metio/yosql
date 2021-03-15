/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.yeps;


import io.cucumber.java.en.Given;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.ApiConfiguration;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.state.ConfigurationState;

/**
 * Defines steps for YEP-1.
 */
public final class ConfigurationSteps {

    private final ConfigurationState configurationState;

    public ConfigurationSteps(final ConfigurationState configurationState) {
        this.configurationState = configurationState;
    }

    @Given("YoSQL uses its defaults configuration")
    public void createDefaultConfiguration() {
        configurationState.setRuntimeConfiguration(RuntimeConfiguration.usingDefaults().build());
    }

    @Given("{} persistence is used")
    public void configureDaoApi(final PersistenceApis api) {
        configurationState.setRuntimeConfiguration(
                RuntimeConfiguration.copyOf(configurationState.getRuntimeConfiguration())
                        .withApi(ApiConfiguration.copyOf(configurationState.getRuntimeConfiguration().api())
                                .withDaoApi(api)));
    }

}
