/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
