/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.models.configuration.GeneratedNames;
import com.palantir.javapoet.CodeBlock;

public final class DefaultJdbcDataSourceMethods implements JdbcMethods.JdbcDataSourceMethods {


    public DefaultJdbcDataSourceMethods() {
    }

    @Override
    public CodeBlock getConnection() {
        return CodeBlock.builder()
                .add("$N.getConnection()", GeneratedNames.DATA_SOURCE)
                .build();
    }

}
