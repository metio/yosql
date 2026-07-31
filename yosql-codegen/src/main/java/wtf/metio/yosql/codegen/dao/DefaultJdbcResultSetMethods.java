/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.models.immutables.NamesConfiguration;

public final class DefaultJdbcResultSetMethods implements JdbcMethods.JdbcResultSetMethods {

    private final NamesConfiguration names;

    public DefaultJdbcResultSetMethods(final NamesConfiguration names) {
        this.names = names;
    }

    @Override
    public CodeBlock getMetaData() {
        return CodeBlock.builder()
                .add("$N.getMetaData()", names.resultSet())
                .build();
    }

}
