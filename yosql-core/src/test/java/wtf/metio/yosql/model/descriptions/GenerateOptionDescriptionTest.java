/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.model.descriptions;

import org.junit.jupiter.api.DisplayName;
import wtf.metio.yosql.testutils.PropertiesTCK;

@DisplayName("GenerateOptionDescriptions")
final class GenerateOptionDescriptionTest implements PropertiesTCK<GenerateOptionsDescriptions> {

    @Override
    public Class<GenerateOptionsDescriptions> getEnumClass() {
        return GenerateOptionsDescriptions.class;
    }

}
