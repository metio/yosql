package de.xn__ho_hia.yosql.generator.helpers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings({ "nls", "static-method" })
final class TypicalNamesTest {

    @Test
    public void shouldDeclareDataSource() {
        Assertions.assertEquals("dataSource", TypicalNames.DATA_SOURCE);
    }

}
