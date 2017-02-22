package com.github.sebhoss.yosql.generator.helpers;

import org.junit.Assert;
import org.junit.Ignore;
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
        Assert.assertEquals(givenType, guessedType.toString());
    }

    @Test
    public void shouldGuessArrayTypeOfPrimitive() {
        // given
        final String givenType = "int[]";

        // when
        final TypeName guessedType = TypicalTypes.guessTypeName(givenType);

        // then
        Assert.assertEquals(givenType, guessedType.toString());
    }

    @Test
    public void shouldGuessTypeOfGenericList() {
        // given
        final String givenType = "java.util.List<java.lang.Object>";

        // when
        final TypeName guessedType = TypicalTypes.guessTypeName(givenType);

        // then
        Assert.assertEquals(givenType, guessedType.toString());
    }

    @Test
    public void shouldGuessTypeOfGenericListOfGenericType() {
        // given
        final String givenType = "java.util.List<java.lang.Object<java.lang.Integer>>";

        // when
        final TypeName guessedType = TypicalTypes.guessTypeName(givenType);

        // then
        Assert.assertEquals(givenType, guessedType.toString());
    }

    @Test
    public void shouldGuessTypeOfGenericMap() {
        // given
        final String givenType = "java.util.Map<java.lang.String, java.lang.Object>";

        // when
        final TypeName guessedType = TypicalTypes.guessTypeName(givenType);

        // then
        Assert.assertEquals(givenType, guessedType.toString());
    }

    @Test
    @Ignore
    public void shouldGuessTypeOfGenericMapOfGenericMap() {
        // given
        final String givenType = "java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>";

        // when
        final TypeName guessedType = TypicalTypes.guessTypeName(givenType);

        // then
        Assert.assertEquals(givenType, guessedType.toString());
    }

}
