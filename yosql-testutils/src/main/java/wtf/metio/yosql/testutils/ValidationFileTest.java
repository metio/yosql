/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.testutils;

import com.squareup.javapoet.*;
import com.squareup.javapoet.MethodSpec.Builder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Super class for tests that make use of a validation file.
 */
@ExtendWith(ValidationFileParameterResolver.class)
public class ValidationFileTest {

    protected static void validate(
            final CodeBlock codeBlock,
            final ValidationFile validationFile) {
        Assertions.assertEquals(validationFile.read(), codeBlock.toString());
    }

    protected static void validate(
            final TypeSpec typeSpec,
            final ValidationFile validationFile) {
        Assertions.assertEquals(validationFile.read(), typeSpec.toString());
    }

    protected static void validate(
            final FieldSpec fieldSpec,
            final ValidationFile validationFile) {
        Assertions.assertEquals(validationFile.read(), fieldSpec.toString());
    }

    protected static void validate(
            final Builder builder,
            final ValidationFile validationFile) {
        Assertions.assertEquals(validationFile.read(), builder.build().toString());
    }

    protected static void validate(
            final ParameterSpec parameter,
            final ValidationFile validationFile) {
        Assertions.assertEquals(validationFile.read(), parameter.toString());
    }

    protected static void validate(
            final AnnotationSpec annotation,
            final ValidationFile validationFile) {
        Assertions.assertEquals(validationFile.read(), annotation.toString());
    }

    protected static void validate(
            final Iterable<AnnotationSpec> annotations,
            final ValidationFile validationFile) {
        int counter = 0;
        for (final var annotation : annotations) {
            Assertions.assertEquals(validationFile.read(counter), annotation.toString());
            counter++;
        }
    }

}
