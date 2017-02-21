package com.github.sebhoss.yosql.generator.helpers;

import org.junit.Assert;
import org.junit.Test;

import com.squareup.javapoet.TypeName;

public class TypicalTypesTest {

    @Test
    public void shouldGuessArrayType() {
        // given
        final String givenType = "java.lang.Object[]";

        // when
        final TypeName guessedType = TypicalTypes.guessTypeName(givenType);

        // then
        Assert.assertEquals("java.lang.Object[]", guessedType.toString());
    }

    @Test
    public void shouldGuessArrayTypeOfPrimitive() {
        // given
        final String givenType = "int[]";

        // when
        final TypeName guessedType = TypicalTypes.guessTypeName(givenType);

        // then
        Assert.assertEquals("int[]", guessedType.toString());
    }

}
