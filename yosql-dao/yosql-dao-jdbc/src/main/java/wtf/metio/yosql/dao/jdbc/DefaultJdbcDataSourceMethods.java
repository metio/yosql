/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

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
