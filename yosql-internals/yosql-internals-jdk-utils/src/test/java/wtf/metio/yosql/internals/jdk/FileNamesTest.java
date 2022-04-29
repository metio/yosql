/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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

}
