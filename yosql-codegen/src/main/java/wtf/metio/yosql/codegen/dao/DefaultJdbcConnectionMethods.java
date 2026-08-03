/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.models.configuration.GeneratedNames;
import com.palantir.javapoet.CodeBlock;

public final class DefaultJdbcConnectionMethods implements JdbcMethods.JdbcConnectionMethods {


    public DefaultJdbcConnectionMethods() {
    }

    @Override
    public CodeBlock createStatement() {
        return CodeBlock.builder()
                .add("$N.createStatement()", GeneratedNames.CONNECTION)
                .build();
    }

    @Override
    public CodeBlock prepareStatement() {
        return CodeBlock.builder()
                .add("$N.prepareStatement($N)", GeneratedNames.CONNECTION, GeneratedNames.QUERY)
                .build();
    }

    @Override
    public CodeBlock prepareCall() {
        return CodeBlock.builder()
                .add("$N.prepareCall($N)", GeneratedNames.CONNECTION, GeneratedNames.QUERY)
                .build();
    }

    @Override
    public CodeBlock getMetaData() {
        return CodeBlock.builder()
                .add("$N.getMetaData()", GeneratedNames.CONNECTION)
                .build();
    }

}
