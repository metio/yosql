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
