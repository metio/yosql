/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.generator.blocks.api.Names;
import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

final class DefaultJdbcConnectionMethods implements JdbcMethods.JdbcConnectionMethods {

    private final Names names;
    private final JdbcNames jdbcNames;

    DefaultJdbcConnectionMethods(final Names names, final JdbcNames jdbcNames) {
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

}
