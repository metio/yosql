/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.internals.testing.configs.ConverterConfigurations;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;
import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.configuration.SqlStatementType;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DefaultReturnTypes")
class DefaultReturnTypesTest {

    private DefaultReturnTypes returnTypes;

    @BeforeEach
    void setUp() {
        returnTypes = new DefaultReturnTypes(ConverterConfigurations.withConverters());
    }

    @Test
    void noneResultType() {
        assertEquals(TypeName.VOID, returnTypes.noneResultType(SqlConfigurations.sqlConfiguration()));
    }

    @Test
    void noneResultTypeWritingWithUpdateCount() {
        assertEquals(TypeName.INT, returnTypes.noneResultType(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withType(SqlStatementType.WRITING)
                .withWritesReturnUpdateCount(true)));
    }

    @Test
    void noneResultTypeWritingWithoutUpdateCount() {
        assertEquals(TypeName.VOID, returnTypes.noneResultType(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withType(SqlStatementType.WRITING)
                .withWritesReturnUpdateCount(false)));
    }

    @Test
    void noneResultTypeReadingWithUpdateCount() {
        assertEquals(TypeName.VOID, returnTypes.noneResultType(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withType(SqlStatementType.READING)
                .withWritesReturnUpdateCount(true)));
    }

    @Test
    void noneResultTypeCallingWithUpdateCount() {
        assertEquals(TypeName.VOID, returnTypes.noneResultType(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withType(SqlStatementType.CALLING)
                .withWritesReturnUpdateCount(true)));
    }

    @Test
    void singleResultType() {
        assertEquals(TypicalTypes.optionalOf(TypicalTypes.MAP_OF_STRING_AND_OBJECTS),
                returnTypes.singleResultType(SqlConfigurations.sqlConfiguration()));
    }

    @Test
    void multiResultType() {
        assertEquals(TypicalTypes.listOf(TypicalTypes.MAP_OF_STRING_AND_OBJECTS),
                returnTypes.multiResultType(SqlConfigurations.sqlConfiguration()));
    }

    @Test
    void cursorResultType() {
        assertEquals(TypicalTypes.streamOf(TypicalTypes.MAP_OF_STRING_AND_OBJECTS),
                returnTypes.cursorResultType(SqlConfigurations.sqlConfiguration()));
    }

    @Test
    void resultTypeWithoutReturningMode() {
        assertTrue(returnTypes.resultType(SqlConfiguration.builder().build()).isEmpty());
    }

    @Test
    void resultTypeWithReturningModeNone() {
        assertFalse(returnTypes.resultType(SqlConfiguration.builder()
                .setReturningMode(ReturningMode.NONE).build()).isEmpty());
    }

    @Test
    void resultTypeWithReturningModeSingle() {
        assertFalse(returnTypes.resultType(SqlConfiguration.builder()
                .setReturningMode(ReturningMode.SINGLE).build()).isEmpty());
    }

    @Test
    void resultTypeWithReturningModeMultiple() {
        assertFalse(returnTypes.resultType(SqlConfiguration.builder()
                .setReturningMode(ReturningMode.MULTIPLE).build()).isEmpty());
    }

    @Test
    void resultTypeWithReturningModeCursor() {
        assertFalse(returnTypes.resultType(SqlConfiguration.builder()
                .setReturningMode(ReturningMode.CURSOR).build()).isEmpty());
    }

}
