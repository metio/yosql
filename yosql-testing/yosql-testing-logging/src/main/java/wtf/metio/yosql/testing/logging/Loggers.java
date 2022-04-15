/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
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
import wtf.metio.yosql.logging.system.SystemLoggingGenerator;
import wtf.metio.yosql.logging.ti.ThatsInteresingLoggingGenerator;
import wtf.metio.yosql.testing.codegen.Blocks;
import wtf.metio.yosql.testing.configs.Apis;

import java.util.LinkedHashSet;

public final class Loggers {

    public static LoggingGenerator loggingGenerator() {
        final var generators = new LinkedHashSet<LoggingGenerator>();
        generators.add(new JulLoggingGenerator(Blocks.names(), Blocks.fields()));
        generators.add(new Log4jLoggingGenerator(Blocks.names(), Blocks.fields()));
        generators.add(new Slf4jLoggingGenerator(Blocks.names(), Blocks.fields()));
        generators.add(new SystemLoggingGenerator(Blocks.names(), Blocks.fields()));
        generators.add(new ThatsInteresingLoggingGenerator());
        generators.add(new NoOpLoggingGenerator());
        return new DelegatingLoggingGenerator(Apis.jul(), generators);
    }

    private Loggers() {
        // factory class
    }

}
