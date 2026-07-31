/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.NamesConfigurations;

@DisplayName("DefaultJdbcMetaDataMethods")
class DefaultJdbcResultSetMetaDataMethodsTest {

    @Test
    void getColumnCount() {
        // given
        final var generator = new DefaultJdbcResultSetMetaDataMethods(NamesConfigurations.defaults());

        // when
        final var columnCount = generator.getColumnCount();

        // then
        Assertions.assertEquals("""
                resultSetMetaData.getColumnCount()""", columnCount.toString());
    }

}
