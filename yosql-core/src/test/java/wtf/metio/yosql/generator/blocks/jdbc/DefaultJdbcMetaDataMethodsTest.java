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

import static wtf.metio.yosql.generator.blocks.jdbc.JdbcObjectMother.jdbcNames;

@DisplayName("DefaultJdbcMetaDataMethods")
class DefaultJdbcMetaDataMethodsTest {

    @Test
    void getColumnCount() {
        // given
        final var generator = new DefaultJdbcMetaDataMethods(jdbcNames());

        // when
        final var columnCount = generator.getColumnCount();

        // then
        Assertions.assertEquals("""
                metaData.getColumnCount()""", columnCount.toString());
    }

}