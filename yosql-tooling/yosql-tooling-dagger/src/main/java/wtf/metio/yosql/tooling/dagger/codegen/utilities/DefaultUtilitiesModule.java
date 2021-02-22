/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.dagger.codegen.utilities;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.codegen.api.UtilitiesGenerator;
import wtf.metio.yosql.dao.jdbc.utilities.*;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Dagger module for the utilities generators.
 */
@Module
public class DefaultUtilitiesModule {

    @Provides
    UtilitiesGenerator provideUtilitiesGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final FlowStateGenerator flowStateGenerator,
            final ResultStateGenerator resultStateGenerator,
            final ToResultRowConverterGenerator toResultRowConverterGenerator,
            final ResultRowGenerator resultRowGenerator) {
        return new JdbcUtilitiesGenerator(
                flowStateGenerator,
                resultStateGenerator,
                toResultRowConverterGenerator,
                resultRowGenerator,
                runtimeConfiguration.jdbc());
    }

}
