/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
