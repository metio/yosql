/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.NamesConfigurations;

@DisplayName("DefaultJdbcResultSetMethods")
class DefaultJdbcResultSetMethodsTest {

    @Test
    void getMetaData() {
        // given
        final var generator = new DefaultJdbcResultSetMethods(NamesConfigurations.defaults());

        // when
        final var metaData = generator.getMetaData();

        // then
        Assertions.assertEquals("""
                resultSet.getMetaData()""", metaData.toString());
    }

}
