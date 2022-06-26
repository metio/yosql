/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.orchestration;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.exceptions.CodeGenerationException;
import wtf.metio.yosql.codegen.exceptions.SqlFileParsingException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Captures execution errors/throwables/exceptions. Allows to print all exceptions after execution is finished instead
 * of failing as soon as the first exception is encountered.
 */
public final class ExecutionErrors {

    private final List<Throwable> errors = new ArrayList<>();
    private final LocLogger logger;

    /**
     * Creates a new execution error instance.
     */
    public ExecutionErrors(final LocLogger logger) {
        this.logger = logger;
    }

    /**
     * Logs all previously recorded errors.
     */
    public void logErrors() {
        for (final var error : errors) {
            logger.error(error.getMessage());
            logger.debug(error.getMessage(), error);
            for (final var suppressed : error.getSuppressed()) {
                logger.error(suppressed.getMessage());
                logger.debug(suppressed.getMessage(), suppressed);
            }
        }
    }

    /**
     * @return <code>true</code> if any {@link Throwable} was added using {@link #add(Throwable)} before.
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * Adds another {@link Throwable} to the list.
     *
     * @param throwable The {@link Throwable} to add.
     */
    public void add(final Throwable throwable) {
        errors.add(requireNonNull(throwable));
    }

    /**
     * @param message The message to include.
     */
    public void illegalState(final String message) {
        errors.add(new IllegalStateException(message));
    }

    /**
     * @param cause   The root cause of the illegal state.
     * @param message The message to include.
     */
    public void illegalState(final Exception cause, final String message) {
        errors.add(new IllegalStateException(message, cause));
    }

    /**
     * @param message   The message to include.
     * @param arguments The arguments to apply to the message.
     */
    public void illegalArgument(final String message, final Object... arguments) {
        errors.add(new IllegalArgumentException(String.format(message, arguments)));
    }

    /**
     * @param cause     The root cause of the illegal argument.
     * @param message   The message to include.
     * @param arguments The arguments to apply to the message.
     */
    public void illegalArgument(final Exception cause, final String message, final Object... arguments) {
        errors.add(new IllegalArgumentException(String.format(message, arguments), cause));
    }

    /**
     * @param message The message to include.
     */
    public void codeGenerationException(final String message) {
        throwWith(new CodeGenerationException(message));
    }

    /**
     * @param message The message to include.
     */
    public void runtimeException(final String message) {
        throwWith(new RuntimeException(message));
    }

    /**
     * @param message The message to include.
     */
    public void sqlFileParsingException(final String message) {
        throwWith(new SqlFileParsingException(message));
    }

    /**
     * @param exception The parent exception to use.
     * @throws T The given exception enriched with all captured suppressed exceptions so far.
     */
    private <T extends Exception> void throwWith(final T exception) throws T {
        errors.forEach(exception::addSuppressed);
        errors.clear();
        throw exception;
    }

}
