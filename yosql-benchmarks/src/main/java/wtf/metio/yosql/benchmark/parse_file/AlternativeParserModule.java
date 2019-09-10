/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark.parse_file;

import dagger.Module;
import dagger.Provides;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.files.DefaultSqlConfigurationFactory;
import wtf.metio.yosql.files.SqlConfigurationFactory;
import wtf.metio.yosql.files.SqlFileParser;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.errors.ExecutionErrors;
import wtf.metio.yosql.model.annotations.Parser;

/**
 * Provides the alternative parser.
 */
@Module
final class AlternativeParserModule {

    @Provides
    SqlFileParser provideSqlFilePaser(
            final ExecutionErrors errors,
            final RuntimeConfiguration runtime,
            final SqlConfigurationFactory factory) {
        return new AlternativeSqlFileParser(errors, factory, runtime);
    }

    @Provides
    SqlConfigurationFactory provideSqlConfigurationFactory(
            @Parser final LocLogger logger,
            final RuntimeConfiguration runtime,
            final ExecutionErrors errors) {
        return new DefaultSqlConfigurationFactory(logger, runtime, errors);
    }

}
