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

@DisplayName("DefaultJdbcMetaDataMethods")
class DefaultJdbcMetaDataMethodsTest extends ObjectMother {

    @Test
    void getColumnCount() {
        // given
        final var generator = new DefaultJdbcMetaDataMethods(yoSqlComponent().jdbcNames());

        // when
        final var columnCount = generator.getColumnCount();

        // then
        Assertions.assertEquals("""
                metaData.getColumnCount()""", columnCount.toString());
    }

}