/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.helpers;

import javax.lang.model.element.Modifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
class TypicalModifiersTest {

    @Test
    public void shouldPreparePublicMethod() {
        Assertions.assertArrayEquals(new Modifier[] { Modifier.PUBLIC }, TypicalModifiers.openClass());
    }

}
