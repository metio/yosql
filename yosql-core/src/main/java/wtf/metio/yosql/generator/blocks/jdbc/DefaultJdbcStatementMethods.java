/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

final class DefaultJdbcStatementMethods implements JdbcMethods.JdbcStatementMethods {

    private final JdbcNamesConfiguration jdbcNames;

    DefaultJdbcStatementMethods(final JdbcNamesConfiguration jdbcNames) {
        this.jdbcNames = jdbcNames;
    }

    @Override
    public CodeBlock executeQuery() {
        return CodeBlock.builder()
                .add("$N.executeQuery()", jdbcNames.statement())
                .build();
    }

    @Override
    public CodeBlock executeUpdate() {
        return CodeBlock.builder()
                .add("$N.executeUpdate()", jdbcNames.statement())
                .build();
    }

    @Override
    public CodeBlock executeBatch() {
        return CodeBlock.builder()
                .add("$N.executeBatch()", jdbcNames.statement())
                .build();
    }

    @Override
    public CodeBlock addBatch() {
        return CodeBlock.builder()
                .add("$N.addBatch()", jdbcNames.statement())
                .build();
    }

}
