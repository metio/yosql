/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.codegen.orchestration.ExecutionErrors;
import wtf.metio.yosql.codegen.orchestration.OrchestrationObjectMother;
import wtf.metio.yosql.internals.testing.configs.RepositoriesConfigurations;
import wtf.metio.yosql.models.configuration.SqlStatementType;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DefaultMethodNameValidator")
class DefaultMethodNameValidatorTest {

    private DefaultMethodNameValidator validator;
    private ExecutionErrors errors;

    @BeforeEach
    void setUp() {
        errors = OrchestrationObjectMother.executionErrors();
        validator = new DefaultMethodNameValidator(RepositoriesConfigurations.validatingMethodNames(), errors, LoggingObjectMother.messages());
    }

    @Test
    @DisplayName("a statement whose kind is unknown fails the build instead of vanishing")
    void detectUnknownStatementType() {
        // Neither the name nor the front matter says what this is, so nothing would be generated
        // for it — a build that succeeds and produces nothing.
        final var configuration = SqlConfiguration.builder()
                .setName("countSomeData")
                .build();
        validator.validateNames(configuration, Paths.get("some.sql"));
        assertTrue(errors.hasErrors());
    }

    @Test
    @DisplayName("naming the kind outright is enough, whatever the name starts with")
    void declaredTypeIsEnough() {
        final var lenient = new DefaultMethodNameValidator(
                RepositoriesConfigurations.defaults(), errors, LoggingObjectMother.messages());
        final var configuration = SqlConfiguration.builder()
                .setType(SqlStatementType.READING)
                .setName("countSomeData")
                .build();
        lenient.validateNames(configuration, Paths.get("some.sql"));
        assertFalse(errors.hasErrors());
    }

    @Test
    void detectInvalidReadPrefix() {
        final var configuration = SqlConfiguration.builder()
                .setType(SqlStatementType.READING)
                .setName("updateSomeData")
                .build();
        final var source = Paths.get("some.sql");
        validator.validateNames(configuration, source);
        assertTrue(errors.hasErrors());
    }

    @Test
    void detectInvalidWritePrefix() {
        final var configuration = SqlConfiguration.builder()
                .setType(SqlStatementType.WRITING)
                .setName("findSomeData")
                .build();
        final var source = Paths.get("some.sql");
        validator.validateNames(configuration, source);
        assertTrue(errors.hasErrors());
    }

    @Test
    void detectInvalidCallPrefix() {
        final var configuration = SqlConfiguration.builder()
                .setType(SqlStatementType.CALLING)
                .setName("findSomeData")
                .build();
        final var source = Paths.get("some.sql");
        validator.validateNames(configuration, source);
        assertTrue(errors.hasErrors());
    }

    @Test
    void acceptValidReadPrefix() {
        final var configuration = SqlConfiguration.builder()
                .setType(SqlStatementType.READING)
                .setName("findSomeData")
                .build();
        final var source = Paths.get("some.sql");
        validator.validateNames(configuration, source);
        assertFalse(errors.hasErrors());
    }

    @Test
    void acceptValidWritePrefix() {
        final var configuration = SqlConfiguration.builder()
                .setType(SqlStatementType.WRITING)
                .setName("writeSomeData")
                .build();
        final var source = Paths.get("some.sql");
        validator.validateNames(configuration, source);
        assertFalse(errors.hasErrors());
    }

    @Test
    void acceptValidCallPrefix() {
        final var configuration = SqlConfiguration.builder()
                .setType(SqlStatementType.CALLING)
                .setName("callSomeProcedure")
                .build();
        final var source = Paths.get("some.sql");
        validator.validateNames(configuration, source);
        assertFalse(errors.hasErrors());
    }

}
