/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
                Init.class,
                AutoComplete.GenerateCompletion.class
        }
)
public final class YoSQL implements Runnable {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    /**
     * Runs a command and exits with what it answered.
     *
     * <p>The exit code is the only thing a shell, a build or a pipeline can read. Dropping it made
     * every failure — a statement that cannot be generated, an option that does not exist — look
     * like a success to whatever ran YoSQL, which is how the CLI example stopped generating anything
     * without a single red build.</p>
     *
     * @param arguments The CLI arguments.
     */
    public static void main(final String... arguments) {
        System.exit(commandLine().execute(arguments));
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
