/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.javapoet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class TypicalTypesTest {

    @Test
    void shouldDefineObjectType() {
        Assertions.assertAll(
                () -> Assertions.assertNotNull(TypicalTypes.OBJECT),
                () -> Assertions.assertEquals("java.lang.Object", TypicalTypes.OBJECT.canonicalName()));
    }

    @Test
    void shouldDefineStringType() {
        Assertions.assertAll(
                () -> Assertions.assertNotNull(TypicalTypes.STRING),
                () -> Assertions.assertEquals("java.lang.String", TypicalTypes.STRING.canonicalName()));
    }

    @Test
    void shouldDefineFlowableType() {
        Assertions.assertAll(
                () -> Assertions.assertNotNull(TypicalTypes.FLOWABLE),
                () -> Assertions.assertEquals("io.reactivex.Flowable", TypicalTypes.FLOWABLE.canonicalName()));
    }

}