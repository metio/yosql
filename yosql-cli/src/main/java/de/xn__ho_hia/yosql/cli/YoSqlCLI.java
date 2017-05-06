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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import de.xn__ho_hia.yosql.BuildInfo;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * Command line interface for YoSQL.
 */
public class YoSqlCLI {

    private static final PrintStream      ERRORS           = System.err;
    private static final PrintStream      OUT              = System.out;
    private static final IMessageConveyor MESSAGES         = new MessageConveyor(Locale.ENGLISH);

    /** The name of the help command. */
    public static final String            HELP_COMMAND     = MESSAGES.getMessage(HELP);

    /** The name of the version command. */
    public static final String            VERSION_COMMAND  = MESSAGES.getMessage(VERSION);

    /** The name of the generate command. */
    public static final String            GENERATE_COMMAND = MESSAGES.getMessage(GENERATE);

    /**
     * @param arguments
     *            The CLI arguments.
     * @throws Exception
     *             In case anything goes wrong
     */
    public static void main(final String... arguments) throws Exception {
        final OptionParser generateParser = createParser();
        try {
            if (matchesCommand(HELP_COMMAND, arguments)) {
                printHelpText(arguments);
            } else if (matchesCommand(VERSION_COMMAND, arguments)) {
                printVersionText();
            } else {
                // when in doubt, generate files
                generateFiles(generateParser, arguments);
            }
            successfulTermination();
        } catch (final OptionException exception) {
            handleFailedOptions(generateParser, exception.options());
        } catch (final Throwable throwable) {
            handleUnknownException(throwable);
        }
        abnormalTermination();
    }

    private static boolean matchesCommand(final String commandName, final String[] arguments) {
        return arguments != null && arguments.length > 0 && commandName.equalsIgnoreCase(arguments[0]);
    }

    @SuppressWarnings("nls")
    private static void printHelpText(final String... arguments) {
        final OptionParser helpParser = createParser();
        final OptionSpec<String> command = helpParser
                .accepts("command")
                .withOptionalArg()
                .describedAs("The name of the command to show. Possible values are [generate, help, version].")
                .forHelp();
        final OptionSet optionSet = helpParser.parse(arguments);
        if (optionSet.has(command)) {
            final String commandName = optionSet.valueOf(command);
            if (HELP_COMMAND.equalsIgnoreCase(commandName)) {
                showHelpForHelp(helpParser, commandName);
            } else if (VERSION_COMMAND.equalsIgnoreCase(commandName)) {
                showHelpForVersion();
            } else if (GENERATE_COMMAND.equalsIgnoreCase(commandName)) {
                showHelpForGenerate();
            } else {
                showHelpForUnknownCommand(commandName);
                abnormalTermination();
            }
        } else {
            showGeneralHelp();
        }
    }

    private static void showGeneralHelp() {
        OUT.println(MESSAGES.getMessage(HELP_REQUIRED));
    }

    private static void showHelpForGenerate() {
        final OptionParser parser = createParser();
        final JOptConfigurationModule module = new JOptConfigurationModule(parser);
        final ExecutionErrors errors = new ExecutionErrors();
        module.provideExecutionConfiguration(module.providePathValueConverter(errors),
                module.provideResultRowConverterConverter(), MESSAGES);
        OUT.println(MESSAGES.getMessage(HELP_FOR_GENERATE));
        printCommandLineOptions(parser, OUT);
    }

    private static void showHelpForVersion() {
        OUT.println(MESSAGES.getMessage(HELP_FOR_VERSION));
    }

    private static void showHelpForUnknownCommand(final String commandName) {
        ERRORS.println(MESSAGES.getMessage(UNKNOWN_COMMAND, commandName));
    }

    private static void showHelpForHelp(final OptionParser helpParser, final String commandName) {
        OUT.println(MESSAGES.getMessage(DETAIL_HELP_REQUIRED, commandName));
        printCommandLineOptions(helpParser, OUT);
    }

    private static void printVersionText() {
        OUT.println(MESSAGES.getMessage(INFORMATION_NEEDED,
                BuildInfo.VERSION, BuildInfo.BUILD, BuildInfo.REVISION, BuildInfo.BUILD_BY, BuildInfo.BUILD_AT));
    }

    private static void generateFiles(final OptionParser parser, final String... arguments) {
        DaggerYoSqlCLIComponent.builder()
                .jOptConfigurationModule(new JOptConfigurationModule(parser, arguments))
                .build()
                .yosql()
                .generateFiles();
    }

    private static void handleFailedOptions(final OptionParser parser, final Collection<String> failedOptions) {
        final Map<String, OptionSpec<?>> recognizedOptions = parser.recognizedOptions();
        final Collection<String> similarOptions = findSimilarOptions(failedOptions, recognizedOptions);
        ERRORS.println(MESSAGES.getMessage(PROBLEM_DURING_OPTION_PARSING,
                failedOptions.stream().collect(Collectors.joining(" --")), GENERATE_COMMAND, similarOptions)); //$NON-NLS-1$
        printCommandLineOptions(parser, ERRORS);
    }

    private static Collection<String> findSimilarOptions(
            final Collection<String> failedOptions,
            final Map<String, OptionSpec<?>> recognizedOptions) {
        final List<String> similars = new ArrayList<>(failedOptions.size());
        final String[] array = recognizedOptions.keySet().stream()
                .filter(string -> !string.contains("arguments")) //$NON-NLS-1$
                .toArray(n -> new String[n]);
        if (array.length > 0) {
            Arrays.sort(array);
            for (final String option : failedOptions) {
                final int index = Math.abs(Arrays.binarySearch(array, option)) - 1;
                if (index > 0) {
                    similars.add(array[index - 1]);
                }
                similars.add(array[index]);
                if (index < array.length) {
                    similars.add(array[index + 1]);
                }
            }
        }
        return similars;
    }

    private static void printCommandLineOptions(final OptionParser parser, final PrintStream output) {
        try {
            parser.printHelpOn(output);
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

    /**
     * @return A new option parser with default configuration.
     */
    public static OptionParser createParser() {
        final OptionParser parser = new OptionParser();
        // parser.formatHelpWith(new BuiltinHelpFormatter(120, 5));
        parser.formatHelpWith(new YoSqlHelpFormatter());
        return parser;
    }

}
