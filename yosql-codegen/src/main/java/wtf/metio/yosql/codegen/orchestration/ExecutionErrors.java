/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.orchestration;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.exceptions.CodeGenerationException;
import wtf.metio.yosql.codegen.exceptions.SqlFileParsingException;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Captures execution errors/throwables/exceptions. Allows to print all exceptions after execution is finished instead
 * of failing as soon as the first exception is encountered.
 */
public final class ExecutionErrors {

    /**
     * Written from several threads at once: the file walk is parallel, and so is writing the types
     * out. A plain ArrayList loses entries under concurrent add, and a lost error is worse than a
     * noisy one — the stage boundary asks whether anything failed, and an answer of "no" lets the
     * build write files from input that did not parse.
     */
    private final List<Throwable> errors = new CopyOnWriteArrayList<>();
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
     * @return how many {@link Throwable}s were recorded. Distinct from {@link #hasErrors()} because
     *         "some errors arrived" and "every error arrived" are different questions, and the
     *         second one is the one that says whether this is safe to write to from several threads.
     */
    public int count() {
        return errors.size();
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
     */
    public void illegalArgument(final String message) {
        errors.add(new IllegalArgumentException(message));
    }

    /**
     * @param cause     The root cause of the illegal argument.
     * @param message   The message to include.
     */
    public void illegalArgument(final Exception cause, final String message) {
        errors.add(new IllegalArgumentException(message, cause));
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
