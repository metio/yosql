/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.dagger.codegen.logging;

import dagger.Provides;
import dagger.Module;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.logging.noop.NoOp;
import wtf.metio.yosql.logging.noop.NoOpLoggingGenerator;

/**
 * Dagger module for no-op logging generators.
 */
@Module
public class NoOpLoggingModule {

    @NoOp
    @Provides
    public LoggingGenerator provideNoOpLoggingGenerator() {
        return new NoOpLoggingGenerator();
    }

}
