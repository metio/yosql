/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.api;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.model.sql.SqlConfiguration;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.ArrayList;
import java.util.List;

import static wtf.metio.yosql.model.sql.SqlConfiguration.merge;

/**
 * Abstract implementation of a {@link MethodsGenerator}.
 */
public abstract class AbstractMethodsGenerator implements MethodsGenerator {

    @Override
    public final Iterable<MethodSpec> asMethods(final List<SqlStatement> statements) {
        final var methods = new ArrayList<MethodSpec>(statements.size());

        methods.add(constructor(statements));
        statements.stream()
                .filter(SqlStatement::shouldGenerateStandardReadAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statementsInRepository) -> methods
                        .add(standardReadMethod(merge(statementsInRepository), statementsInRepository)));
        statements.stream()
                .filter(SqlStatement::shouldGenerateStandardWriteAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statementsInRepository) -> methods
                        .add(standardWriteMethod(merge(statementsInRepository), statementsInRepository)));
        statements.stream()
                .filter(SqlStatement::shouldGenerateStandardCallAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statementsInRepository) -> methods
                        .add(standardCallMethod(merge(statementsInRepository), statementsInRepository)));
        statements.stream()
                .filter(SqlStatement::shouldGenerateBatchAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statementsInRepository) -> methods
                        .add(batchWriteMethod(merge(statementsInRepository), statementsInRepository)));
        statements.stream()
                .filter(SqlStatement::shouldGenerateStreamEagerAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statementsInRepository) -> methods
                        .add(streamEagerReadMethod(merge(statementsInRepository), statementsInRepository)));
        statements.stream()
                .filter(SqlStatement::shouldGenerateStreamLazyAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statementsInRepository) -> methods
                        .add(streamLazyReadMethod(merge(statementsInRepository), statementsInRepository)));
        statements.stream()
                .filter(SqlStatement::shouldGenerateRxJavaAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statementsInRepository) -> methods
                        .add(rxJavaReadMethod(merge(statementsInRepository), statementsInRepository)));

        return methods;
    }

    protected abstract MethodSpec constructor(List<SqlStatement> sqlStatementsInRepository);

    protected abstract MethodSpec standardReadMethod(
            SqlConfiguration configuration,
            List<SqlStatement> statements);

    protected abstract MethodSpec standardWriteMethod(
            SqlConfiguration configuration,
            List<SqlStatement> statements);

    protected abstract MethodSpec standardCallMethod(
            SqlConfiguration configuration,
            List<SqlStatement> statements);

    protected abstract MethodSpec batchWriteMethod(
            SqlConfiguration configuration,
            List<SqlStatement> statements);

    protected abstract MethodSpec streamEagerReadMethod(
            SqlConfiguration configuration,
            List<SqlStatement> statements);

    protected abstract MethodSpec streamLazyReadMethod(
            SqlConfiguration configuration,
            List<SqlStatement> statements);

    protected abstract MethodSpec rxJavaReadMethod(
            SqlConfiguration configuration,
            List<SqlStatement> statements);

}
