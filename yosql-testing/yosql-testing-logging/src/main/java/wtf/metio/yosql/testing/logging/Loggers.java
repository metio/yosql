/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.testing.logging;

import wtf.metio.yosql.logging.api.DelegatingLoggingGenerator;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.logging.jul.JulLoggingGenerator;
import wtf.metio.yosql.logging.log4j.Log4jLoggingGenerator;
import wtf.metio.yosql.logging.noop.NoOpLoggingGenerator;
import wtf.metio.yosql.logging.slf4j.Slf4jLoggingGenerator;
import wtf.metio.yosql.testing.codegen.Blocks;
import wtf.metio.yosql.testing.configs.Apis;

public final class Loggers {

    public static LoggingGenerator loggingGenerator() {
        return new DelegatingLoggingGenerator(Apis.defaults(),
                new JulLoggingGenerator(Blocks.names(), Blocks.fields()),
                new Log4jLoggingGenerator(Blocks.names(), Blocks.fields()),
                new NoOpLoggingGenerator(),
                new Slf4jLoggingGenerator(Blocks.names(), Blocks.fields()));
    }

    private Loggers() {
        // factory class
    }
    
}
