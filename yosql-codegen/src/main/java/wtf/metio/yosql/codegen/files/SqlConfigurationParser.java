/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.nio.file.Path;

/**
 * Parses strings into {@link SqlConfiguration}s.
 */
public interface SqlConfigurationParser {

    /**
     * @param source    the file the front matter was read from, for diagnostics — the parser's own
     *                  account of a syntax error describes a fragment, not a file anyone can open
     * @param rawConfig the front matter
     */
    SqlConfiguration parseConfig(Path source, String rawConfig);

}
