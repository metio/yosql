/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.helpers;

import static wtf.metio.yosql.generator.helpers.TypicalFields.privateField;

import com.squareup.javapoet.TypeName;

import org.junit.jupiter.api.Test;

import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

class TypicalFieldsTest extends ValidationFileTest {

    @Test
    public void shouldDeclarePrivateField(final ValidationFile validationFile) {
        validate(privateField(TypeName.BOOLEAN, "test"), validationFile);
    }

}
