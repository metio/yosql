/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.models.immutables.NamesConfiguration;

public class DefaultJdbcDatabaseMetaDataMethods implements JdbcMethods.JdbcDatabaseMetaDataMethods {

    private final NamesConfiguration names;

    public DefaultJdbcDatabaseMetaDataMethods(final NamesConfiguration names) {
        this.names = names;
    }

    @Override
    public CodeBlock getDatabaseProductName() {
        return CodeBlock.builder()
                .add("$N.getDatabaseProductName()", names.databaseMetaData())
                .build();
    }

}
