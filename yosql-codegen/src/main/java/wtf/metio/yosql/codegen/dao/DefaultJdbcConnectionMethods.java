/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
