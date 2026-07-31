/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.models.constants.api;

import wtf.metio.yosql.internals.junit5.EnumTCK;
import wtf.metio.yosql.models.configuration.LoggingApis;

import java.util.stream.Stream;

final class LoggingApisTest implements EnumTCK<LoggingApis> {

    @Override
    public Class<LoggingApis> getEnumClass() {
        return LoggingApis.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("NONE", "JUL", "SYSTEM", "LOG4J", "SLF4J", "TI", "TINYLOG");
    }

}
