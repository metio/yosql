/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.model;

import java.util.stream.Stream;

import de.xn__ho_hia.yosql.testutils.EnumTCK;

final class SqlTypeTest implements EnumTCK<SqlType> {

    @Override
    public Class<SqlType> getEnumClass() {
        return SqlType.class;
    }

    @Override
    @SuppressWarnings("nls")
    public Stream<String> validValues() {
        return Stream.of("READING", "WRITING", "CALLING");
    }

}
