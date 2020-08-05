/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.cli;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;
import wtf.metio.yosql.DaggerYoSqlComponent;
import wtf.metio.yosql.YoSql;
import wtf.metio.yosql.model.internal.Loggers;

import java.util.Locale;

import static wtf.metio.yosql.cli.i18n.CliEvents.UNKNOWN_EXCEPTION;

/**
 * Command line interface for YoSQL.
 */
public class YoSqlCLI {

    private static final Locale SYSTEM_LOCALE = Locale.ENGLISH;
    private static final IMessageConveyor SYSTEM_MESSAGES = new MessageConveyor(SYSTEM_LOCALE);
    private static final LocLoggerFactory LOGGER_FACTORY = new LocLoggerFactory(SYSTEM_MESSAGES);
    private static final LocLogger LOGGER = LOGGER_FACTORY.getLocLogger(SYSTEM_MESSAGES.getMessage(Loggers.CLI));

    /**
     * @param arguments The CLI arguments.
     */
    public static void main(final String... arguments) {
        final var yosql = createYoSql(arguments);

        try {
            yosql.generateCode();
            System.exit(0);
        } catch (final Exception exception) {
            LOGGER.error(UNKNOWN_EXCEPTION, exception);
        }
        System.exit(1);
    }

    private static YoSql createYoSql(final String... arguments) {
        return DaggerYoSqlComponent.builder().build().yosql();
    }

}
