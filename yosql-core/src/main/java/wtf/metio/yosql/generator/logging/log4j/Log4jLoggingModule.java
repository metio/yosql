/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.logging.log4j;

import dagger.Provides;
import dagger.Module;
import wtf.metio.yosql.generator.api.LoggingGenerator;
import wtf.metio.yosql.generator.helpers.TypicalFields;

/**
 * Dagger2 module for log4j based logging generators.
 */
@Module
public class Log4jLoggingModule {

    @Log4j
    @Provides
    LoggingGenerator provideLog4jLoggingGenerator(final TypicalFields fields) {
        return new Log4jLoggingGenerator(fields);
    }

}
