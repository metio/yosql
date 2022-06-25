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
                .setGenerateBatchApi(false)
                .build();
        final var adapted = configurer.batch(original);
        assertEquals(original.generateBatchApi(), adapted.generateBatchApi());
    }

    @Test
    void batchChangedToRepositoryDefault() {
        final var original = SqlConfiguration.builder()
                // .setGenerateBatchApi(true) // value is NOT set
                .build();
        final var adapted = configurer.batch(original);
        assertTrue(adapted.generateBatchApi().isPresent());
        assertEquals(repositories.generateBatchApi(), adapted.generateBatchApi().get());
    }

    @Test
    void batchAllowsReads() {
        final var original = SqlConfiguration.builder()
                .setGenerateBatchApi(true)
                .setType(SqlStatementType.READING)
                .build();
        final var adapted = configurer.batch(original);
        assertTrue(adapted.generateBatchApi().isPresent());
        assertTrue(adapted.generateBatchApi().get());
    }

    @Test
    void standardKeep() {
        final var original = SqlConfiguration.builder()
                .setGenerateStandardApi(false)
                .build();
        final var adapted = configurer.standard(original);
        assertEquals(original.generateStandardApi(), adapted.generateStandardApi());
    }

    @Test
    void standardChangedToRepositoryDefault() {
        final var original = SqlConfiguration.builder()
                // .setGenerateStandardApi(true) // value is NOT set
                .build();
        final var adapted = configurer.standard(original);
        assertTrue(adapted.generateStandardApi().isPresent());
        assertEquals(repositories.generateStandardApi(), adapted.generateStandardApi().get());
    }

    @Test
    void keepApis() {
        final var original = SqlConfiguration.builder()
                .setGenerateBatchApi(false)
                .setGenerateStandardApi(false)
                .build();
        final var adapted = configurer.configureApis(original);
        assertAll(
                () -> assertEquals(original.generateBatchApi(), adapted.generateBatchApi()),
                () -> assertEquals(original.generateStandardApi(), adapted.generateStandardApi()));
    }

    @Test
    void changeApis() {
        final var original = SqlConfiguration.builder().build();
        final var adapted = configurer.configureApis(original);
        assertAll(
                () -> assertEquals(repositories.generateBatchApi(), adapted.generateBatchApi().get()),
                () -> assertEquals(repositories.generateStandardApi(), adapted.generateStandardApi().get()));
    }

}
