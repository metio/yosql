/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.parser;

import dagger.Provides;
import dagger.Module;
import wtf.metio.yosql.model.ExecutionConfiguration;
import wtf.metio.yosql.model.ExecutionErrors;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.model.annotations.Reader;

/**
 * Provides the default file resolver.
 */
@Module
public final class DefaultResolverModule {

    @Provides
    SqlFileResolver provideSqlFileResolver(
            final ParserPreconditions preconditions,
            final ExecutionErrors errors,
            final ExecutionConfiguration configuration,
            @Reader final LocLogger logger) {
        return new PathBasedSqlFileResolver(preconditions, errors, configuration, logger);
    }

}
