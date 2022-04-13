/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.logging.tinylog;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testing.codegen.Blocks;

@DisplayName("TinylogLoggingGenerator")
class TinylogLoggingGeneratorTest {

    private TinylogLoggingGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new TinylogLoggingGenerator(Blocks.names());
    }

    @Test
    void isEnabled() {
        Assertions.assertTrue(generator.isEnabled());
    }

}