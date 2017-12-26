/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.utilities;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.generator.api.UtilitiesGenerator;

/**
 * Dagger2 module for the default utilities generators.
 */
@Module
@SuppressWarnings("static-method")
public final class DefaultUtilitiesModule {

    @Provides
    UtilitiesGenerator provideUtilitiesGenerator(
            final FlowStateGenerator flowStateGenerator,
            final ResultStateGenerator resultStateGenerator,
            final ToResultRowConverterGenerator toResultRowConverterGenerator,
            final ResultRowGenerator resultRowGenerator) {
        return new DefaultUtilitiesGenerator(flowStateGenerator, resultStateGenerator, toResultRowConverterGenerator,
                resultRowGenerator);
    }

}
