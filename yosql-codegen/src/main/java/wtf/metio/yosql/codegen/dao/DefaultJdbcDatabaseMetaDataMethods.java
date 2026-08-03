/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.models.configuration.GeneratedNames;
import com.palantir.javapoet.CodeBlock;

public class DefaultJdbcDatabaseMetaDataMethods implements JdbcMethods.JdbcDatabaseMetaDataMethods {


    public DefaultJdbcDatabaseMetaDataMethods() {
    }

    @Override
    public CodeBlock getDatabaseProductName() {
        return CodeBlock.builder()
                .add("$N.getDatabaseProductName()", GeneratedNames.DATABASE_META_DATA)
                .build();
    }

}
