/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DefaultMethodExceptionHandler")
class DefaultMethodExceptionHandlerTest {

    private DefaultMethodExceptionHandler generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultMethodExceptionHandler();
    }

    @Test
    void sqlException() {
        // given
        final var configuration = SqlConfiguration.builder().setCatchAndRethrow(false).build();

        // when
        final var exception = generator.thrownExceptions(configuration);

        // then
        assertFalse(((Collection<?>) exception).isEmpty());
    }

    @Test
    void noSqlException() {
        // given
        final var configuration = SqlConfiguration.builder().setCatchAndRethrow(true).build();

        // when
        final var exception = generator.thrownExceptions(configuration);

        // then
        assertTrue(((Collection<?>) exception).isEmpty());
    }

}
