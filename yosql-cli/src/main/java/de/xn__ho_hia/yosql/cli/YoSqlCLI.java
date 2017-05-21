/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.cli;

import static de.xn__ho_hia.yosql.cli.CliEvents.*;
import static de.xn__ho_hia.yosql.cli.Commands.GENERATE;
import static de.xn__ho_hia.yosql.cli.Commands.HELP;
import static de.xn__ho_hia.yosql.cli.Commands.VERSION;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import de.xn__ho_hia.yosql.BuildInfo;
import de.xn__ho_hia.yosql.YoSql;
import joptsimple.OptionException;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * Command line interface for YoSQL.
 */
public class YoSqlCLI {

    private static final Locale           SYSTEM_LOCALE    = Locale.ENGLISH;
    private static final IMessageConveyor SYSTEM_MESSAGES  = new MessageConveyor(SYSTEM_LOCALE);

    private static final PrintStream      ERRORS           = System.err;
    private static final PrintStream      OUT              = System.out;

    private static final String           HELP_COMMAND     = SYSTEM_MESSAGES.getMessage(HELP);
    private static final String           VERSION_COMMAND  = SYSTEM_MESSAGES.getMessage(VERSION);
    private static final String           GENERATE_COMMAND = SYSTEM_MESSAGES.getMessage(GENERATE);

    /**
     * @param arguments
     *            The CLI arguments.
     * @throws Exception
     *             In case anything goes wrong
     */
    public static void main(final String... arguments) throws Exception {
        final YoSqlCLIComponent cliComponent = buildCLiComponent(arguments);

        try {
            if (matchesCommand(HELP_COMMAND, arguments)) {
                printHelpText(cliComponent, arguments);
            } else if (matchesCommand(VERSION_COMMAND, arguments)) {
                printVersionText();
            } else {
                // when in doubt, generate files
                assertLoggerIsConfigured(cliComponent.rootLogger());
                generateCode(cliComponent.yoSql());
            }
            successfulTermination();
        } catch (final OptionException exception) {
            handleFailedOptions(cliComponent.generateParser(), exception.options());
        } catch (final Throwable throwable) {
            handleUnknownException(throwable);
        }
        abnormalTermination();
    }

    private static void generateCode(final YoSql yoSql) {
        yoSql.generateFiles();
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
        final OptionSet optionSet = helpParser.parser.parse(arguments);
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
        OUT.println(SYSTEM_MESSAGES.getMessage(HELP_REQUIRED));
    }

    private static void showHelpForGenerate(final YoSqlOptionParser generateParser) {
        OUT.println(SYSTEM_MESSAGES.getMessage(HELP_FOR_GENERATE));
        printCommandLineOptions(generateParser, OUT);
    }

    private static void showHelpForVersion(final YoSqlOptionParser versionParser) {
        OUT.println(SYSTEM_MESSAGES.getMessage(HELP_FOR_VERSION));
        printCommandLineOptions(versionParser, OUT);
    }

    private static void showHelpForHelp(final YoSqlOptionParser helpParser, final String commandName) {
        OUT.println(SYSTEM_MESSAGES.getMessage(DETAIL_HELP_REQUIRED, commandName));
        printCommandLineOptions(helpParser, OUT);
    }

    private static void printVersionText() {
        OUT.println(SYSTEM_MESSAGES.getMessage(INFORMATION_NEEDED,
                BuildInfo.VERSION, BuildInfo.BUILD, BuildInfo.REVISION, BuildInfo.BUILD_BY, BuildInfo.BUILD_AT));
    }

    private static void showHelpForUnknownCommand(final String commandName) {
        ERRORS.println(SYSTEM_MESSAGES.getMessage(UNKNOWN_COMMAND, commandName));
    }

    private static void handleFailedOptions(
            final YoSqlOptionParser generateParser,
            final Collection<String> failedOptions) {
        final Map<String, OptionSpec<?>> recognizedOptions = generateParser.parser.recognizedOptions();
        final Collection<String> similarOptions = findSimilarOptions(failedOptions, recognizedOptions);
        ERRORS.println(SYSTEM_MESSAGES.getMessage(PROBLEM_DURING_OPTION_PARSING,
                failedOptions.stream().collect(Collectors.joining(" --")), GENERATE_COMMAND, similarOptions)); //$NON-NLS-1$
        printCommandLineOptions(generateParser, ERRORS);
    }

    private static Collection<String> findSimilarOptions(
            final Collection<String> failedOptions,
            final Map<String, OptionSpec<?>> recognizedOptions) {
        final Set<String> similars = new TreeSet<>();
        final String[] availableOptions = recognizedOptions.keySet().stream()
                .filter(string -> !string.contains("arguments")) //$NON-NLS-1$
                .toArray(size -> new String[size]);
        if (availableOptions.length > 0) {
            findNearestMatch(failedOptions, similars, availableOptions);
        }
        return similars;
    }

    private static void findNearestMatch(
            final Collection<String> failedOptions,
            final Set<String> similars,
            final String[] availableOptions) {
        Arrays.sort(availableOptions);
        for (final String option : failedOptions) {
            final int index = Math.abs(Arrays.binarySearch(availableOptions, option)) - 1;
            similars.add(availableOptions[Math.max(index - 1, 0)]);
            similars.add(availableOptions[Math.max(Math.min(index, availableOptions.length - 1), 0)]);
            similars.add(availableOptions[Math.min(index + 1, availableOptions.length - 1)]);
        }
    }

    private static void printCommandLineOptions(final YoSqlOptionParser parser, final PrintStream output) {
        try {
            parser.parser.printHelpOn(output);
        } catch (final IOException exception) {
            handleUnknownException(exception);
        }
    }

    private static void handleUnknownException(final Throwable throwable) {
        throwable.printStackTrace(ERRORS);
    }

    private static void successfulTermination() {
        System.exit(0);
    }

    private static void abnormalTermination() {
        System.exit(1);
    }

}
