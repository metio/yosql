/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.helpers;

import static de.xn__ho_hia.yosql.generator.helpers.TypicalMethods.publicMethod;

import org.junit.jupiter.api.Test;

import de.xn__ho_hia.yosql.testutils.ValidationFile;
import de.xn__ho_hia.yosql.testutils.ValidationFileTest;

@SuppressWarnings({ "nls", "static-method" })
class TypicalMethodsTest extends ValidationFileTest {

    @Test
    public void shouldPreparePublicMethod(final ValidationFile validationFile) {
        validate(publicMethod("test"), validationFile);
    }

}
