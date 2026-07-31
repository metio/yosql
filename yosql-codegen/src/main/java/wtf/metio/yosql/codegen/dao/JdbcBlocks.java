/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * Reusable code blocks using the JDBC API.
 */
public interface JdbcBlocks {

    CodeBlock getConnectionInline();

    CodeBlock getConnection(SqlConfiguration configuration);

    CodeBlock prepareStatementInline();

    CodeBlock prepareCallInline();

    CodeBlock getMetaDataStatement();

    CodeBlock executeQueryInline();

    CodeBlock getResultSet();

    CodeBlock executeQueryStatement();

    CodeBlock returnExecuteUpdate(SqlConfiguration configuration);

    CodeBlock executeForReturning();

    CodeBlock executeBatch();

    CodeBlock closeResultSet();

    CodeBlock closePrepareStatement();

    CodeBlock closeConnection(SqlConfiguration configuration);

    CodeBlock openConnection(SqlConfiguration configuration);

    CodeBlock tryPrepareCallable();

    CodeBlock executeStatement(SqlConfiguration configuration);

    CodeBlock createStatement(SqlConfiguration configuration);

    CodeBlock prepareBatch(SqlConfiguration configuration);

    CodeBlock pickVendorQuery(List<SqlStatement> sqlStatements);

    CodeBlock logExecutedQuery(SqlConfiguration sqlConfiguration);

    CodeBlock logExecutedBatchQuery(SqlConfiguration sqlConfiguration);

    CodeBlock returnAsMultiple(ResultRowConverter converter);

    CodeBlock returnAsSingle(SqlConfiguration sqlConfiguration);

    CodeBlock streamStateful(SqlConfiguration configuration);

    CodeBlock setParameters(SqlConfiguration configuration);

    CodeBlock setBatchParameters(SqlConfiguration configuration);

}
