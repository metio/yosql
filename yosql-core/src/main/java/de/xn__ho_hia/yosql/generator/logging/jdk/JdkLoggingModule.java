/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.logging.jdk;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.generator.api.LoggingGenerator;
import de.xn__ho_hia.yosql.generator.helpers.TypicalFields;

/**
 * Dagger2 module for java.util.logging based logging generators.
 */
@Module
@SuppressWarnings("static-method")
public class JdkLoggingModule {

    @JDK
    @Provides
    LoggingGenerator provideJdkLoggingGenerator(final TypicalFields fields) {
        return new JdkLoggingGenerator(fields);
    }

}
