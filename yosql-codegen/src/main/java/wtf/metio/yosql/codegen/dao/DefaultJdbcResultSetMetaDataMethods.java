/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.models.configuration.GeneratedNames;
import com.palantir.javapoet.CodeBlock;

public final class DefaultJdbcResultSetMetaDataMethods implements JdbcMethods.JdbcResultSetMetaDataMethods {


    public DefaultJdbcResultSetMetaDataMethods() {
    }

    @Override
    public CodeBlock getColumnCount() {
        return CodeBlock.builder()
                .add("$N.getColumnCount()", GeneratedNames.RESULT_SET_META_DATA)
                .build();
    }

}
