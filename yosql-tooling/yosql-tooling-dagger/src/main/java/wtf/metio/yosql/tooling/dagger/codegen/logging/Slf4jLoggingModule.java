/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.dagger.codegen.logging;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import wtf.metio.yosql.codegen.api.Fields;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.logging.slf4j.Slf4jLoggingGenerator;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Dagger module for slf4j based logging generators.
 */
@Module
public class Slf4jLoggingModule {

    @IntoSet
    @Provides
    public LoggingGenerator provideSlf4jLoggingGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final Fields fields) {
        return new Slf4jLoggingGenerator(runtimeConfiguration.names(), fields);
    }

}
