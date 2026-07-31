/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
