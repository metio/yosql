/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.logging.jdk;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.generator.api.LoggingGenerator;
import wtf.metio.yosql.generator.blocks.generic.Fields;
import wtf.metio.yosql.generator.blocks.generic.Names;

/**
 * Dagger module for java.util.logging based logging generators.
 */
@Module
public final class JdkLoggingModule {

    @JDK
    @Provides
    LoggingGenerator provideJdkLoggingGenerator(
            final Names names,
            final Fields fields) {
        return new JdkLoggingGenerator(names, fields);
    }

}
