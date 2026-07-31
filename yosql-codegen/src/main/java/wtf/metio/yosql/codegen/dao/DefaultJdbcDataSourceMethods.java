/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.models.immutables.NamesConfiguration;

public final class DefaultJdbcDataSourceMethods implements JdbcMethods.JdbcDataSourceMethods {

    private final NamesConfiguration names;

    public DefaultJdbcDataSourceMethods(final NamesConfiguration names) {
        this.names = names;
    }

    @Override
    public CodeBlock getConnection() {
        return CodeBlock.builder()
                .add("$N.getConnection()", names.dataSource())
                .build();
    }

}
