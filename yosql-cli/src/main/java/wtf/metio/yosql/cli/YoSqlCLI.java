/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.cli;

import static wtf.metio.yosql.cli.i18n.CliEvents.*;
import static wtf.metio.yosql.cli.i18n.Commands.GENERATE;
import static wtf.metio.yosql.cli.i18n.Commands.HELP;
import static wtf.metio.yosql.cli.i18n.Commands.VERSION;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import wtf.metio.yosql.YoSql;
import wtf.metio.yosql.cli.dagger.DaggerYoSqlCLIComponent;
import wtf.metio.yosql.cli.dagger.YoSqlCLIComponent;
import wtf.metio.yosql.cli.parser.YoSqlOptionParser;
import wtf.metio.yosql.model.Loggers;
import joptsimple.OptionException;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * Command line interface for YoSQL.
 */
public class YoSqlCLI {

    private static final Locale           SYSTEM_LOCALE    = Locale.ENGLISH;
    private static final IMessageConveyor SYSTEM_MESSAGES  = new MessageConveyor(SYSTEM_LOCALE);
    private static final LocLoggerFactory LOGGER_FACTORY   = new LocLoggerFactory(SYSTEM_MESSAGES);
    private static final LocLogger        LOGGER           = LOGGER_FACTORY
            .getLocLogger(SYSTEM_MESSAGES.getMessage(Loggers.CLI));

    private static final String           HELP_COMMAND     = SYSTEM_MESSAGES.getMessage(HELP);
    private static final String           VERSION_COMMAND  = SYSTEM_MESSAGES.getMessage(VERSION);
    private static final String           GENERATE_COMMAND = SYSTEM_MESSAGES.getMessage(GENERATE);

    /**
     * @param arguments
     *            The CLI arguments.
     */
    public static void main(final String... arguments) {
        final YoSqlCLIComponent cliComponent = buildCLiComponent(arguments);

        try {
            if (matchesCommand(HELP_COMMAND, arguments)) {
                printHelpText(cliComponent, arguments);
            } else if (matchesCommand(VERSION_COMMAND, arguments)) {
                printVersionText();
            } else {
                // when in doubt, generate code
                assertLoggerIsConfigured(cliComponent.rootLogger());
                generateCode(cliComponent.yoSql());
            }
            successfulTermination();
        } catch (final OptionException exception) {
            handleUnknownGenerateOptions(cliComponent.generateParser(), exception.options());
        } catch (final Exception throwable) {
            handleUnknownException(throwable);
        }
        abnormalTermination();
    }

    private static void generateCode(final YoSql yoSql) {
        yoSql.generateCode();
    }

    private static void assertLoggerIsConfigured(final Logger rootLogger) {
        assert rootLogger != null;
    }

    private static YoSqlCLIComponent buildCLiComponent(final String... arguments) {
        return DaggerYoSqlCLIComponent.builder()
                .arguments(arguments)
                .build();
    }

    private static boolean matchesCommand(final String commandName, final String[] arguments) {
        return arguments != null && arguments.length > 0 && commandName.equalsIgnoreCase(arguments[0]);
    }

    private static void printHelpText(final YoSqlCLIComponent yoSqlCli, final String... arguments) {
        final YoSqlOptionParser helpParser = yoSqlCli.helpParser();
        final OptionSpec<String> command = yoSqlCli.helpCommandOption();
        final OptionSet optionSet = helpParser.parse(arguments);
        if (optionSet.has(command)) {
            final String commandName = optionSet.valueOf(command);
            if (HELP_COMMAND.equalsIgnoreCase(commandName)) {
                showHelpForHelp(helpParser, commandName);
            } else if (VERSION_COMMAND.equalsIgnoreCase(commandName)) {
                showHelpForVersion(yoSqlCli.versionParser());
            } else if (GENERATE_COMMAND.equalsIgnoreCase(commandName)) {
                showHelpForGenerate(yoSqlCli.generateParser());
            } else {
                showHelpForUnknownCommand(commandName);
                abnormalTermination();
            }
        } else {
            showGeneralHelp();
        }
    }

