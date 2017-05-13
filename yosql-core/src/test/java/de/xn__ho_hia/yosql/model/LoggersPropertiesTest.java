/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.model;

import de.xn__ho_hia.yosql.testutils.PropertiesTCK;

final class LoggersPropertiesTest extends PropertiesTCK<Loggers> {

    @Override
    public Class<Loggers> getEnumClass() {
        return Loggers.class;
    }

}
