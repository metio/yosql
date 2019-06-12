/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.api;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.model.SqlConfiguration;
import wtf.metio.yosql.model.SqlStatement;

import java.util.ArrayList;
import java.util.List;

import static wtf.metio.yosql.model.SqlConfiguration.merge;

/**
 * Abstract implementation of a {@link MethodsGenerator}.
 */
public abstract class AbstractMethodsGenerator implements MethodsGenerator {

    @Override
    public final Iterable<MethodSpec> asMethods(final List<SqlStatement> sqlStatementsInRepository) {
        final List<MethodSpec> methods = new ArrayList<>(sqlStatementsInRepository.size());

        methods.add(constructor(sqlStatementsInRepository));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateStandardReadAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods
                        .add(standardReadMethod(merge(statements), statements)));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateStandardWriteAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods
                        .add(standardWriteMethod(merge(statements), statements)));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateStandardCallAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods
                        .add(standardCallMethod(merge(statements), statements)));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateBatchAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods
                        .add(batchWriteMethod(merge(statements), statements)));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateStreamEagerAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods
                        .add(streamEagerReadMethod(merge(statements), statements)));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateStreamLazyAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods
                        .add(streamLazyReadMethod(merge(statements), statements)));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateRxJavaAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods
                        .add(rxJavaReadMethod(merge(statements), statements)));

        return methods;
    }

    protected abstract MethodSpec constructor(List<SqlStatement> sqlStatementsInRepository);

    protected abstract MethodSpec standardReadMethod(
            SqlConfiguration mergedConfiguration,
            List<SqlStatement> vendorStatements);

    protected abstract MethodSpec standardWriteMethod(
            SqlConfiguration sqlConfiguration,
            List<SqlStatement> vendorStatements);

    protected abstract MethodSpec standardCallMethod(
            SqlConfiguration sqlConfiguration,
            List<SqlStatement> vendorStatements);

    protected abstract MethodSpec batchWriteMethod(
            SqlConfiguration sqlConfiguration,
            List<SqlStatement> vendorStatements);

    protected abstract MethodSpec streamEagerReadMethod(
            SqlConfiguration sqlConfiguration,
            List<SqlStatement> vendorStatements);

    protected abstract MethodSpec streamLazyReadMethod(
            SqlConfiguration sqlConfiguration,
            List<SqlStatement> vendorStatements);

    protected abstract MethodSpec rxJavaReadMethod(
            SqlConfiguration sqlConfiguration,
            List<SqlStatement> vendorStatements);

}
