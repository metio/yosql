package com.github.sebhoss.yosql.generator;

import static com.github.sebhoss.yosql.model.SqlConfiguration.merge;

import java.util.ArrayList;
import java.util.List;

import com.github.sebhoss.yosql.model.SqlConfiguration;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.squareup.javapoet.MethodSpec;

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
