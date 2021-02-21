/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.codegen.model.options;

import org.junit.jupiter.api.DisplayName;
import wtf.metio.yosql.internals.utils.test.EnumTCK;

import java.util.stream.Stream;

@DisplayName("LoggingApiOptions")
final class LoggingApiOptionsTest implements EnumTCK<LoggingApiOptions> {

    @Override
    public Class<LoggingApiOptions> getEnumClass() {
        return LoggingApiOptions.class;
    }

    @Override
    public Stream<String> validValues() {
        return Stream.of("NONE", "JDK", "LOG4J", "SLF4J");
    }

}
