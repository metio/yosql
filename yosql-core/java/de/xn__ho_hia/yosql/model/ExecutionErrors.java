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

/**
 * Captures execution errors/throwables/exceptions.
 */
public final class ExecutionErrors {

    private final List<Throwable> errors = new ArrayList<>();

    /**
     * @return <code>true</code> if any {@link Throwable} was added using
     *         {@link #add(Throwable)} before.
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

	public void illegalState(final String message, final Object... arguments) {
		errors.add(new IllegalStateException(String.format(message, arguments)));
	}

	public <T extends Exception> void throwWith(final T exception) throws T {
		errors.forEach(exception::addSuppressed);
		throw exception;
	}

}
