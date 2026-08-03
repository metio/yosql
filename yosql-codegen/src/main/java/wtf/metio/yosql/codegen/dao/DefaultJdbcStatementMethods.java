/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.models.configuration.GeneratedNames;
import com.palantir.javapoet.CodeBlock;

public final class DefaultJdbcStatementMethods implements JdbcMethods.JdbcStatementMethods {


    public DefaultJdbcStatementMethods() {
    }

    @Override
    public CodeBlock executeQuery() {
        return CodeBlock.builder()
                .add("$N.executeQuery()", GeneratedNames.STATEMENT)
                .build();
    }

    @Override
    public CodeBlock executeGivenQuery() {
        return CodeBlock.builder()
                .add("$N.executeQuery($N)", GeneratedNames.STATEMENT, GeneratedNames.QUERY)
                .build();
    }

    @Override
    public CodeBlock executeUpdate() {
        return CodeBlock.builder()
                .add("$N.executeUpdate()", GeneratedNames.STATEMENT)
                .build();
    }

    @Override
    public CodeBlock executeBatch() {
        return CodeBlock.builder()
                .add("$N.executeBatch()", GeneratedNames.STATEMENT)
                .build();
    }

    @Override
    public CodeBlock addBatch() {
        return CodeBlock.builder()
                .add("$N.addBatch()", GeneratedNames.STATEMENT)
                .build();
    }

    @Override
    public CodeBlock getResultSet() {
        return CodeBlock.builder()
                .add("$N.getResultSet()", GeneratedNames.STATEMENT)
                .build();
    }

    @Override
    public CodeBlock execute() {
        return CodeBlock.builder()
                .add("$N.execute()", GeneratedNames.STATEMENT)
                .build();
    }

}
