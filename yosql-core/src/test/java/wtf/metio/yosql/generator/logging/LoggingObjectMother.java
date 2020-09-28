/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.logging;

import wtf.metio.yosql.generator.api.LoggingGenerator;

import static wtf.metio.yosql.generator.logging.jdk.JdkLoggingObjectMother.jdkLoggingGenerator;
import static wtf.metio.yosql.generator.logging.log4j.Log4jLoggingObjectMother.log4jLoggingGenerator;
import static wtf.metio.yosql.generator.logging.noop.NoOpLoggingObjectMother.noOpLoggingGenerator;
import static wtf.metio.yosql.generator.logging.slf4j.Slf4jLoggingObjectMother.slf4jLoggingGenerator;

public final class LoggingObjectMother {

    public static LoggingGenerator loggingGenerator() {
        return new DelegatingLoggingGenerator(
                null,
                jdkLoggingGenerator(),
                log4jLoggingGenerator(),
                noOpLoggingGenerator(),
                slf4jLoggingGenerator()
        );
    }

    private LoggingObjectMother() {
        // factory class
    }

}
