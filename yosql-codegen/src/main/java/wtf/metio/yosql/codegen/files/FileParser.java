/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
