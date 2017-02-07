package com.github.sebhoss.yosql;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import com.github.sebhoss.yosql.plugin.PluginErrors;
import com.github.sebhoss.yosql.plugin.PluginPreconditions;

import java.io.File;
import java.io.IOException;

public class PluginPreconditionsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

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
