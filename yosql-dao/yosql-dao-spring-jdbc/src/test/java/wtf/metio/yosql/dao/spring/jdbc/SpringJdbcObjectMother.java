/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.spring.jdbc;

import wtf.metio.yosql.codegen.api.BlockingMethodGenerator;
import wtf.metio.yosql.models.immutables.JavaConfiguration;
import wtf.metio.yosql.testing.codegen.Blocks;
import wtf.metio.yosql.testing.configs.ConverterConfigurations;
import wtf.metio.yosql.testing.logging.LoggingObjectMother;

/**
 * Object mother for Spring-JDBC related classes.
 *
 * @see <a href="https://martinfowler.com/bliki/ObjectMother.html">Martin Fowler's article on ObjectMothers</a>
 */
public class SpringJdbcObjectMother {

    public static SpringJdbcBlocks springJdbcBlocks() {
        return new DefaultSpringJdbcBlocks();
    }

    public static BlockingMethodGenerator blockingMethodGenerator(final JavaConfiguration java) {
        return new SpringJdbcBlockingMethodGenerator(
                Blocks.controlFlows(java),
                Blocks.methods(java),
                Blocks.parameters(java),
                LoggingObjectMother.loggingGenerator(),
                springJdbcBlocks(),
                ConverterConfigurations.withResultRowConverter());
    }

    private SpringJdbcObjectMother() {
        // factory class
    }

}