    private static void showGeneralHelp() {
        LOGGER.info(HELP_REQUIRED);
    }

    private static void showHelpForGenerate(final YoSqlOptionParser generateParser) {
        LOGGER.info(HELP_FOR_GENERATE);
        printCommandLineOptions(generateParser, LOGGER::info);
    }

    private static void showHelpForVersion(final YoSqlOptionParser versionParser) {
        LOGGER.info(HELP_FOR_VERSION);
        printCommandLineOptions(versionParser, LOGGER::info);
    }

    private static void showHelpForHelp(final YoSqlOptionParser helpParser, final String commandName) {
        LOGGER.info(DETAIL_HELP_REQUIRED, commandName);
        printCommandLineOptions(helpParser, LOGGER::info);
    }

    private static void printVersionText() {
        LOGGER.info(VERSION_INFORMATION_REQUESTED,
                BuildInfo.VERSION, BuildInfo.BUILD, BuildInfo.REVISION, BuildInfo.BUILD_BY, BuildInfo.BUILD_AT);
    }

    private static void showHelpForUnknownCommand(final String commandName) {
        LOGGER.error(UNKNOWN_COMMAND, commandName);
    }

    private static void handleUnknownGenerateOptions(
            final YoSqlOptionParser generateParser,
            final Collection<String> failedOptions) {
        final String[] recognizedOptions = generateParser.recognizedOptions();
        if (recognizedOptions.length > 0) {
            final Collection<String> similarOptions = findSimilarOptions(failedOptions, recognizedOptions);
            logProblematicGenerateOptions(failedOptions, similarOptions);
        }
        printCommandLineOptions(generateParser, LOGGER::error);
    }

    private static void logProblematicGenerateOptions(
            final Collection<String> failedOptions,
            final Collection<String> similarOptions) {
        LOGGER.error(PROBLEM_DURING_OPTION_PARSING,
                formatOptions(failedOptions), GENERATE_COMMAND, similarOptions);
    }

    private static String formatOptions(final Collection<String> failedOptions) {
        return String.join(" --", failedOptions); //$NON-NLS-1$
    }

    private static Collection<String> findSimilarOptions(
            final Collection<String> failedOptions,
            final String[] recognizedOptions) {
        final Set<String> similars = new TreeSet<>();
        if (recognizedOptions.length > 0) {
            similars.addAll(findNearestMatch(failedOptions, recognizedOptions));
        }
        return similars;
    }

    private static Set<String> findNearestMatch(
            final Collection<String> failedOptions,
            final String[] recognizedOptions) {
        final Set<String> nearestMatch = new TreeSet<>();
        for (final String option : failedOptions) {
            final int index = Math.abs(Arrays.binarySearch(recognizedOptions, option)) - 1;
            if (index == recognizedOptions.length) {
                nearestMatch.add(recognizedOptions[Math.max(index - 2, 0)]);
            }
            nearestMatch.add(recognizedOptions[Math.max(index - 1, 0)]);
            nearestMatch.add(recognizedOptions[Math.max(Math.min(index, recognizedOptions.length - 1), 0)]);
            if (index == 0) {
                nearestMatch.add(recognizedOptions[Math.min(index + 1, recognizedOptions.length - 1)]);
            }
        }
        return nearestMatch;
    }

    private static void printCommandLineOptions(final YoSqlOptionParser parser, final Consumer<String> output) {
        try {
            output.accept(parser.printHelp());
        } catch (final IOException exception) {
            handleUnknownException(exception);
        }
    }

    private static void handleUnknownException(final Exception exception) {
        LOGGER.error(UNKNOWN_EXCEPTION, exception);
    }

    private static void successfulTermination() {
        System.exit(0);
    }

    private static void abnormalTermination() {
        System.exit(1);
    }

}
