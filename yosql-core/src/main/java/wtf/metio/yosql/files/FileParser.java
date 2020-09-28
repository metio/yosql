/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.files;

import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;

/**
 * High-level interface that handles parsing of SQL files.
 * 
 * @see SqlFileResolver
 * @see SqlFileParser
 */
public interface FileParser {

    /**
     * @return All files found in the configured input directory.
     */
    List<SqlStatement> parseFiles();

}
