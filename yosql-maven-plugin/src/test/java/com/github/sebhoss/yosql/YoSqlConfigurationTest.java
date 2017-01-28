package com.github.sebhoss.yosql;

import static com.github.sebhoss.yosql.YoSqlConfiguration.defaultSqlFileSet;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import org.apache.maven.model.FileSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 *
 */
public class YoSqlConfigurationTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void shouldReturnNotNullFileSet() throws IOException {
        // given
        FileSet fileSet;

        // when
        fileSet = defaultSqlFileSet(folder.newFolder());

        // then
        assertNotNull(fileSet);
    }

    @Test
    public void shouldReturnNotNullFileSetDirectory() throws IOException {
        // given
        final FileSet fileSet = defaultSqlFileSet(folder.newFolder());

        // when
        final String directory = fileSet.getDirectory();

        // then
        assertNotNull(directory);
    }

    @Test
    public void shouldReturnFileSetPointingToRelativeDirectory() throws IOException {
        // given
        final FileSet fileSet = defaultSqlFileSet(folder.newFolder());

        // when
        final String directory = fileSet.getDirectory();

        // then
        assertFalse(directory.startsWith("/"));
    }

    @Test
    public void shouldReturnFileSetPointingToYoSqlDirectory() throws IOException {
        // given
        final FileSet fileSet = defaultSqlFileSet(folder.newFolder());

        // when
        final String directory = fileSet.getDirectory();

        // then
        assertEquals("./src/main/yosql", directory);
    }

    @Test
    public void shouldReturnNotNullFileSetIncludes() throws IOException {
        // given
        final FileSet fileSet = defaultSqlFileSet(folder.newFolder());

        // when
        final List<String> includes = fileSet.getIncludes();

        // then
        assertNotNull(includes);
    }

    @Test
    public void shouldReturnFileSetWithIncludes() throws IOException {
        // given
        final FileSet fileSet = defaultSqlFileSet(folder.newFolder());

        // when
        final List<String> includes = fileSet.getIncludes();

        // then
        assertFalse(includes.isEmpty());
    }

    @Test
    public void shouldReturnFileSetIncludingSqlFiles() throws IOException {
        // given
        final FileSet fileSet = defaultSqlFileSet(folder.newFolder());

        // when
        final List<String> includes = fileSet.getIncludes();

        // then
        assertThat(includes, hasItem("**/*.sql"));
    }

}
