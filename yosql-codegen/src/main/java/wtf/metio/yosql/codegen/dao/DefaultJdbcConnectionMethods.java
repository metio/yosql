/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.models.immutables.NamesConfiguration;

public final class DefaultJdbcConnectionMethods implements JdbcMethods.JdbcConnectionMethods {

    private final NamesConfiguration names;

    public DefaultJdbcConnectionMethods(final NamesConfiguration names) {
        this.names = names;
    }

    @Override
    public CodeBlock createStatement() {
        return CodeBlock.builder()
                .add("$N.createStatement()", names.connection())
                .build();
    }

    @Override
    public CodeBlock prepareStatement() {
        return CodeBlock.builder()
                .add("$N.prepareStatement($N)", names.connection(), names.query())
                .build();
    }

    @Override
    public CodeBlock prepareCall() {
        return CodeBlock.builder()
                .add("$N.prepareCall($N)", names.connection(), names.query())
                .build();
    }

    @Override
    public CodeBlock getMetaData() {
        return CodeBlock.builder()
                .add("$N.getMetaData()", names.connection())
                .build();
    }

}
