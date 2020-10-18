/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.model.sql.SqlConfiguration;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DefaultJdbcTransformer")
class DefaultJdbcTransformerTest {

    private DefaultJdbcTransformer generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcTransformer();
    }

    @Test
    void sqlException() {
        // given
        final var configuration = new SqlConfiguration();

        // when
        final var exception = generator.sqlException(configuration);

        // then
        assertFalse(((Collection<?>) exception).isEmpty());
    }

    @Test
    void noSqlException() {
        // given
        final var configuration = new SqlConfiguration();
        configuration.setMethodCatchAndRethrow(true);

        // when
        final var exception = generator.sqlException(configuration);

        // then
        assertTrue(((Collection<?>) exception).isEmpty());
    }

}