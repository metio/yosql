/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.models.immutables.NamesConfiguration;

public final class DefaultJdbcStatementMethods implements JdbcMethods.JdbcStatementMethods {

    private final NamesConfiguration names;

    public DefaultJdbcStatementMethods(final NamesConfiguration names) {
        this.names = names;
    }

    @Override
    public CodeBlock executeQuery() {
        return CodeBlock.builder()
                .add("$N.executeQuery()", names.statement())
                .build();
    }

    @Override
    public CodeBlock executeUpdate() {
        return CodeBlock.builder()
                .add("$N.executeUpdate()", names.statement())
                .build();
    }

    @Override
    public CodeBlock executeBatch() {
        return CodeBlock.builder()
                .add("$N.executeBatch()", names.statement())
                .build();
    }

    @Override
    public CodeBlock addBatch() {
        return CodeBlock.builder()
                .add("$N.addBatch()", names.statement())
                .build();
    }

    @Override
    public CodeBlock getResultSet() {
        return CodeBlock.builder()
                .add("$N.getResultSet()", names.statement())
                .build();
    }

    @Override
    public CodeBlock execute() {
        return CodeBlock.builder()
                .add("$N.execute()", names.statement())
                .build();
    }

}
