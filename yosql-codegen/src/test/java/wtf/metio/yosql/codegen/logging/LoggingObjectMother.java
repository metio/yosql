/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.logging;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;
import wtf.metio.yosql.codegen.blocks.BlocksObjectMother;
import wtf.metio.yosql.internals.jdk.SupportedLocales;
import wtf.metio.yosql.internals.testing.configs.LoggingConfigurations;
import wtf.metio.yosql.models.configuration.LoggingApis;
import wtf.metio.yosql.models.immutables.LoggingConfiguration;

import java.util.LinkedHashMap;

/**
 * Object mother for logging related classes.
 */
public final class LoggingObjectMother {

    /**
     * @return Delegating logging generator with all available generators using their default config.
     */
    public static LoggingGenerator loggingGenerator() {
        return loggingGenerator(LoggingConfigurations.jul());
    }

    /**
     * @param logging which API the generated repositories should log through
     */
    public static LoggingGenerator loggingGenerator(final LoggingConfiguration logging) {
        final var fields = BlocksObjectMother.fields();
        final var generators = new LinkedHashMap<LoggingApis, LoggingGenerator>();
        generators.put(LoggingApis.JUL, new JulLoggingGenerator(fields));
        generators.put(LoggingApis.LOG4J, new Log4jLoggingGenerator(fields));
        generators.put(LoggingApis.SLF4J, new Slf4jLoggingGenerator(fields));
        generators.put(LoggingApis.SYSTEM, new SystemLoggingGenerator(fields));
        generators.put(LoggingApis.TI, new ThatsInterestingLoggingGenerator());
        generators.put(LoggingApis.TINYLOG, new TinylogLoggingGenerator());
        generators.put(LoggingApis.NONE, new NoOpLoggingGenerator());
        return new DelegatingLoggingGenerator(logging, generators, messages());
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
