/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.constants.sql;

import wtf.metio.yosql.internals.junit5.EnumTCK;

import java.util.stream.Stream;

final class SqlTypeTest implements EnumTCK<SqlType> {

    @Override
    public Class<SqlType> getEnumClass() {
        return SqlType.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("READING", "WRITING", "CALLING", "UNKNOWN");
    }

    @Override
    public Stream<String> invalidValues() {
        return Stream.of("QUERY", "UPDATE", "CALL");
    }

}