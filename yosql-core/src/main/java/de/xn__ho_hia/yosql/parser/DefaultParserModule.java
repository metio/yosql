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
import de.xn__ho_hia.yosql.dagger.LoggerModule.Parser;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;

/**
 * Provides the default parser.
 */
@Module
@SuppressWarnings("static-method")
public final class DefaultParserModule {

    @Provides
    SqlFileParser provideSqlFilePaser(
            final ExecutionErrors errors,
            final ExecutionConfiguration config,
            final SqlConfigurationFactory factory,
            final @Parser LocLogger logger) {
        return new DefaultSqlFileParser(errors, config, factory, logger);
    }

}
