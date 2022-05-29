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
import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.configuration.SqlType;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.testing.configs.RepositoriesConfigurations;

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
        final var original = SqlConfiguration.usingDefaults().setType(SqlType.CALLING).build();
        final var adapted = configurer.type(original);
        assertEquals(original.type(), adapted.type());
    }

    @Test
    void typeChangeReading() {
        final var original = SqlConfiguration.usingDefaults()
                .setType(SqlType.UNKNOWN)
                .setName(repositories.allowedReadPrefixes().get(0) + "Something")
                .build();
        final var adapted = configurer.type(original);
        assertTrue(adapted.type().isPresent());
        assertEquals(SqlType.READING, adapted.type().get());
    }

    @Test
    void typeChangeWriting() {
        final var original = SqlConfiguration.usingDefaults()
                .setType(SqlType.UNKNOWN)
                .setName(repositories.allowedWritePrefixes().get(0) + "Something")
                .build();
        final var adapted = configurer.type(original);
        assertTrue(adapted.type().isPresent());
        assertEquals(SqlType.WRITING, adapted.type().get());
    }

    @Test
    void typeChangeCalling() {
        final var original = SqlConfiguration.usingDefaults()
                .setType(SqlType.UNKNOWN)
                .setName(repositories.allowedCallPrefixes().get(0) + "Something")
                .build();
        final var adapted = configurer.type(original);
        assertTrue(adapted.type().isPresent());
        assertEquals(SqlType.CALLING, adapted.type().get());
    }

    @Test
    void typeChangeUnknown() {
        final var original = SqlConfiguration.usingDefaults()
                .setType(SqlType.UNKNOWN)
                .setName("question" + "Something")
                .build();
        final var adapted = configurer.type(original);
        assertTrue(adapted.type().isPresent());
        assertEquals(SqlType.UNKNOWN, adapted.type().get());
    }

    @Test
    void returningModeKeep() {
        final var original = SqlConfiguration.usingDefaults()
                .setReturningMode(ReturningMode.MULTIPLE)
                .build();
        final var adapted = configurer.returningMode(original);
        assertEquals(original.returningMode(), adapted.returningMode());
    }

    @Test
    void returningModeChangeReading() {
        final var original = SqlConfiguration.usingDefaults()
                .setType(SqlType.READING)
                .build();
        final var adapted = configurer.returningMode(original);
        assertTrue(adapted.returningMode().isPresent());
        assertEquals(ReturningMode.MULTIPLE, adapted.returningMode().get());
    }

    @Test
    void returningModeChangeCalling() {
        final var original = SqlConfiguration.usingDefaults()
                .setType(SqlType.CALLING)
                .build();
        final var adapted = configurer.returningMode(original);
        assertTrue(adapted.returningMode().isPresent());
        assertEquals(ReturningMode.SINGLE, adapted.returningMode().get());
    }

    @Test
    void returningModeChangeWriting() {
        final var original = SqlConfiguration.usingDefaults()
                .setType(SqlType.WRITING)
                .build();
        final var adapted = configurer.returningMode(original);
        assertTrue(adapted.returningMode().isPresent());
        assertEquals(ReturningMode.NONE, adapted.returningMode().get());
    }

    @Test
    void returningModeChangeUnknown() {
        final var original = SqlConfiguration.usingDefaults()
                .setReturningMode(ReturningMode.NONE)
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.returningMode(original);
        assertTrue(adapted.returningMode().isPresent());
        assertEquals(ReturningMode.NONE, adapted.returningMode().get());
    }

    @Test
    void catchAndRethrowKeep() {
        final var original = SqlConfiguration.usingDefaults()
                .setCatchAndRethrow(false)
                .build();
        final var adapted = configurer.catchAndRethrow(original);
        assertEquals(original.catchAndRethrow(), adapted.catchAndRethrow());
    }

    @Test
    void catchAndRethrowChange() {
        final var original = SqlConfiguration.usingDefaults()
                // .setCatchAndRethrow(false) // value is NOT set
                .build();
        final var adapted = configurer.catchAndRethrow(original);
        assertTrue(adapted.catchAndRethrow().isPresent());
        assertEquals(repositories.catchAndRethrow(), adapted.catchAndRethrow().get());
    }

    @Test
    void injectConvertersKeep() {
        final var original = SqlConfiguration.usingDefaults()
                .setInjectConverters(true)
                .build();
        final var adapted = configurer.injectConverters(original);
        assertEquals(original.injectConverters(), adapted.injectConverters());
    }

    @Test
    void injectConvertersChange() {
        final var original = SqlConfiguration.usingDefaults()
                // .setInjectConverters(true) // value is NOT set
                .build();
        final var adapted = configurer.injectConverters(original);
        assertTrue(adapted.injectConverters().isPresent());
        assertEquals(repositories.injectConverters(), adapted.injectConverters().get());
    }

    @Test
    void throwOnMultipleResultsForSingleKeep() {
        final var original = SqlConfiguration.usingDefaults()
                .setThrowOnMultipleResultsForSingle(true)
                .build();
        final var adapted = configurer.throwOnMultipleResultsForSingle(original);
        assertEquals(original.throwOnMultipleResultsForSingle(), adapted.throwOnMultipleResultsForSingle());
    }

    @Test
    void throwOnMultipleResultsForSingleChange() {
        final var original = SqlConfiguration.usingDefaults()
                // .setThrowOnMultipleResultsForSingle(true) // value is NOT set
                .build();
        final var adapted = configurer.throwOnMultipleResultsForSingle(original);
        assertTrue(adapted.throwOnMultipleResultsForSingle().isPresent());
        assertEquals(repositories.throwOnMultipleResultsForSingle(), adapted.throwOnMultipleResultsForSingle().get());
    }

    @Test
    void usePreparedStatementKeep() {
        final var original = SqlConfiguration.usingDefaults()
                .setUsePreparedStatement(false)
                .build();
        final var adapted = configurer.usePreparedStatement(original);
        assertEquals(original.usePreparedStatement(), adapted.usePreparedStatement());
    }

    @Test
    void usePreparedStatementChange() {
        final var original = SqlConfiguration.usingDefaults()
                // .setUsePreparedStatement(false) // value is NOT set
                .build();
        final var adapted = configurer.usePreparedStatement(original);
        assertTrue(adapted.usePreparedStatement().isPresent());
        assertEquals(repositories.usePreparedStatement(), adapted.usePreparedStatement().get());
    }

    @Test
    void keepSettings() {
        final var original = SqlConfiguration.usingDefaults()
                .setType(SqlType.CALLING)
                .setReturningMode(ReturningMode.MULTIPLE)
                .setCatchAndRethrow(false)
                .setInjectConverters(true)
                .build();
        final var adapted = configurer.configureSettings(original);
        assertAll(
                () -> assertEquals(original.type(), adapted.type()),
                () -> assertEquals(original.returningMode(), adapted.returningMode()),
                () -> assertEquals(original.catchAndRethrow(), adapted.catchAndRethrow()),
                () -> assertEquals(original.injectConverters(), adapted.injectConverters()));
    }

    @Test
    void changeSettings() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(repositories.allowedCallPrefixes().get(0) + "Something")
                .setType(SqlType.UNKNOWN)
                // .setCatchAndRethrow(false) // do NOT set value
                // .setInjectConverters(true) // do NOT set value
                // .setThrowOnMultipleResultsForSingle(true) // value is NOT set
                // .setUsePreparedStatement(false) // value is NOT set
                .build();
        final var adapted = configurer.configureSettings(original);
        assertAll(
                () -> assertEquals(SqlType.CALLING, adapted.type().get()),
                () -> assertEquals(ReturningMode.SINGLE, adapted.returningMode().get()),
                () -> assertEquals(repositories.catchAndRethrow(), adapted.catchAndRethrow().get()),
                () -> assertEquals(repositories.injectConverters(), adapted.injectConverters().get()),
                () -> assertEquals(repositories.throwOnMultipleResultsForSingle(), adapted.throwOnMultipleResultsForSingle().get()),
                () -> assertEquals(repositories.usePreparedStatement(), adapted.usePreparedStatement().get()));
    }

}
