/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import org.apache.maven.plugins.annotations.Parameter;
import wtf.metio.yosql.model.configuration.StatementConfiguration;
import wtf.metio.yosql.model.options.StatementInRepositoryOptions;

/**
 * Configures how SQL statements are handled.
 */
public class Statements {

    /**
     * Controls whether the SQL statements should be inlined in the generated repositories or loaded at options
     * (default: <strong>INLINE_CONCAT</strong>). Other possible value is <strong>INLINE_TEXT_BLOCK</strong>.
     */
    @Parameter(defaultValue = "INLINE_CONCAT")
    private String repositorySqlStatements;

    StatementConfiguration asConfiguration() {
        return StatementConfiguration.builder()
                .setEmbed(StatementInRepositoryOptions.valueOf(repositorySqlStatements))
                .build();
    }

}
