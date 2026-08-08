/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.RepositoriesConfigurations;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;
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
    @DisplayName("a write holding a collection is not handed a batch it could never have")
    void batchNotDefaultedOntoACollection() {
        final var original = SqlConfiguration.builder()
                // executeBatch is NOT set: this is the project default arriving
                .setName("updateCancelledOrders")
                .addParameters(SqlConfigurations.collectionParameter())
                .build();

        final var adapted = configurer.batch(original);

        assertAll(
                () -> assertTrue(adapted.executeBatch().isPresent()),
                () -> assertFalse(adapted.executeBatch().get(),
                        "each value of a collection needs a placeholder of its own, so every "
                                + "execution of the batch would need a different query"));
    }

    @Test
    @DisplayName("a statement that asks for the batch itself still gets the error")
    void batchKeptWhenAskedForWithACollection() {
        final var original = SqlConfiguration.builder()
                .setName("updateCancelledOrders")
                .setExecuteBatch(true)
                .addParameters(SqlConfigurations.collectionParameter())
                .build();

        final var adapted = configurer.batch(original);

        assertTrue(adapted.executeBatch().orElseThrow(),
                "asking for something impossible is reported, not quietly granted");
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
    void keepApis() {
        final var original = SqlConfiguration.builder()
                .setExecuteBatch(false)
                .setExecuteOnce(false)
                .build();
        final var adapted = configurer.configureApis(original);
        assertAll(
                () -> assertEquals(original.executeBatch(), adapted.executeBatch()),
                () -> assertEquals(original.executeOnce(), adapted.executeOnce()));
    }

    @Test
    void changeApis() {
        final var original = SqlConfiguration.builder().build();
        final var adapted = configurer.configureApis(original);
        assertAll(
                () -> assertEquals(repositories.executeBatch(), adapted.executeBatch().get()),
                () -> assertEquals(repositories.executeOnce(), adapted.executeOnce().get()));
    }

}
