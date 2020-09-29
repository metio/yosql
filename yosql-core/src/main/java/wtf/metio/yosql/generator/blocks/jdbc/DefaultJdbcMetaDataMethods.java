/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

final class DefaultJdbcMetaDataMethods implements JdbcMethods.JdbcMetaDataMethods {

    private final JdbcNames jdbcNames;

    DefaultJdbcMetaDataMethods(final JdbcNames jdbcNames) {
        this.jdbcNames = jdbcNames;
    }

    @Override
    public CodeBlock getColumnCount() {
        return CodeBlock.builder()
                .add("$N.getColumnCount()", jdbcNames.metaData())
                .build();
    }

}
