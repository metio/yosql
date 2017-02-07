package com.github.sebhoss.yosql.generator;

import java.util.ArrayList;
import java.util.List;

import com.github.sebhoss.yosql.model.SqlStatement;
import com.squareup.javapoet.MethodSpec;

public abstract class AbstractMethodGenerator implements MethodGenerator {

    @Override
    public final Iterable<MethodSpec> asMethods(final List<SqlStatement> sqlStatementsInRepository) {
        final List<MethodSpec> methods = new ArrayList<>(sqlStatementsInRepository.size());

        methods.add(constructor(sqlStatementsInRepository));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateStandardReadAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods.add(standardReadMethod(methodName, statements)));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateStandardWriteAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods.add(standardWriteMethod(methodName, statements)));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateBatchAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods.add(batchWriteMethod(methodName, statements)));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateStreamEagerAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods.add(streamEagerReadMethod(methodName, statements)));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateStreamLazyAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods.add(streamLazyReadMethod(methodName, statements)));
        sqlStatementsInRepository.stream()
                .filter(SqlStatement::shouldGenerateRxJavaAPI)
                .collect(SqlStatement.groupByName())
                .forEach((methodName, statements) -> methods.add(rxJavaReadMethod(methodName, statements)));

        return methods;
    }

    protected abstract MethodSpec constructor(List<SqlStatement> sqlStatementsInRepository);

    protected abstract MethodSpec standardReadMethod(String methodName, List<SqlStatement> vendorStatements);

    protected abstract MethodSpec standardWriteMethod(String methodName, List<SqlStatement> vendorStatements);

    protected abstract MethodSpec batchWriteMethod(String methodName, List<SqlStatement> vendorStatements);

    protected abstract MethodSpec streamEagerReadMethod(String methodName, List<SqlStatement> vendorStatements);

    protected abstract MethodSpec streamLazyReadMethod(String methodName, List<SqlStatement> vendorStatements);

    protected abstract MethodSpec rxJavaReadMethod(String methodName, List<SqlStatement> vendorStatements);

}
