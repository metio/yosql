/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package com.github.sebhoss.yosql;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import com.github.sebhoss.yosql.plugin.PluginErrors;
import com.github.sebhoss.yosql.plugin.PluginPreconditions;

public class PluginPreconditionsTest {

    @Rule
    public ExpectedException    thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder      folder = new TemporaryFolder();

    private PluginPreconditions preconditions;

    @Before
    public void setUp() {
        preconditions = new PluginPreconditions(new PluginErrors());
    }

    @Test
    public void shouldRejectMissingDirectory() throws MojoExecutionException, IOException {
        // given
        final File directory = folder.newFolder("abc");
        directory.setReadOnly();

        // when
        thrown.expect(MojoExecutionException.class);

        // then
        preconditions.assertDirectoryIsWriteable(directory);
    }

}
