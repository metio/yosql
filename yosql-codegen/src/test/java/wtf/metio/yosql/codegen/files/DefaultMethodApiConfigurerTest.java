/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.RepositoriesConfigurations;
import wtf.metio.yosql.models.configuration.SqlStatementType;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import static org.junit.jupiter.api.Assertions.*;

class DefaultMethodApiConfigurerTest {

    private DefaultMethodApiConfigurer configurer;
    private RepositoriesConfiguration repositories;

    @BeforeEach
    void setUp() {
        repositories = RepositoriesConfigurations.defaults();
        configurer = new DefaultMethodApiConfigurer(repositories);
    }

    @Test
    void batchKeep() {
        final var original = SqlConfiguration.builder()
                .setExecuteBatch(false)
                .build();
        final var adapted = configurer.batch(original);
        assertEquals(original.executeBatch(), adapted.executeBatch());
    }

    @Test
    void batchChangedToRepositoryDefault() {
        final var original = SqlConfiguration.builder()
                // .setExecuteBatch(true) // value is NOT set
                .build();
        final var adapted = configurer.batch(original);
        assertTrue(adapted.executeBatch().isPresent());
        assertEquals(repositories.executeBatch(), adapted.executeBatch().get());
    }

    @Test
    void batchAllowsReads() {
        final var original = SqlConfiguration.builder()
                .setExecuteBatch(true)
                .setType(SqlStatementType.READING)
                .build();
        final var adapted = configurer.batch(original);
        assertTrue(adapted.executeBatch().isPresent());
        assertTrue(adapted.executeBatch().get());
    }

    @Test
    void onceKeep() {
        final var original = SqlConfiguration.builder()
                .setExecuteOnce(false)
                .build();
        final var adapted = configurer.once(original);
        assertEquals(original.executeOnce(), adapted.executeOnce());
    }

    @Test
    void onceChangedToRepositoryDefault() {
        final var original = SqlConfiguration.builder()
                // .setExecuteOnce(true) // value is NOT set
                .build();
        final var adapted = configurer.once(original);
        assertTrue(adapted.executeOnce().isPresent());
        assertEquals(repositories.executeOnce(), adapted.executeOnce().get());
    }

    @Test
    void manyKeep() {
        final var original = SqlConfiguration.builder()
                .setExecuteMany(false)
                .build();
        final var adapted = configurer.many(original);
        assertEquals(original.executeMany(), adapted.executeMany());
    }

    @Test
    void manyChangedToRepositoryDefault() {
        final var original = SqlConfiguration.builder()
                // .setExecuteMany(true) // value is NOT set
                .build();
        final var adapted = configurer.many(original);
        assertTrue(adapted.executeMany().isPresent());
        assertEquals(repositories.executeMany(), adapted.executeMany().get());
    }

    @Test
    void keepApis() {
        final var original = SqlConfiguration.builder()
                .setExecuteBatch(false)
                .setExecuteOnce(false)
                .setExecuteMany(false)
                .build();
        final var adapted = configurer.configureApis(original);
        assertAll(
                () -> assertEquals(original.executeBatch(), adapted.executeBatch()),
                () -> assertEquals(original.executeOnce(), adapted.executeOnce()),
                () -> assertEquals(original.executeMany(), adapted.executeMany()));
    }

    @Test
    void changeApis() {
        final var original = SqlConfiguration.builder().build();
        final var adapted = configurer.configureApis(original);
        assertAll(
                () -> assertEquals(repositories.executeBatch(), adapted.executeBatch().get()),
                () -> assertEquals(repositories.executeOnce(), adapted.executeOnce().get()),
                () -> assertEquals(repositories.executeMany(), adapted.executeMany().get()));
    }

}
