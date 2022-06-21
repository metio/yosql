/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.cli;

import picocli.AutoComplete;
import picocli.CommandLine;

/**
 * Command line interface for YoSQL.
 */
@CommandLine.Command(
        name = "yosql",
        description = "write more SQL!",
        mixinStandardHelpOptions = true,
        showAtFileInUsageHelp = true,
        usageHelpAutoWidth = true,
        showDefaultValues = true,
        versionProvider = VersionProvider.class,
        subcommands = {
                Generate.class,
                AutoComplete.GenerateCompletion.class
        }
)
public final class YoSQL implements Runnable {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    /**
     * @param arguments The CLI arguments.
     */
    public static void main(final String... arguments) {
        commandLine().execute(arguments);
    }

    // visible for testing
    public static CommandLine commandLine() {
        final var commandLine = new CommandLine(new YoSQL());
        commandLine.setStopAtPositional(true);
        return commandLine;
    }

    /**
     * Reminds users that a subcommand must be used.
     */
    @Override
    public void run() {
        throw new CommandLine.ParameterException(spec.commandLine(), "ERROR: Missing required subcommand" + System.lineSeparator());
    }

}
