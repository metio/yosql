/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.models.configuration.GeneratedNames;
import com.palantir.javapoet.CodeBlock;

public final class DefaultJdbcResultSetMethods implements JdbcMethods.JdbcResultSetMethods {


    public DefaultJdbcResultSetMethods() {
    }

    @Override
    public CodeBlock getMetaData() {
        return CodeBlock.builder()
                .add("$N.getMetaData()", GeneratedNames.RESULT_SET)
                .build();
    }

}
