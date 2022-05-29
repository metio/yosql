/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.testing.configs.SqlConfigurations;

/**
 * Verifies that {@link JdbcBlocks}s work correctly.
 */
abstract class JdbcBlocksTCK {

    /**
     * @return A new {@link JdbcBlocks}.
     */
    abstract JdbcBlocks generator();

    abstract String getConnectionInlineExpectation();
    abstract String prepareStatementInlineExpectation();
    abstract String prepareCallInlineExpectation();
    abstract String getMetaDataStatementExpectation();
    abstract String executeQueryInlineExpectation();
    abstract String returnExecuteUpdateExpectation();
    abstract String executeBatchExpectation();
    abstract String closeResultSetExpectation();
    abstract String closePrepareStatementExpectation();
    abstract String closeConnectionExpectation();
    abstract String closeStateExpectation();
    abstract String executeStatementExpectation();
    abstract String openConnectionExpectation();
    abstract String tryPrepareCallableExpectation();
    abstract String createStatementExpectation();
    abstract String prepareBatchExpectation();
    abstract String pickVendorQueryExpectation();
    abstract String logExecutedQueryExpectation();
    abstract String logExecutedBatchQueryExpectation();
    abstract String returnAsMultipleExpectation();
    abstract String returnAsSingleExpectation();
    abstract String setParametersExpectation();
    abstract String setBatchParametersExpectation();

    @Test
    final void getConnectionInline() {
        Assertions.assertEquals(
                getConnectionInlineExpectation(),
                generator().getConnectionInline().toString());
    }

    @Test
    final void prepareStatementInline() {
        Assertions.assertEquals(
                prepareStatementInlineExpectation(),
                generator().prepareStatementInline().toString());
    }

    @Test
    final void prepareCallInline() {
        Assertions.assertEquals(
                prepareCallInlineExpectation(),
                generator().prepareCallInline().toString());
    }

    @Test
    final void getMetaDataStatement() {
        Assertions.assertEquals(
                getMetaDataStatementExpectation(),
                generator().getMetaDataStatement().toString());
    }

    @Test
    final void executeQueryInline() {
        Assertions.assertEquals(
                executeQueryInlineExpectation(),
                generator().executeQueryInline().toString());
    }

    @Test
    final void returnExecuteUpdate() {
        Assertions.assertEquals(
                returnExecuteUpdateExpectation(),
                generator().returnExecuteUpdate().toString());
    }

    @Test
    final void executeBatch() {
        Assertions.assertEquals(
                executeBatchExpectation(),
                generator().executeBatch().toString());
    }

    @Test
    final void closeResultSet() {
        Assertions.assertEquals(
                closeResultSetExpectation(),
                generator().closeResultSet().toString());
    }

    @Test
    final void closePrepareStatement() {
        Assertions.assertEquals(
                closePrepareStatementExpectation(),
                generator().closePrepareStatement().toString());
    }

    @Test
    final void closeConnection() {
        Assertions.assertEquals(
                closeConnectionExpectation(),
                generator().closeConnection().toString());
    }

    @Test
    final void closeState() {
        Assertions.assertEquals(
                closeStateExpectation(),
                generator().closeState().toString());
    }

    @Test
    final void executeStatement() {
        Assertions.assertEquals(
                executeStatementExpectation(),
                generator().executeStatement().toString());
    }

    @Test
    final void openConnection() {
        Assertions.assertEquals(
                openConnectionExpectation(),
                generator().openConnection().toString());
    }

    @Test
    final void tryPrepareCallable() {
        Assertions.assertEquals(
                tryPrepareCallableExpectation(),
                generator().tryPrepareCallable().toString());
    }

    @Test
    final void createStatement() {
        Assertions.assertEquals(
                createStatementExpectation(),
                generator().createStatement().toString());
    }

    @Test
    final void prepareBatch() {
        Assertions.assertEquals(
                prepareBatchExpectation(),
                generator().prepareBatch(SqlConfigurations.sqlConfiguration()).toString());
    }

    @Test
    final void pickVendorQuery() {
        Assertions.assertEquals(
                pickVendorQueryExpectation(),
                generator().pickVendorQuery(SqlConfigurations.sqlStatement()).toString());
    }

    @Test
    final void logExecutedQuery() {
        Assertions.assertEquals(
                logExecutedQueryExpectation(),
                generator().logExecutedQuery(SqlConfigurations.sqlConfiguration()).toString());
    }

    @Test
    final void logExecutedBatchQuery() {
        Assertions.assertEquals(
                logExecutedBatchQueryExpectation(),
                generator().logExecutedBatchQuery(SqlConfigurations.sqlConfiguration()).toString());
    }

    @Test
    final void returnAsMultiple() {
        final var converter = ResultRowConverter.builder()
                .setAlias("converter")
                .setConverterType("com.example.Converter")
                .setMethodName("apply").setResultType("com.example.Domain")
                .build();
        Assertions.assertEquals(
                returnAsMultipleExpectation(),
                generator().returnAsMultiple(TypicalTypes.listOf(TypeName.OBJECT), converter).toString());
    }

    @Test
    final void returnAsSingle() {
        final var converter = ResultRowConverter.builder()
                .setAlias("converter")
                .setConverterType("com.example.Converter")
                .setMethodName("apply").setResultType("com.example.Domain")
                .build();
        Assertions.assertEquals(
                returnAsSingleExpectation(),
                generator().returnAsSingle(TypicalTypes.listOf(TypeName.OBJECT), converter).toString());
    }

    @Test
    final void setParameters() {
        Assertions.assertEquals(
                setParametersExpectation(),
                generator().setParameters(SqlConfigurations.sqlConfiguration()).toString());
    }

    @Test
    final void setBatchParameters() {
        Assertions.assertEquals(
                setBatchParametersExpectation(),
                generator().setBatchParameters(SqlConfigurations.sqlConfiguration()).toString());
    }

}
