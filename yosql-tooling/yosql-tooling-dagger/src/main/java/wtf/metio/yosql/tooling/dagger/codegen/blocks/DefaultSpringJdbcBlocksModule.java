/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.dagger.codegen.blocks;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.codegen.api.Parameters;
import wtf.metio.yosql.dao.spring.jdbc.DefaultSpringJdbcBlocks;
import wtf.metio.yosql.dao.spring.jdbc.DefaultSpringJdbcParameters;
import wtf.metio.yosql.dao.spring.jdbc.SpringJdbcBlocks;
import wtf.metio.yosql.dao.spring.jdbc.SpringJdbcParameters;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

@Module
public class DefaultSpringJdbcBlocksModule {

    @Provides
    public SpringJdbcBlocks provideSpringJdbcBlocks() {
        return new DefaultSpringJdbcBlocks();
    }

    @Provides
    public SpringJdbcParameters provideSpringJdbcParameters(
            final RuntimeConfiguration runtimeConfiguration,
            final Parameters parameters) {
        return new DefaultSpringJdbcParameters(parameters, runtimeConfiguration.names());
    }

}
