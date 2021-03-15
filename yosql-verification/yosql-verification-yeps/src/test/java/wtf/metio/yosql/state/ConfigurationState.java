/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.state;

import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

public final class ConfigurationState {

    private RuntimeConfiguration runtimeConfiguration = RuntimeConfiguration.usingDefaults().build();

    public RuntimeConfiguration getRuntimeConfiguration() {
        return runtimeConfiguration;
    }

    public void setRuntimeConfiguration(final RuntimeConfiguration runtimeConfiguration) {
        this.runtimeConfiguration = runtimeConfiguration;
    }

}
