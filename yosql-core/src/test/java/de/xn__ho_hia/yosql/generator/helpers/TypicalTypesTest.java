/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.helpers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings({ "nls", "static-method" })
final class TypicalTypesTest {

    @Test
    public void shouldDeclareJavaLangObject() {
        Assertions.assertEquals("java.lang.Object", TypicalTypes.OBJECT.toString());
    }

}
