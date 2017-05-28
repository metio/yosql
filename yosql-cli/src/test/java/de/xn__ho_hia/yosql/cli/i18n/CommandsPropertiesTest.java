/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.cli.i18n;

import de.xn__ho_hia.yosql.cli.i18n.Commands;
import de.xn__ho_hia.yosql.testutils.PropertiesTCK;

final class CommandsPropertiesTest extends PropertiesTCK<Commands> {

    @Override
    protected Class<Commands> getEnumClass() {
        return Commands.class;
    }

}
