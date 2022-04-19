/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.api;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of a {@link MethodsGenerator}.
 */
public abstract class AbstractMethodsGenerator implements MethodsGenerator {

    @Override
    public final Iterable<MethodSpec> asMethods(final List<SqlStatement> statements) {
        final var methods = new ArrayList<MethodSpec>(statements.size());

        methods.add(constructor(statements));
        statements.stream()
                .filter(SqlStatement::shouldGenerateBlockingReadAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statementsInRepository) -> methods
                        .add(blockingReadMethod(SqlConfiguration.fromStatements(statementsInRepository), statementsInRepository)));
        statements.stream()
                .filter(SqlStatement::shouldGenerateBlockingWriteAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statementsInRepository) -> methods
                        .add(blockingWriteMethod(SqlConfiguration.fromStatements(statementsInRepository), statementsInRepository)));
        statements.stream()
                .filter(SqlStatement::shouldGenerateBlockingCallAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statementsInRepository) -> methods
                        .add(blockingCallMethod(SqlConfiguration.fromStatements(statementsInRepository), statementsInRepository)));
        statements.stream()
                .filter(SqlStatement::shouldGenerateBatchAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statementsInRepository) -> methods
                        .add(batchWriteMethod(SqlConfiguration.fromStatements(statementsInRepository), statementsInRepository)));
        statements.stream()
                .filter(SqlStatement::shouldGenerateStreamEagerAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statementsInRepository) -> methods
                        .add(streamEagerReadMethod(SqlConfiguration.fromStatements(statementsInRepository), statementsInRepository)));
        statements.stream()
                .filter(SqlStatement::shouldGenerateStreamLazyAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statementsInRepository) -> methods
                        .add(streamLazyReadMethod(SqlConfiguration.fromStatements(statementsInRepository), statementsInRepository)));
        statements.stream()
                .filter(SqlStatement::shouldGenerateRxJavaAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statementsInRepository) -> methods
                        .add(rxJavaReadMethod(SqlConfiguration.fromStatements(statementsInRepository), statementsInRepository)));

        return methods;
    }

    public abstract MethodSpec constructor(List<SqlStatement> sqlStatementsInRepository);

    public abstract MethodSpec blockingReadMethod(
            SqlConfiguration configuration,
            List<SqlStatement> statements);

    public abstract MethodSpec blockingWriteMethod(
            SqlConfiguration configuration,
            List<SqlStatement> statements);

    public abstract MethodSpec blockingCallMethod(
            SqlConfiguration configuration,
            List<SqlStatement> statements);

    public abstract MethodSpec batchWriteMethod(
            SqlConfiguration configuration,
            List<SqlStatement> statements);

    public abstract MethodSpec streamEagerReadMethod(
            SqlConfiguration configuration,
            List<SqlStatement> statements);

    public abstract MethodSpec streamLazyReadMethod(
            SqlConfiguration configuration,
            List<SqlStatement> statements);

    public abstract MethodSpec rxJavaReadMethod(
            SqlConfiguration configuration,
            List<SqlStatement> statements);

}
