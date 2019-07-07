/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.cli.parser;

import java.io.IOException;
import java.io.StringWriter;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * Wrapper around JOpt {@link OptionParser} that collects all known options.
 */
public final class YoSqlOptionParser {

    private final OptionParser   parser;
    private final OptionSpec<?>[] options;

    /**
     * @param parser
     *            The parser to use.
     * @param options
     *            The option specs to use.
     */
    public YoSqlOptionParser(
            final OptionParser parser,
            final OptionSpec<?>... options) {
        this.parser = parser;
        this.options = options;
    }

    /**
     * @return The recognized options of this parser.
     */
    public String[] recognizedOptions() {
        return parser.recognizedOptions().keySet().stream()
                .filter(string -> !string.contains("arguments")) //$NON-NLS-1$
                .sorted()
                .toArray(size -> new String[size]);
    }

    /**
     * @return The help text as a {@link String} for further processing.
     * @throws IOException
     *             In case anything goes wrong during help text writing.
     */
    public String printHelp() throws IOException {
        final StringWriter writer = new StringWriter();
        parser.printHelpOn(writer);
        return writer.toString();
    }

    /**
     * @param arguments
     *            The arguments to parse.
     * @return The resulting option set.
     */
    public OptionSet parse(final String... arguments) {
        return parser.parse(arguments);
    }

}
