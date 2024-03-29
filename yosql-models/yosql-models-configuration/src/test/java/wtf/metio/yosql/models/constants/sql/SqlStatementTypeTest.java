/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.constants.sql;

import wtf.metio.yosql.internals.junit5.EnumTCK;
import wtf.metio.yosql.models.configuration.SqlStatementType;

import java.util.stream.Stream;

final class SqlStatementTypeTest implements EnumTCK<SqlStatementType> {

    @Override
    public Class<SqlStatementType> getEnumClass() {
        return SqlStatementType.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("READING", "WRITING", "CALLING");
    }

    @Override
    public Stream<String> invalidValues() {
        return Stream.of("QUERY", "UPDATE", "CALL");
    }

}
