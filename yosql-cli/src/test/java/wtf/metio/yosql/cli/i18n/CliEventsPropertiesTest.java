/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.cli.i18n;

import wtf.metio.yosql.cli.i18n.CliEvents;
import wtf.metio.yosql.testutils.PropertiesTCK;

final class CliEventsPropertiesTest extends PropertiesTCK<CliEvents> {

    @Override
    protected Class<CliEvents> getEnumClass() {
        return CliEvents.class;
    }

}