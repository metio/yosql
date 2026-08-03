/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DefaultJdbcDatabaseMetaDataMethods")
class DefaultJdbcDatabaseMetaDataMethodsTest {

    private DefaultJdbcDatabaseMetaDataMethods generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcDatabaseMetaDataMethods();
    }

    @Test
    void getDatabaseProductName() {
        Assertions.assertEquals("""
                databaseMetaData.getDatabaseProductName()""", generator.getDatabaseProductName().toString());
    }

}
