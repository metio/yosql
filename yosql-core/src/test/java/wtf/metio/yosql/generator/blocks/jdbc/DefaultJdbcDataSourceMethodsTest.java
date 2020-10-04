/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.tests.ObjectMother;

@DisplayName("DefaultJdbcDataSourceMethods")
class DefaultJdbcDataSourceMethodsTest extends ObjectMother {

    @Test
    void getConnection() {
        // given
        final var generator = new DefaultJdbcDataSourceMethods(yoSqlComponent().jdbcNames());

        // when
        final var connection = generator.getConnection();

        // then
        Assertions.assertEquals("""
                dataSource.getConnection()""", connection.toString());
    }

}