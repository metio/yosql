/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.cli;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * Command line interface for YoSQL.
 */
public class YoSqlCLI {

    /**
     * @param arguments
     *            The CLI arguments.
     * @throws Exception
     *             In case anything goes wrong
     */
    public static void main(final String... arguments) throws Exception {
        setLogLevel(arguments);

        try {
            DaggerYoSqlCLIComponent.builder()
                    .jOptConfigurationModule(new JOptConfigurationModule(arguments))
                    .build()
                    .yosql()
                    .generateFiles();
        } catch (final Throwable throwable) {
            throwable.printStackTrace();
            System.exit(1);
        }
    }

    @SuppressWarnings("nls")
    private static void setLogLevel(final String... arguments) {
        final OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        final OptionSpec<String> logLevel = parser
                .accepts("logLevel")
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo("INFO")
                .describedAs("The logging level to use");

        final Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        final OptionSet options = parser.parse(arguments);
        root.setLevel(Level.valueOf(options.valueOf(logLevel).toUpperCase()));
    }

}
