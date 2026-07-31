/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.internals.jdk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FileNames")
class FileNamesTest {

    @Test
    void withoutExtension() {
        assertAll(
                () -> assertEquals("some", FileNames.withoutExtension(Paths.get("some.file"))),
                () -> assertEquals("some.file.with", FileNames.withoutExtension(Paths.get("some.file.with.dots"))));
    }

    @Test
    void toSlashes() {
        assertAll(
                () -> assertEquals("some/folder/file", FileNames.toSlashes(Paths.get("some/folder/file"))),
                () -> assertEquals("some/folder/file", FileNames.toSlashes(Paths.get("some\\folder\\file"))));
    }

}
