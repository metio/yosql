/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.internals.jdk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayName("FileNames")
class FileNamesTest {

    @Test
    void withoutExtension() {
        assertAll(
                () -> assertEquals("some", FileNames.withoutExtension(Paths.get("some.file"))),
                () -> assertEquals("some.file.with", FileNames.withoutExtension(Paths.get("some.file.with.dots"))));
    }

    @Test
    @DisplayName("a path's segments are joined with slashes, whatever the platform separates them with")
    void toSlashes() {
        assertAll(
                () -> assertEquals("some/folder/file", FileNames.toSlashes(Paths.get("some/folder/file"))),
                () -> assertEquals("some/folder/file", FileNames.toSlashes(Paths.get("some", "folder", "file"))));
    }

    @Test
    @DisplayName("a backslash inside one name is part of that name, not a separator")
    void toSlashesKeepsBackslashesInsideNames() {
        // Only reachable where a backslash is a legal filename character. Where it separates
        // segments, the path has segments and the assertion above covers it.
        assumeTrue(File.separatorChar == '/', "backslash is a separator on this platform");

        assertEquals("some\\folder\\file", FileNames.toSlashes(Paths.get("some\\folder\\file")));
    }

}
