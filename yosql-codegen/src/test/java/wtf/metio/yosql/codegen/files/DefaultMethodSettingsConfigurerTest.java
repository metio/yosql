/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.RepositoriesConfigurations;
import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.configuration.SqlStatementType;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DefaultMethodSettingsConfigurer")
class DefaultMethodSettingsConfigurerTest {

    private DefaultMethodSettingsConfigurer configurer;
    private RepositoriesConfiguration repositories;

    @BeforeEach
    void setUp() {
        repositories = RepositoriesConfigurations.defaults();
        configurer = new DefaultMethodSettingsConfigurer(repositories);
    }

    @Test
    void typeKeep() {
        final var original = SqlConfiguration.builder().setType(SqlStatementType.CALLING).build();
        final var adapted = configurer.type(original);
        assertEquals(original.type(), adapted.type());
    }

    @Test
    void typeChangeReading() {
        final var original = SqlConfiguration.builder()
                .setName(repositories.allowedReadPrefixes().get(0) + "Something")
                .build();
        final var adapted = configurer.type(original);
        assertTrue(adapted.type().isPresent());
        assertEquals(SqlStatementType.READING, adapted.type().get());
    }

    @Test
    void typeChangeWriting() {
        final var original = SqlConfiguration.builder()
                .setName(repositories.allowedWritePrefixes().get(0) + "Something")
                .build();
        final var adapted = configurer.type(original);
        assertTrue(adapted.type().isPresent());
        assertEquals(SqlStatementType.WRITING, adapted.type().get());
    }

    @Test
    void typeChangeCalling() {
        final var original = SqlConfiguration.builder()
                .setName(repositories.allowedCallPrefixes().get(0) + "Something")
                .build();
        final var adapted = configurer.type(original);
        assertTrue(adapted.type().isPresent());
        assertEquals(SqlStatementType.CALLING, adapted.type().get());
    }

    @Test
    void returningModeKeep() {
        final var original = SqlConfiguration.builder()
                .setReturningMode(ReturningMode.MULTIPLE)
                .build();
        final var adapted = DefaultMethodSettingsConfigurer.returningMode(original);
        assertEquals(original.returningMode(), adapted.returningMode());
    }

    @Test
    void returningModeChangeReading() {
        final var original = SqlConfiguration.builder()
                .setType(SqlStatementType.READING)
                .build();
        final var adapted = DefaultMethodSettingsConfigurer.returningMode(original);
        assertTrue(adapted.returningMode().isPresent());
        assertEquals(ReturningMode.MULTIPLE, adapted.returningMode().get());
    }

    @Test
    void returningModeChangeCalling() {
        final var original = SqlConfiguration.builder()
                .setType(SqlStatementType.CALLING)
                .build();
        final var adapted = DefaultMethodSettingsConfigurer.returningMode(original);
        assertTrue(adapted.returningMode().isPresent());
        assertEquals(ReturningMode.SINGLE, adapted.returningMode().get());
    }

    @Test
    void returningModeChangeWriting() {
        final var original = SqlConfiguration.builder()
                .setType(SqlStatementType.WRITING)
                .build();
        final var adapted = DefaultMethodSettingsConfigurer.returningMode(original);
        assertTrue(adapted.returningMode().isPresent());
        assertEquals(ReturningMode.NONE, adapted.returningMode().get());
    }

    @Test
    void returningModeRemainUnknown() {
        final var original = SqlConfiguration.builder()
                .build();
        final var adapted = DefaultMethodSettingsConfigurer.returningMode(original);
        assertTrue(adapted.returningMode().isEmpty());
    }

    @Test
    void catchAndRethrowKeep() {
        final var original = SqlConfiguration.builder()
                .setCatchAndRethrow(false)
                .build();
        final var adapted = configurer.catchAndRethrow(original);
        assertEquals(original.catchAndRethrow(), adapted.catchAndRethrow());
    }

