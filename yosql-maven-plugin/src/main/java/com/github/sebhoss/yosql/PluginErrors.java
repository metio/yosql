/*
 * ysura GmbH ("COMPANY") CONFIDENTIAL
 * Unpublished Copyright (c) 2012-2015 ysura GmbH, All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have executed
 * Confidentiality and Non-disclosure agreements explicitly covering such access.
 *
 * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes
 * information that is confidential and/or proprietary, and is a trade secret, of COMPANY. ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE,
 * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS SOURCE CODE WITHOUT THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE
 * LAWS AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS
 * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 */
package com.github.sebhoss.yosql;

import org.apache.maven.plugin.MojoExecutionException;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Captures plugin execution errors/throwables/exceptions.
 */
@Named
@Singleton
public class PluginErrors {

    private final List<Throwable> exceptions = new ArrayList<>();

    /**
     * @return <code>true</code> if any {@link Throwable} was added using {@link #add(Throwable)} before.
     */
    public boolean hasErrors() {
        return !exceptions.isEmpty();
    }

    /**
     * Adds another {@link Throwable} to the list.
     *
     * @param throwable The {@link Throwable} to add.
     */
    public void add(final Throwable throwable) {
        exceptions.add(requireNonNull(throwable));
    }

    /**
     * Announces a MOJO build error by throwing an instance of {@link MojoExecutionException}. The thrown exception will contain all previously
     * {@link #add(Throwable) added} {@link Throwable}.
     *
     * @param message The build error message.
     * @param args The optional list of arguments for that message (used with {@link String#format(String, Object...)}).
     * @throws MojoExecutionException The new instance of a {@link MojoExecutionException} which contains all previously added exceptions.
     */
    public void buildError(final String message, final Object... args) throws MojoExecutionException {
        final MojoExecutionException exception = new MojoExecutionException(String.format(message, args));
        exceptions.forEach(exception::addSuppressed);
        throw exception;
    }

}
