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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for {@link PluginErrors}.
 */
public class PluginErrorsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private PluginErrors pluginErrors;

    @Before
    public void setUp() {
        pluginErrors = new PluginErrors();
    }

    /**
     * Ensures that a fresh instance has no errors attached yet.
     */
    @Test
    public void shouldNotHaveErrorsAfterCreation() {
        // given
        // nothing

        // when
        final boolean hasErrors = pluginErrors.hasErrors();

        // then
        Assert.assertFalse(hasErrors);
    }

    /**
     * Ensures that errors will be reported once they are added.
     */
    @Test
    public void shouldHaveErrorsAfterAddingOne() {
        // given
        final Throwable throwable = new Throwable();

        // when
        pluginErrors.add(throwable);

        // then
        Assert.assertTrue(pluginErrors.hasErrors());
    }

    /**
     * Ensures that no <code>NULL</code> {@link Throwable}s can be added. Those would otherwise cause another NullPointerException later down the line.
     */
    @Test
    public void shouldNotAcceptNullThrowables() {
        // given
        final Throwable throwable = null;

        // when
        thrown.expect(NullPointerException.class);

        // then
        pluginErrors.add(throwable);
    }

    /**
     * Ensures that an actual exception is thrown in case a build-error is encountered.
     */
    @Test
    public void shouldThrowMojoException() throws MojoExecutionException {
        // given
        final String message = "kaboom";

        // when
        thrown.expect(MojoExecutionException.class);
        thrown.expectMessage(message);

        // then
        pluginErrors.buildError(message);
    }

}
