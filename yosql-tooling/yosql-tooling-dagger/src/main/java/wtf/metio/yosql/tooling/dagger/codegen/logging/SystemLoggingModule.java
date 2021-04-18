/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.dagger.codegen.logging;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.logging.system.SystemLoggingGenerator;

/**
 * Dagger module for {@link System.Logger} logging generators.
 */
@Module
public class SystemLoggingModule {

    @IntoSet
    @Provides
    public LoggingGenerator provideNoOpLoggingGenerator() {
        return new SystemLoggingGenerator();
    }

}
