/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.immutables;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NamesConfiguration")
class NamesConfigurationTest {

    @Test
    void shouldDetectDuplicatedNames() {
        final var names = NamesConfiguration.usingDefaults().build();
        // TODO: add validation method to config
        //        final var validation = names.validateConfiguration();
        //        assertTrue(validation.isEmpty());
    }

}
