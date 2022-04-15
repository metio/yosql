/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.codegen.api.Names;
import wtf.metio.yosql.models.immutables.JdbcConfiguration;

public final class DefaultJdbcConnectionMethods implements JdbcMethods.JdbcConnectionMethods {

    private final Names names;
    private final JdbcConfiguration jdbcNames;

    public DefaultJdbcConnectionMethods(final Names names, final JdbcConfiguration jdbcNames) {
        this.names = names;
        this.jdbcNames = jdbcNames;
    }

    @Override
    public CodeBlock prepareStatement() {
        return CodeBlock.builder()
                .add("$N.prepareStatement($N)", jdbcNames.connection(), names.query())
                .build();
    }

    @Override
    public CodeBlock prepareCallable() {
        return CodeBlock.builder()
                .add("$N.prepareCall($N)", jdbcNames.connection(), names.query())
                .build();
    }

    @Override
    public CodeBlock getMetaData() {
        return CodeBlock.builder()
                .add("$N.getMetaData()", jdbcNames.connection())
                .build();
    }

}
