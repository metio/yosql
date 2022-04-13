/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.models.immutables.JdbcConfiguration;

public final class DefaultJdbcStatementMethods implements JdbcMethods.JdbcStatementMethods {

    private final JdbcConfiguration jdbcNames;

    public DefaultJdbcStatementMethods(final JdbcConfiguration jdbcNames) {
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

    @Override
    public CodeBlock getResultSet() {
        return CodeBlock.builder()
                .add("$N.getResultSet()", jdbcNames.statement())
                .build();
    }

    @Override
    public CodeBlock execute() {
        return CodeBlock.builder()
                .add("$N.execute()", jdbcNames.statement())
                .build();
    }

}
