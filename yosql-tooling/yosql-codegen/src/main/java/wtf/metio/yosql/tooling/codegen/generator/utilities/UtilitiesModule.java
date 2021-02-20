/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.codegen.generator.utilities;

import dagger.Provides;
import dagger.Module;
import wtf.metio.yosql.tooling.codegen.generator.api.UtilitiesGenerator;

/**
 * Dagger module for the utilities generators.
 */
@Module
public final class UtilitiesModule {

    @Provides
    UtilitiesGenerator provideUtilitiesGenerator(
            final FlowStateGenerator flowStateGenerator,
            final ResultStateGenerator resultStateGenerator,
            final ToResultRowConverterGenerator toResultRowConverterGenerator,
            final ResultRowGenerator resultRowGenerator) {
        return new DefaultUtilitiesGenerator(
            flowStateGenerator,
            resultStateGenerator,
            toResultRowConverterGenerator,
            resultRowGenerator);
    }

}
