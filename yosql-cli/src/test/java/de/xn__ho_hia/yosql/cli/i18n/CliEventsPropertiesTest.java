/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.cli.i18n;

import de.xn__ho_hia.yosql.cli.i18n.CliEvents;
import de.xn__ho_hia.yosql.testutils.PropertiesTCK;

final class CliEventsPropertiesTest extends PropertiesTCK<CliEvents> {

    @Override
    protected Class<CliEvents> getEnumClass() {
        return CliEvents.class;
    }

}
