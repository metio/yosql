/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DefaultJdbcMetaDataMethods")
class DefaultJdbcResultSetMetaDataMethodsTest {

    @Test
    void getColumnCount() {
        // given
        final var generator = new DefaultJdbcResultSetMetaDataMethods();

        // when
        final var columnCount = generator.getColumnCount();

        // then
        Assertions.assertEquals("""
                resultSetMetaData.getColumnCount()""", columnCount.toString());
    }

}
