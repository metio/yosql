/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.models.constants.sql.SqlType;
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
    void mutinyKeep() {
        final var original = SqlConfiguration.usingDefaults()
                .setGenerateMutinyApi(false)
                .build();
        final var adapted = configurer.mutiny(original);
        assertEquals(original.generateMutinyApi(), adapted.generateMutinyApi());
    }

    @Test
    void mutinyChangedToRepositoryDefault() {
        final var original = SqlConfiguration.usingDefaults()
                // .setGenerateMutinyApi(true) // value is NOT set
                .build();
        final var adapted = configurer.mutiny(original);
        assertTrue(adapted.generateMutinyApi().isPresent());
        assertEquals(repositories.generateMutinyApi(), adapted.generateMutinyApi().get());
    }

    @Test
    void reactorKeep() {
        final var original = SqlConfiguration.usingDefaults()
                .setGenerateReactorApi(false)
                .build();
        final var adapted = configurer.reactor(original);
        assertEquals(original.generateReactorApi(), adapted.generateReactorApi());
    }

    @Test
    void reactorChangedToRepositoryDefault() {
        final var original = SqlConfiguration.usingDefaults()
                // .setGenerateReactorApi(true) // value is NOT set
                .build();
        final var adapted = configurer.reactor(original);
        assertTrue(adapted.generateReactorApi().isPresent());
        assertEquals(repositories.generateReactorApi(), adapted.generateReactorApi().get());
    }

    @Test
    void rxJavaKeep() {
        final var original = SqlConfiguration.usingDefaults()
                .setGenerateRxJavaApi(false)
                .build();
        final var adapted = configurer.rxJava(original);
        assertEquals(original.generateRxJavaApi(), adapted.generateRxJavaApi());
    }

    @Test
    void rxJavaChangedToRepositoryDefault() {
        final var original = SqlConfiguration.usingDefaults()
                // .setGenerateRxJavaApi(true) // value is NOT set
                .build();
        final var adapted = configurer.rxJava(original);
        assertTrue(adapted.generateRxJavaApi().isPresent());
        assertEquals(repositories.generateRxJavaApi(), adapted.generateRxJavaApi().get());
    }

    @Test
    void streamEagerKeep() {
        final var original = SqlConfiguration.usingDefaults()
                .setGenerateStreamEagerApi(false)
                .build();
        final var adapted = configurer.streamEager(original);
        assertEquals(original.generateStreamEagerApi(), adapted.generateStreamEagerApi());
    }

    @Test
    void streamEagerForcedDisabledForWrites() {
        final var original = SqlConfiguration.usingDefaults()
                .setGenerateStreamEagerApi(true)
                .setType(SqlType.WRITING)
                .build();
        final var adapted = configurer.streamEager(original);
        assertTrue(adapted.generateStreamEagerApi().isPresent());
        assertFalse(adapted.generateStreamEagerApi().get());
    }

    @Test
    void streamEagerChangedToRepositoryDefault() {
        final var original = SqlConfiguration.usingDefaults()
                // .setGenerateStreamEagerApi(true) // value is NOT set
                .build();
        final var adapted = configurer.streamEager(original);
        assertTrue(adapted.generateStreamEagerApi().isPresent());
        assertEquals(repositories.generateStreamEagerApi(), adapted.generateStreamEagerApi().get());
    }

    @Test
    void streamLazyKeep() {
        final var original = SqlConfiguration.usingDefaults()
                .setGenerateStreamLazyApi(false)
                .build();
        final var adapted = configurer.streamLazy(original);
        assertEquals(original.generateStreamLazyApi(), adapted.generateStreamLazyApi());
    }

    @Test
    void streamLazyForcedDisabledForWrites() {
        final var original = SqlConfiguration.usingDefaults()
                .setGenerateStreamLazyApi(true)
                .setType(SqlType.WRITING)
                .build();
        final var adapted = configurer.streamLazy(original);
        assertTrue(adapted.generateStreamLazyApi().isPresent());
        assertFalse(adapted.generateStreamLazyApi().get());
    }

    @Test
    void streamLazyChangedToRepositoryDefault() {
        final var original = SqlConfiguration.usingDefaults()
                // .setGenerateStreamLazyApi(true) // value is NOT set
                .build();
        final var adapted = configurer.streamLazy(original);
        assertTrue(adapted.generateStreamLazyApi().isPresent());
        assertEquals(repositories.generateStreamLazyApi(), adapted.generateStreamLazyApi().get());
    }

    @Test
    void keepApis() {
        final var original = SqlConfiguration.usingDefaults()
                .setGenerateBatchApi(false)
                .setGenerateBlockingApi(false)
                .setGenerateMutinyApi(false)
                .setGenerateReactorApi(false)
                .setGenerateRxJavaApi(false)
                .setGenerateStreamEagerApi(false)
                .setGenerateStreamLazyApi(false)
                .build();
        final var adapted = configurer.configureApis(original);
        assertAll(
                () -> assertEquals(original.generateBatchApi(), adapted.generateBatchApi()),
                () -> assertEquals(original.generateBlockingApi(), adapted.generateBlockingApi()),
                () -> assertEquals(original.generateMutinyApi(), adapted.generateMutinyApi()),
                () -> assertEquals(original.generateReactorApi(), adapted.generateReactorApi()),
                () -> assertEquals(original.generateRxJavaApi(), adapted.generateRxJavaApi()),
                () -> assertEquals(original.generateStreamLazyApi(), adapted.generateStreamLazyApi()),
                () -> assertEquals(original.generateStreamLazyApi(), adapted.generateStreamLazyApi()));
    }

    @Test
    void changeApis() {
        final var original = SqlConfiguration.usingDefaults().build();
        final var adapted = configurer.configureApis(original);
        assertAll(
                () -> assertEquals(repositories.generateBatchApi(), adapted.generateBatchApi().get()),
                () -> assertEquals(repositories.generateBlockingApi(), adapted.generateBlockingApi().get()),
                () -> assertEquals(repositories.generateMutinyApi(), adapted.generateMutinyApi().get()),
                () -> assertEquals(repositories.generateReactorApi(), adapted.generateReactorApi().get()),
                () -> assertEquals(repositories.generateRxJavaApi(), adapted.generateRxJavaApi().get()),
                () -> assertEquals(repositories.generateStreamLazyApi(), adapted.generateStreamLazyApi().get()),
                () -> assertEquals(repositories.generateStreamLazyApi(), adapted.generateStreamLazyApi().get()));
    }

}