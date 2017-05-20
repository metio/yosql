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
