/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.orchestration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.exceptions.CodeGenerationException;
import wtf.metio.yosql.codegen.exceptions.SqlFileParsingException;
import wtf.metio.yosql.codegen.logging.LoggingObjectMother;

class ExecutionErrorsTest {

    private ExecutionErrors errors;

    @BeforeEach
    void setUp() {
        errors = new ExecutionErrors(LoggingObjectMother.logger());
    }

    @Test
    void emptyByDefault() {
        Assertions.assertFalse(errors.hasErrors());
    }

    @Test
    void addException() {
        errors.add(new Exception());

        Assertions.assertTrue(errors.hasErrors());
    }

    @Test
    void addRuntimeException() {
        errors.add(new RuntimeException());

        Assertions.assertTrue(errors.hasErrors());
    }

    @Test
    void illegalState() {
        errors.illegalState("some wrong state");

        Assertions.assertTrue(errors.hasErrors());
    }

    @Test
    void illegalStateWithException() {
        errors.illegalState(new Exception(), "some wrong state");

        Assertions.assertTrue(errors.hasErrors());
    }

    @Test
    void illegalArgument() {
        errors.illegalArgument("some wrong argument");

        Assertions.assertTrue(errors.hasErrors());
    }

    @Test
    void illegalArgumentWithException() {
        errors.illegalArgument(new Exception(), "some wrong argument");

        Assertions.assertTrue(errors.hasErrors());
    }

    @Test
    void codeGenerationException() {
        Assertions.assertThrows(CodeGenerationException.class,
                () -> errors.codeGenerationException("some codegen problem"));
    }

    @Test
    void runtimeException() {
        Assertions.assertThrows(RuntimeException.class,
                () -> errors.runtimeException("some runtime problem"));
    }

    @Test
    void sqlFileParsingException() {
        Assertions.assertThrows(SqlFileParsingException.class,
                () -> errors.sqlFileParsingException("some SQL parsing problem"));
    }

}
