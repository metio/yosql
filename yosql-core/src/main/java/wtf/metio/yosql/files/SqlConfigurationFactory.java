/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.files;

import wtf.metio.yosql.model.sql.SqlConfiguration;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Factory for new {@link SqlConfiguration}s.
 */
public interface SqlConfigurationFactory {

    /**
     * @param source           The source file of the statement.
     * @param yaml             The YAML front matter of the statement.
     * @param parameterIndices The parameter indices (if any) of the statement.
     * @param statementInFile  The counter for statements with the same name in the same source file.
     * @return The resulting configuration.
     */
    SqlConfiguration createStatementConfiguration(
            Path source,
            String yaml,
            Map<String, List<Integer>> parameterIndices,
            int statementInFile);

}
