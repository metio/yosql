/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.parser;

import dagger.Provides;
import dagger.Module;
import wtf.metio.yosql.model.annotations.Parser;
import wtf.metio.yosql.model.ExecutionConfiguration;
import wtf.metio.yosql.model.ExecutionErrors;
import org.slf4j.cal10n.LocLogger;

/**
 * Provides the default parser.
 */
@Module
public final class DefaultParserModule {

    @Provides
    SqlFileParser provideSqlFilePaser(
            final ExecutionErrors errors,
            final ExecutionConfiguration config,
            final SqlConfigurationFactory factory,
            final @Parser LocLogger logger) {
        return new DefaultSqlFileParser(errors, config, factory, logger);
    }

    @Provides
    SqlConfigurationFactory provideSqlConfigurationFactory(
            final ExecutionErrors errors,
            final ExecutionConfiguration config,
            final @Parser LocLogger logger) {
        return new SqlConfigurationFactory(errors, config, logger);
    }

}
