/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.models.configuration.SqlType;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.testing.configs.RepositoriesConfigurations;

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
        final var original = SqlConfiguration.usingDefaults()
                .setGenerateBatchApi(false)
                .build();
        final var adapted = configurer.batch(original);
        assertEquals(original.generateBatchApi(), adapted.generateBatchApi());
    }

    @Test
    void batchChangedToRepositoryDefault() {
        final var original = SqlConfiguration.usingDefaults()
                // .setGenerateBatchApi(true) // value is NOT set
                .build();
        final var adapted = configurer.batch(original);
        assertTrue(adapted.generateBatchApi().isPresent());
        assertEquals(repositories.generateBatchApi(), adapted.generateBatchApi().get());
    }

    @Test
    void batchForcedDisabledForReads() {
        final var original = SqlConfiguration.usingDefaults()
                .setGenerateBatchApi(true)
                .setType(SqlType.READING)
                .build();
        final var adapted = configurer.batch(original);
        assertTrue(adapted.generateBatchApi().isPresent());
        assertFalse(adapted.generateBatchApi().get());
    }

    @Test
    void blockingKeep() {
        final var original = SqlConfiguration.usingDefaults()
                .setGenerateBlockingApi(false)
                .build();
        final var adapted = configurer.blocking(original);
        assertEquals(original.generateBlockingApi(), adapted.generateBlockingApi());
    }

    @Test
    void blockingChangedToRepositoryDefault() {
        final var original = SqlConfiguration.usingDefaults()
                // .setGenerateBlockingApi(true) // value is NOT set
                .build();
        final var adapted = configurer.blocking(original);
        assertTrue(adapted.generateBlockingApi().isPresent());
        assertEquals(repositories.generateBlockingApi(), adapted.generateBlockingApi().get());
    }

    @Test
    void keepApis() {
        final var original = SqlConfiguration.usingDefaults()
                .setGenerateBatchApi(false)
                .setGenerateBlockingApi(false)
                .build();
        final var adapted = configurer.configureApis(original);
        assertAll(
                () -> assertEquals(original.generateBatchApi(), adapted.generateBatchApi()),
                () -> assertEquals(original.generateBlockingApi(), adapted.generateBlockingApi()));
    }

    @Test
    void changeApis() {
        final var original = SqlConfiguration.usingDefaults().build();
        final var adapted = configurer.configureApis(original);
        assertAll(
                () -> assertEquals(repositories.generateBatchApi(), adapted.generateBatchApi().get()),
                () -> assertEquals(repositories.generateBlockingApi(), adapted.generateBlockingApi().get()));
    }

}
