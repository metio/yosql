/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.dagger.logging;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.codegen.logging.ThatsInteresingLoggingGenerator;

import javax.inject.Singleton;

/**
 * Dagger module for thats-interesting logging generators.
 */
@Module
public class ThatsInterstingLoggingModule {

    @IntoSet
    @Provides
    @Singleton
    LoggingGenerator provideNoOpLoggingGenerator() {
        return new ThatsInteresingLoggingGenerator();
    }

}
