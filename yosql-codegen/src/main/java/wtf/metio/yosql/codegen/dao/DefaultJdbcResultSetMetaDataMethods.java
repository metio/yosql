/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import com.palantir.javapoet.CodeBlock;
import wtf.metio.yosql.models.immutables.NamesConfiguration;

public final class DefaultJdbcResultSetMetaDataMethods implements JdbcMethods.JdbcResultSetMetaDataMethods {

    private final NamesConfiguration names;

    public DefaultJdbcResultSetMetaDataMethods(final NamesConfiguration names) {
        this.names = names;
    }

    @Override
    public CodeBlock getColumnCount() {
        return CodeBlock.builder()
                .add("$N.getColumnCount()", names.resultSetMetaData())
                .build();
    }

}
