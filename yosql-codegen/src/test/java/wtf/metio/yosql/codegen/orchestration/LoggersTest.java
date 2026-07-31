/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.orchestration;

import wtf.metio.yosql.internals.junit5.EnumTCK;

import java.util.stream.Stream;

class LoggersTest implements EnumTCK<Loggers> {

    @Override
    public Class<Loggers> getEnumClass() {
        return Loggers.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of(
                "PARSER",
                "READER",
                "REPOSITORIES",
                "WRITER",
                "TIMER",
                "CONVERTERS",
                "EXECUTIONS");
    }

}
