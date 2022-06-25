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
