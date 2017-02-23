/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package com.github.sebhoss.yosql.plugin;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Captures plugin execution errors/throwables/exceptions.
 */
@Named
@Singleton
public class PluginErrors {

    private final List<Throwable> exceptions = new ArrayList<>();

    /**
     * @return <code>true</code> if any {@link Throwable} was added using
     *         {@link #add(Throwable)} before.
     */
    public boolean hasErrors() {
        return !exceptions.isEmpty();
    }

    /**
     * Adds another {@link Throwable} to the list.
     *
     * @param throwable
     *            The {@link Throwable} to add.
     */
    public void add(final Throwable throwable) {
        exceptions.add(requireNonNull(throwable));
    }

    /**
     * Announces a MOJO build error by throwing an instance of
     * {@link MojoExecutionException}. The thrown exception will contain all
     * previously {@link #add(Throwable) added} {@link Throwable}.
     *
     * @param message
     *            The build error message.
     * @param args
     *            The optional list of arguments for that message (used with
     *            {@link String#format(String, Object...)}).
     * @throws MojoExecutionException
     *             The new instance of a {@link MojoExecutionException} which
     *             contains all previously added exceptions.
     */
    public void buildError(final String message, final Object... args) throws MojoExecutionException {
        final MojoExecutionException exception = new MojoExecutionException(String.format(message, args));
        exceptions.forEach(exception::addSuppressed);
        throw exception;
    }

}
