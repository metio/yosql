/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dao.jdbc;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * Reusable code blocks using the JDBC API.
 */
public interface JdbcBlocks {

    CodeBlock connectionVariable();

    CodeBlock statementVariable();

    CodeBlock callableVariable();

    CodeBlock readMetaData();

    CodeBlock readColumnCount();

    CodeBlock resultSetVariable();

    CodeBlock resultSetVariableStatement();

    CodeBlock executeUpdate();

    CodeBlock executeBatch();

    CodeBlock closeResultSet();

    CodeBlock closePrepareStatement();

    CodeBlock closeConnection();

    CodeBlock closeState();

    CodeBlock executeStatement();

    CodeBlock openConnection();

    CodeBlock tryPrepareCallable();

    CodeBlock createStatement();

    CodeBlock prepareBatch(SqlConfiguration configuration);

    CodeBlock pickVendorQuery(List<SqlStatement> sqlStatements);

    CodeBlock logExecutedQuery(SqlConfiguration sqlConfiguration);

    CodeBlock logExecutedBatchQuery(SqlConfiguration sqlConfiguration);

    CodeBlock returnAsList(ParameterizedTypeName listOfResults, String converterAlias);

    CodeBlock returnAsStream(ParameterizedTypeName listOfResults, String converterAlias);

    CodeBlock streamStateful(TypeSpec spliterator, TypeSpec closer);

    CodeBlock createResultState();

    CodeBlock returnNewFlowState();

    CodeBlock newFlowable(TypeSpec initialState, TypeSpec generator, TypeSpec disposer);

    CodeBlock setParameters(final SqlConfiguration configuration);

    CodeBlock setBatchParameters(final SqlConfiguration configuration);

}
