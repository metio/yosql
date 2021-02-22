/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.logging;

import wtf.metio.yosql.internals.junit5.EnumTCK;

import java.util.stream.Stream;

final class LoggersTest implements EnumTCK<Loggers> {

    @Override
    public Class<Loggers> getEnumClass() {
        return Loggers.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of(
                "PARSER",
                "READER",
                "GENERATOR",
                "WRITER",
                "TIMER",
                "UTILITIES",
                "EXECUTIONS");
    }

}
