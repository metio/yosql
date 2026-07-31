/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * High-level interface that handles parsing of SQL files.
 *
 * @see SqlStatementParser
 */
@FunctionalInterface
public interface FileParser {

    /**
     * @return All files found in the configured input directory.
     */
    List<SqlStatement> parseFiles();

}
