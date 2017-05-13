/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.testutils;

import com.squareup.javapoet.CodeBlock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Super class for tests that make use of a validation file.
 */
@ExtendWith(ValidationFileParameterResolver.class)
public class ValidationFileTest {

    protected static final void validate(
            final CodeBlock codeBlock,
            final ValidationFile validationFile) {
        Assertions.assertEquals(validationFile.read(), codeBlock.toString());
    }

}
