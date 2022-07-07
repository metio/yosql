/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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

    CodeBlock closeConnection();

    CodeBlock openConnection();

    CodeBlock tryPrepareCallable();

    CodeBlock executeStatement(SqlConfiguration configuration);

    CodeBlock createStatement(SqlConfiguration configuration);

    CodeBlock prepareBatch(SqlConfiguration configuration);

    CodeBlock pickVendorQuery(List<SqlStatement> sqlStatements);

    CodeBlock logExecutedQuery(SqlConfiguration sqlConfiguration);

    CodeBlock logExecutedBatchQuery(SqlConfiguration sqlConfiguration);

    CodeBlock returnAsMultiple(ResultRowConverter converter);

    CodeBlock returnAsSingle(SqlConfiguration sqlConfiguration);

    CodeBlock streamStateful(ResultRowConverter converter);

    CodeBlock setParameters(SqlConfiguration configuration);

    CodeBlock setBatchParameters(SqlConfiguration configuration);

}
