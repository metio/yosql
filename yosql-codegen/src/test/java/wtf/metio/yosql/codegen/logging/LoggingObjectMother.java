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
import wtf.metio.yosql.internals.testing.configs.NamesConfigurations;

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
        final var fields = BlocksObjectMother.fields();
        generators.add(new JulLoggingGenerator(names, fields));
        generators.add(new Log4jLoggingGenerator(names, fields));
        generators.add(new Slf4jLoggingGenerator(names, fields));
        generators.add(new SystemLoggingGenerator(names, fields));
        generators.add(new ThatsInterestingLoggingGenerator());
        generators.add(new NoOpLoggingGenerator());
        return new DelegatingLoggingGenerator(LoggingConfigurations.jul(), generators, messages());
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
