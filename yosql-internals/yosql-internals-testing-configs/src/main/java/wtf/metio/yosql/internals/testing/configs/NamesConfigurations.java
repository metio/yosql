/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.testing.configs;

import wtf.metio.yosql.models.immutables.NamesConfiguration;

/**
 * Object mother for {@link NamesConfiguration}s.
 */
public final class NamesConfigurations {

    public static NamesConfiguration defaults() {
        return NamesConfiguration.builder().build();
    }

    private NamesConfigurations() {
        // factory class
    }

}
