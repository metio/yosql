/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.parser;

import org.slf4j.cal10n.LocLogger;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.dagger.LoggerModule;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;

/**
 * Provides the default parser.
 */
@Module
@SuppressWarnings("static-method")
public final class DefaultResolverModule {

    @Provides
    SqlFileResolver provideSqlFileResolver(
            final ParserPreconditions preconditions,
            final ExecutionErrors errors,
            final ExecutionConfiguration configuration,
            @LoggerModule.Reader final LocLogger logger) {
        return new PathBasedSqlFileResolver(preconditions, errors, configuration, logger);
    }

}
