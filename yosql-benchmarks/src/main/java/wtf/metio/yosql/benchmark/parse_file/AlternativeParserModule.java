/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark.parse_file;

import org.slf4j.cal10n.LocLogger;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.model.annotations.Parser;
import wtf.metio.yosql.model.ExecutionConfiguration;
import wtf.metio.yosql.model.ExecutionErrors;
import wtf.metio.yosql.parser.SqlConfigurationFactory;
import wtf.metio.yosql.parser.SqlFileParser;

/**
 * Provides the alternative parser.
 */
@Module
final class AlternativeParserModule {

    @Provides
    SqlFileParser provideSqlFilePaser(
            final ExecutionErrors errors,
            final ExecutionConfiguration config,
            final SqlConfigurationFactory factory) {
        return new AlternativeSqlFileParser(errors, config, factory);
    }

    @Provides
    SqlConfigurationFactory provideSqlConfigurationFactory(
            final ExecutionErrors errors,
            final ExecutionConfiguration config,
            final @Parser LocLogger logger) {
        return new SqlConfigurationFactory(errors, config, logger);
    }

}
