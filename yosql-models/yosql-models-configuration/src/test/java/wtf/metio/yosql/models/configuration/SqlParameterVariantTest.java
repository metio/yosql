/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.configuration;

import wtf.metio.yosql.internals.junit5.EnumTCK;

import java.util.stream.Stream;

class SqlParameterVariantTest implements EnumTCK<SqlParameterVariant> {

    @Override
    public Class<SqlParameterVariant> getEnumClass() {
        return SqlParameterVariant.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("IN", "OUT", "INOUT");
    }

    @Override
    public Stream<String> invalidValues() {
        return Stream.of("IN_OUT", "INPUT", "OUTPUT");
    }

}
