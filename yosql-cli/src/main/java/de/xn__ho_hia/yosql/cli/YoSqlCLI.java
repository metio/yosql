/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import joptsimple.OptionException;
import joptsimple.OptionParser;
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
        try {
            DaggerYoSqlCLIComponent.builder()
                    .jOptConfigurationModule(new JOptConfigurationModule(arguments))
                    .build()
                    .yosql()
                    .generateFiles();
        } catch (final OptionParsingException exception) {
            if (exception.couldNotParseOptions()) {
                handleParsingException(exception);
            }
            exception.getParser().printHelpOn(System.err);
        } catch (final Throwable throwable) {
            throwable.printStackTrace();
            System.exit(1);
        }
    }

    private static void handleParsingException(final OptionParsingException exception) {
        final OptionException cause = exception.getCause();
        final Collection<String> failedOptions = cause.options();
        final OptionParser parser = exception.getParser();
        final Map<String, OptionSpec<?>> recognizedOptions = parser.recognizedOptions();
        final Collection<String> similarOptions = findSimilarOptions(failedOptions, recognizedOptions);
        final IMessageConveyor messages = new MessageConveyor(Locale.ENGLISH);
        System.err.println(messages.getMessage(CliEvents.PROBLEM_DURING_OPTION_PARSING, failedOptions, similarOptions));
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

}
