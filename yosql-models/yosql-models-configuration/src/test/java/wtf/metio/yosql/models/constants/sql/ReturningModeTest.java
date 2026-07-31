/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.constants.sql;

import wtf.metio.yosql.internals.junit5.EnumTCK;
import wtf.metio.yosql.models.configuration.ReturningMode;

import java.util.stream.Stream;

final class ReturningModeTest implements EnumTCK<ReturningMode> {

    @Override
    public Class<ReturningMode> getEnumClass() {
        return ReturningMode.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("NONE", "SINGLE", "MULTIPLE", "CURSOR");
    }

    @Override
    public Stream<String> invalidValues() {
        return Stream.of("LIST", "FIRST", "ONE");
    }
}
