/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import org.immutables.value.Value;
import wtf.metio.yosql.model.options.StatementInRepositoryOptions;

/**
 * Configures the statement related options.
 */
@Value.Immutable
public interface StatementConfiguration { // TODO: merge into RepositoryConfiguration

    static ImmutableStatementConfiguration.Builder builder() {
        return ImmutableStatementConfiguration.builder();
    }

    /**
     * @return A statement configuration using default values.
     */
    static ImmutableStatementConfiguration usingDefaults() {
        return builder().build();
    }

    @Value.Default
    default StatementInRepositoryOptions embed() {
        return StatementInRepositoryOptions.INLINE_CONCAT;
    }

}
