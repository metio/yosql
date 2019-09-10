package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.*;
import wtf.metio.yosql.model.sql.SqlConfiguration;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;
import java.util.function.Function;

/**
 * Reusable code blocks using the JDBC API.
 */
public interface JdbcBlocks {

    CodeBlock connectionVariable();

    CodeBlock statementVariable();

    CodeBlock callableVariable();

    CodeBlock readMetaData();

    CodeBlock reactColumnCount();

    CodeBlock resultSetVariable();

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

    CodeBlock.Builder prepareReturnList(TypeName listOfResults, String converterAlias);

    CodeBlock returnAsList(TypeName listOfResults, String converterAlias);
    CodeBlock returnAsStream(TypeName listOfResults, String converterAlias);

    CodeBlock streamStateful(TypeSpec spliterator, TypeSpec closer);

    CodeBlock createResultState();
    CodeBlock returnNewFlowState();
    CodeBlock newFlowable(TypeSpec initialState, TypeSpec generator, TypeSpec disposer);

    CodeBlock parameterAssignment(
            SqlConfiguration configuration,
            String codeStatement,
            Function<String, Object[]> parameterSetter);

    CodeBlock setParameters(final SqlConfiguration configuration);
    CodeBlock setBatchParameters(final SqlConfiguration configuration);

}
