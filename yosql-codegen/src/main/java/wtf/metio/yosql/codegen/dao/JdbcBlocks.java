/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.dao;

import com.palantir.javapoet.CodeBlock;
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

    /**
     * The result set of a statement that has already run, declared plainly rather than as a resource.
     *
     * <p>A method that hands the caller a lazy {@link java.util.stream.Stream} cannot close the result
     * set when it returns — the stream still has to read from it. Ownership passes to the stream's
     * {@code onClose}, which is why this variant exists next to {@link #getResultSet()}.</p>
     */
    CodeBlock getResultSetStatement();

    CodeBlock executeQueryStatement();

    CodeBlock returnExecuteUpdate(SqlConfiguration configuration);

    CodeBlock executeForReturning(SqlConfiguration configuration);

    CodeBlock executeBatch();

    CodeBlock closeResultSet();

    CodeBlock closePrepareStatement();

    CodeBlock closeConnection(SqlConfiguration configuration);

    CodeBlock openConnection(SqlConfiguration configuration);

    /**
     * @return how many control flows {@link #openConnection(SqlConfiguration)} opened for this
     *         statement — one for a try-with-resources around the connection, one for the plain try
     *         that catch-and-rethrow needs, and none when the statement does neither. Whoever closes
     *         those flows has to ask, because a fixed count is right for two of those three cases.
     */
    int connectionFlows(SqlConfiguration configuration);

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

    /**
     * Opens the scope that closes the connection again if the method never hands it to a Stream.
     *
     * <p>Empty when the caller passed the connection in, because closing what somebody else opened is
     * not this method's to do — the same rule {@link #closeConnection(SqlConfiguration)} follows.</p>
     */
    CodeBlock openConnectionScope(SqlConfiguration configuration);

    /**
     * Closes that scope. Pair with {@link #openConnectionScope(SqlConfiguration)}.
     */
    CodeBlock closeConnectionOnFailure(SqlConfiguration configuration);

    CodeBlock setParameters(SqlConfiguration configuration);

    CodeBlock setBatchParameters(SqlConfiguration configuration);

}
