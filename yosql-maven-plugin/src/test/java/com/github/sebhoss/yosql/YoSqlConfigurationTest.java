package com.github.sebhoss.yosql;

import static com.github.sebhoss.yosql.YoSqlConfiguration.defaultSqlFileSet;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.apache.maven.model.FileSet;
import org.junit.Test;

/**
 *
 */
public class YoSqlConfigurationTest {

    @Test
    public void shouldReturnNotNullFileSet() {
        // given
        FileSet fileSet;

        // when
        fileSet = defaultSqlFileSet();

        // then
        assertNotNull(fileSet);
    }

    @Test
    public void shouldReturnNotNullFileSetDirectory() {
        // given
        final FileSet fileSet = defaultSqlFileSet();

        // when
        final String directory = fileSet.getDirectory();

        // then
        assertNotNull(directory);
    }

    @Test
    public void shouldReturnFileSetPointingToRelativeDirectory() {
        // given
        final FileSet fileSet = defaultSqlFileSet();

        // when
        final String directory = fileSet.getDirectory();

        // then
        assertFalse(directory.startsWith("/"));
    }

    @Test
    public void shouldReturnFileSetPointingToYoSqlDirectory() {
        // given
        final FileSet fileSet = defaultSqlFileSet();

        // when
        final String directory = fileSet.getDirectory();

        // then
        assertEquals("src/main/yosql", directory);
    }

    @Test
    public void shouldReturnNotNullFileSetIncludes() {
        // given
        final FileSet fileSet = defaultSqlFileSet();

        // when
        final List<String> includes = fileSet.getIncludes();

        // then
        assertNotNull(includes);
    }

    @Test
    public void shouldReturnFileSetWithIncludes() {
        // given
        final FileSet fileSet = defaultSqlFileSet();

        // when
        final List<String> includes = fileSet.getIncludes();

        // then
        assertFalse(includes.isEmpty());
    }

    @Test
    public void shouldReturnFileSetIncludingSqlFiles() {
        // given
        final FileSet fileSet = defaultSqlFileSet();

        // when
        final List<String> includes = fileSet.getIncludes();

        // then
        assertThat(includes, hasItem("**/*.sql"));
    }

}
