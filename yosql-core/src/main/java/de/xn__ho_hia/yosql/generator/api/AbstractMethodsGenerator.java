/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.api;

import static de.xn__ho_hia.yosql.model.SqlConfiguration.merge;

import java.util.ArrayList;
import java.util.List;

import com.squareup.javapoet.MethodSpec;

import de.xn__ho_hia.yosql.model.SqlConfiguration;
import de.xn__ho_hia.yosql.model.SqlStatement;

@SuppressWarnings({ "javadoc" })
public abstract class AbstractMethodsGenerator implements MethodsGenerator {

    @Override
    public final Iterable<MethodSpec> asMethods(final List<SqlStatement> sqlStatementsInRepository) {
        final List<MethodSpec> methods = new ArrayList<>(sqlStatementsInRepository.size());

        methods.add(constructor(sqlStatementsInRepository));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateStandardReadAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods
                        .add(standardReadMethod(methodName, merge(statements), statements)));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateStandardWriteAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods
                        .add(standardWriteMethod(methodName, merge(statements), statements)));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateStandardCallAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods
                        .add(standardCallMethod(methodName, merge(statements), statements)));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateBatchAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods
                        .add(batchWriteMethod(methodName, merge(statements), statements)));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateStreamEagerAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods
                        .add(streamEagerReadMethod(methodName, merge(statements), statements)));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateStreamLazyAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods
                        .add(streamLazyReadMethod(methodName, merge(statements), statements)));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateRxJavaAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods
                        .add(rxJavaReadMethod(methodName, merge(statements), statements)));

        return methods;
    }

    protected abstract MethodSpec constructor(List<SqlStatement> sqlStatementsInRepository);

    protected abstract MethodSpec standardReadMethod(
            String methodName,
            SqlConfiguration mergedConfiguration,
            List<SqlStatement> vendorStatements);

    protected abstract MethodSpec standardWriteMethod(
            String methodName,
            SqlConfiguration sqlConfiguration,
            List<SqlStatement> vendorStatements);

    protected abstract MethodSpec standardCallMethod(
            String methodName,
            SqlConfiguration sqlConfiguration,
            List<SqlStatement> vendorStatements);

    protected abstract MethodSpec batchWriteMethod(
            String methodName,
            SqlConfiguration sqlConfiguration,
            List<SqlStatement> vendorStatements);

    protected abstract MethodSpec streamEagerReadMethod(
            String methodName,
            SqlConfiguration sqlConfiguration,
            List<SqlStatement> vendorStatements);

    protected abstract MethodSpec streamLazyReadMethod(
            String methodName,
            SqlConfiguration sqlConfiguration,
            List<SqlStatement> vendorStatements);

    protected abstract MethodSpec rxJavaReadMethod(
            String methodName,
            SqlConfiguration sqlConfiguration,
            List<SqlStatement> vendorStatements);

}