    @Test
    void catchAndRethrowChange() {
        final var original = SqlConfiguration.builder()
                // .setCatchAndRethrow(false) // value is NOT set
                .build();
        final var adapted = configurer.catchAndRethrow(original);
        assertTrue(adapted.catchAndRethrow().isPresent());
        assertEquals(repositories.catchAndRethrow(), adapted.catchAndRethrow().get());
    }

    @Test
    void throwOnMultipleResultsForSingleKeep() {
        final var original = SqlConfiguration.builder()
                .setThrowOnMultipleResultsForSingle(true)
                .build();
        final var adapted = configurer.throwOnMultipleResultsForSingle(original);
        assertEquals(original.throwOnMultipleResultsForSingle(), adapted.throwOnMultipleResultsForSingle());
    }

    @Test
    void throwOnMultipleResultsForSingleChange() {
        final var original = SqlConfiguration.builder()
                // .setThrowOnMultipleResultsForSingle(true) // value is NOT set
                .build();
        final var adapted = configurer.throwOnMultipleResultsForSingle(original);
        assertTrue(adapted.throwOnMultipleResultsForSingle().isPresent());
        assertEquals(repositories.throwOnMultipleResultsForSingle(), adapted.throwOnMultipleResultsForSingle().get());
    }

    @Test
    void usePreparedStatementKeep() {
        final var original = SqlConfiguration.builder()
                .setUsePreparedStatement(false)
                .build();
        final var adapted = configurer.usePreparedStatement(original);
        assertEquals(original.usePreparedStatement(), adapted.usePreparedStatement());
    }

    @Test
    void usePreparedStatementChange() {
        final var original = SqlConfiguration.builder()
                // .setUsePreparedStatement(false) // value is NOT set
                .build();
        final var adapted = configurer.usePreparedStatement(original);
        assertTrue(adapted.usePreparedStatement().isPresent());
        assertEquals(repositories.usePreparedStatement(), adapted.usePreparedStatement().get());
    }

    @Test
    void writesReturnUpdateCountKeep() {
        final var original = SqlConfiguration.builder()
                .setWritesReturnUpdateCount(false)
                .build();
        final var adapted = configurer.usePreparedStatement(original);
        assertEquals(original.writesReturnUpdateCount(), adapted.writesReturnUpdateCount());
    }

    @Test
    void writesReturnUpdateCountChange() {
        final var original = SqlConfiguration.builder()
                // .setWritesReturnUpdateCount(false) // value is NOT set
                .build();
        final var adapted = configurer.writesReturnUpdateCount(original);
        assertTrue(adapted.writesReturnUpdateCount().isPresent());
        assertEquals(repositories.writesReturnUpdateCount(), adapted.writesReturnUpdateCount().get());
    }

    @Test
    void keepSettings() {
        final var original = SqlConfiguration.builder()
                .setType(SqlStatementType.CALLING)
                .setReturningMode(ReturningMode.MULTIPLE)
                .setCatchAndRethrow(false)
                .build();
        final var adapted = configurer.configureSettings(original);
        assertAll(
                () -> assertEquals(original.type(), adapted.type()),
                () -> assertEquals(original.returningMode(), adapted.returningMode()),
                () -> assertEquals(original.catchAndRethrow(), adapted.catchAndRethrow()));
    }

    @Test
    void changeSettings() {
        final var original = SqlConfiguration.builder()
                .setName(repositories.allowedCallPrefixes().get(0) + "Something")
                // .setCatchAndRethrow(false) // do NOT set value
                // .setThrowOnMultipleResultsForSingle(true) // value is NOT set
                // .setUsePreparedStatement(false) // value is NOT set
                .build();
        final var adapted = configurer.configureSettings(original);
        assertAll(
                () -> assertEquals(SqlStatementType.CALLING, adapted.type().get()),
                () -> assertEquals(ReturningMode.SINGLE, adapted.returningMode().get()),
                () -> assertEquals(repositories.catchAndRethrow(), adapted.catchAndRethrow().get()),
                () -> assertEquals(repositories.throwOnMultipleResultsForSingle(), adapted.throwOnMultipleResultsForSingle().get()),
                () -> assertEquals(repositories.usePreparedStatement(), adapted.usePreparedStatement().get()));
    }

}
