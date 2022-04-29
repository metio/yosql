/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.testing.logging;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;
import wtf.metio.yosql.internals.jdk.SupportedLocales;
import wtf.metio.yosql.logging.api.DelegatingLoggingGenerator;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.logging.jul.JulLoggingGenerator;
import wtf.metio.yosql.logging.log4j.Log4jLoggingGenerator;
import wtf.metio.yosql.logging.noop.NoOpLoggingGenerator;
import wtf.metio.yosql.logging.slf4j.Slf4jLoggingGenerator;
import wtf.metio.yosql.logging.system.SystemLoggingGenerator;
import wtf.metio.yosql.logging.ti.ThatsInteresingLoggingGenerator;
import wtf.metio.yosql.testing.codegen.Blocks;
import wtf.metio.yosql.testing.configs.ApiConfigurations;
import wtf.metio.yosql.testing.configs.NamesConfigurations;

import java.util.LinkedHashSet;

/**
 * Object mother for logging related classes.
 */
public final class LoggingObjectMother {

    /**
     * @return Delegating logging generator with all available generators using their default config.
     */
    public static LoggingGenerator loggingGenerator() {
        final var generators = new LinkedHashSet<LoggingGenerator>();
        final var names = NamesConfigurations.defaults();
        final var fields = Blocks.fields();
        generators.add(new JulLoggingGenerator(names, fields));
        generators.add(new Log4jLoggingGenerator(names, fields));
        generators.add(new Slf4jLoggingGenerator(names, fields));
        generators.add(new SystemLoggingGenerator(names, fields));
        generators.add(new ThatsInteresingLoggingGenerator());
        generators.add(new NoOpLoggingGenerator());
        return new DelegatingLoggingGenerator(ApiConfigurations.jul(), generators);
    }

    /**
     * @return Messages in the english language.
     */
    public static IMessageConveyor messages() {
        return new MessageConveyor(SupportedLocales.ENGLISH);
    }

    /**
     * @return Localized logger for test execution.
     */
    public static LocLogger logger() {
        return new LocLoggerFactory(messages()).getLocLogger("yosql.test");
    }

    private LoggingObjectMother() {
        // factory class
    }

}
