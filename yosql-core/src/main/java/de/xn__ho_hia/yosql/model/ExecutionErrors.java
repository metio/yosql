/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Captures execution errors/throwables/exceptions.
 */
public final class ExecutionErrors {

    private final List<Throwable> errors = new ArrayList<>();

    /**
     * Creates a new execution error instance.
     */
    @Inject
    public ExecutionErrors() {
        // for dagger
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
     * @param throwable
     *            The {@link Throwable} to add.
     */
    public void add(final Throwable throwable) {
        errors.add(requireNonNull(throwable));
    }

    /**
     * @param message
     *            The message to include.
     */
    public void illegalState(final String message) {
        errors.add(new IllegalStateException(message));
    }

    /**
     * @param cause
     *            The root cause of the illegal state.
     * @param message
     *            The message to include.
     */
    public void illegalState(final Exception cause, final String message) {
        errors.add(new IllegalStateException(message, cause));
    }

    /**
     * @param message
     *            The message to include.
     * @param arguments
     *            The arguments to apply to the message.
     */
    public void illegalArgument(final String message, final Object... arguments) {
        errors.add(new IllegalArgumentException(String.format(message, arguments)));
    }

    /**
     * @param message
     *            The message to include.
     */
    public void codeGenerationException(final String message) {
        throwWith(new CodeGenerationException(message));
    }

    /**
     * @param message
     *            The message to include.
     */
    public void sqlFileParsingException(final String message) {
        throwWith(new SqlFileParsingException(message));
    }

    /**
     * @param exception
     *            The parent exception to use.
     * @throws T
     *             The given exception enriched with all captured suppressed exceptions so far.
     */
    public <T extends Exception> void throwWith(final T exception) throws T {
        errors.forEach(exception::addSuppressed);
        errors.clear();
        throw exception;
    }

}
