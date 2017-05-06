/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.cli;

import static de.xn__ho_hia.yosql.cli.CliEvents.HELP_REQUIRED;
import static de.xn__ho_hia.yosql.cli.CliEvents.INFORMATION_NEEDED;
import static de.xn__ho_hia.yosql.cli.CliEvents.PROBLEM_DURING_OPTION_PARSING;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import de.xn__ho_hia.yosql.BuildInfo;
import joptsimple.OptionParser;
import joptsimple.OptionSpec;

/**
 * Command line interface for YoSQL.
 */
public class YoSqlCLI {

    private static final PrintStream ERRORS = System.err;

    /**
     * @param arguments
     *            The CLI arguments.
     * @throws Exception
     *             In case anything goes wrong
     */
    public static void main(final String... arguments) throws Exception {
        try {
            generateFiles(arguments);
            successfulTermination();
        } catch (final OptionParsingException exception) {
            if (exception.couldNotParseOptions()) {
                handleFailedOptions(exception.getParser(), exception.failedOptions());
                abnormalTermination();
            } else if (exception.requiresHelp()) {
                printUsageText(exception.getParser());
                successfulTermination();
            } else if (exception.needsInformation()) {
                printInfoText();
                successfulTermination();
            }
        } catch (final Throwable throwable) {
            handleUnknownException(throwable);
            abnormalTermination();
        }
    }

    private static void generateFiles(final String... arguments) {
        DaggerYoSqlCLIComponent.builder()
                .jOptConfigurationModule(new JOptConfigurationModule(arguments))
                .build()
                .yosql()
                .generateFiles();
    }

    private static void handleFailedOptions(final OptionParser parser, final Collection<String> failedOptions)
            throws IOException {
        final IMessageConveyor messages = new MessageConveyor(Locale.ENGLISH);
        final Map<String, OptionSpec<?>> recognizedOptions = parser.recognizedOptions();
        final Collection<String> similarOptions = findSimilarOptions(failedOptions, recognizedOptions);
        ERRORS.println(messages.getMessage(PROBLEM_DURING_OPTION_PARSING, failedOptions, similarOptions));
        printHelpText(parser);
    }

    private static Collection<String> findSimilarOptions(
            final Collection<String> failedOptions,
            final Map<String, OptionSpec<?>> recognizedOptions) {
        final List<String> similars = new ArrayList<>(failedOptions.size());
        final String[] array = recognizedOptions.keySet().stream()
                .filter(string -> !string.contains("arguments")) //$NON-NLS-1$
                .toArray(n -> new String[n]);
        Arrays.sort(array);
        for (final String option : failedOptions) {
            final int index = Math.abs(Arrays.binarySearch(array, option)) - 1;
            similars.add(array[index]);
            if (index > 0) {
                similars.add(array[index - 1]);
            }
            if (index < array.length) {
                similars.add(array[index + 1]);
            }
        }
        return similars;
    }

    private static void printUsageText(final OptionParser parser) throws IOException {
        final IMessageConveyor messages = new MessageConveyor(Locale.ENGLISH);
        ERRORS.println(messages.getMessage(HELP_REQUIRED));
        printHelpText(parser);
    }

    private static void printHelpText(final OptionParser parser) throws IOException {
        parser.printHelpOn(ERRORS);
    }

    private static void printInfoText() {
        final IMessageConveyor messages = new MessageConveyor(Locale.ENGLISH);
        ERRORS.println(messages.getMessage(INFORMATION_NEEDED,
                BuildInfo.VERSION, BuildInfo.BUILD, BuildInfo.REVISION, BuildInfo.BUILD_BY, BuildInfo.BUILD_AT));
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
