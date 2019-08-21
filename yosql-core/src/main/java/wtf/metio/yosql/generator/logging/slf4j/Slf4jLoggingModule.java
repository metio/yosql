/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.logging.slf4j;

import dagger.Provides;
import dagger.Module;
import wtf.metio.yosql.generator.api.LoggingGenerator;
import wtf.metio.yosql.generator.helpers.TypicalFields;

/**
 * Dagger module for slf4j based logging generators.
 */
@Module
public class Slf4jLoggingModule {

    @Slf4j
    @Provides
    LoggingGenerator provideSlf4jLoggingGenerator(final TypicalFields fields) {
        return new Slf4jLoggingGenerator(fields);
    }

}
