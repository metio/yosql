/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.dagger.codegen.logging;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.codegen.api.Fields;
import wtf.metio.yosql.codegen.api.Names;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.logging.jul.JUL;
import wtf.metio.yosql.logging.jul.JulLoggingGenerator;

/**
 * Dagger module for java.util.logging based logging generators.
 */
@Module
public class JulLoggingModule {

    @JUL
    @Provides
    public LoggingGenerator provideJdkLoggingGenerator(
            final Names names,
            final Fields fields) {
        return new JulLoggingGenerator(names, fields);
    }

}
