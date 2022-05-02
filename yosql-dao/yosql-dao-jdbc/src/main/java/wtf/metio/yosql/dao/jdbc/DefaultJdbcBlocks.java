/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dao.jdbc;

import com.squareup.javapoet.*;
import de.xn__ho_hia.javapoet.TypeGuesser;
import io.reactivex.Flowable;
import io.smallrye.mutiny.Multi;
import reactor.core.publisher.Flux;
import wtf.metio.yosql.codegen.api.ControlFlows;
import wtf.metio.yosql.codegen.api.Fields;
import wtf.metio.yosql.codegen.api.Variables;
import wtf.metio.yosql.codegen.blocks.GenericBlocks;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.internals.jdk.Buckets;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.LoggingApis;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static wtf.metio.yosql.codegen.blocks.CodeBlocks.code;

public final class DefaultJdbcBlocks implements JdbcBlocks {

    private final RuntimeConfiguration runtimeConfiguration;
    private final GenericBlocks blocks;
    private final ControlFlows controlFlows;
    private final NamesConfiguration names;
    private final Variables variables;
    private final Fields fields;
    private final JdbcMethods jdbcMethods;
    private final LoggingGenerator logging;

    public DefaultJdbcBlocks(
            final RuntimeConfiguration runtimeConfiguration,
            final GenericBlocks blocks,
            final ControlFlows controlFlows,
            final Variables variables,
            final Fields fields,
            final JdbcMethods jdbcMethods,
            final LoggingGenerator logging) {
        this.runtimeConfiguration = runtimeConfiguration;
        this.blocks = blocks;
        this.names = runtimeConfiguration.names();
        this.variables = variables;
        this.controlFlows = controlFlows;
        this.fields = fields;
        this.jdbcMethods = jdbcMethods;
        this.logging = logging;
    }

    @Override
    public CodeBlock connectionVariable() {
        return variables.inline(Connection.class, names.connection(), jdbcMethods.dataSource().getConnection());
    }

    @Override
    public CodeBlock statementVariable() {
        return variables.inline(PreparedStatement.class, names.statement(),
                jdbcMethods.connection().prepareStatement());
    }

    @Override
    public CodeBlock callableVariable() {
        return variables.inline(CallableStatement.class, names.statement(),
                jdbcMethods.connection().prepareCallable());
    }

    @Override
    public CodeBlock readMetaData() {
        return variables.statement(ResultSetMetaData.class, names.resultSetMetaData(),
                jdbcMethods.resultSet().getMetaData());
    }

    @Override
    public CodeBlock readColumnCount() {
        return variables.statement(int.class, names.columnCount(),
                jdbcMethods.resultSetMetaData().getColumnCount());
    }

    @Override
    public CodeBlock resultSetVariable() {
        return variables.inline(ResultSet.class, names.resultSet(),
                jdbcMethods.statement().executeQuery());
    }

    @Override
    public CodeBlock getResultSet() {
        return controlFlows.tryWithResource(variables.inline(ResultSet.class, names.resultSet(),
                jdbcMethods.statement().getResultSet()));
    }

    @Override
    public CodeBlock resultSetVariableStatement() {
        return variables.statement(ResultSet.class, names.resultSet(),
                jdbcMethods.statement().executeQuery());
    }

    @Override
    public CodeBlock returnExecuteUpdate() {
        return blocks.returnValue(jdbcMethods.statement().executeUpdate());
    }

    @Override
    public CodeBlock executeForReturning() {
        return jdbcMethods.statement().execute();
    }

    @Override
    public CodeBlock executeBatch() {
        return blocks.returnValue(jdbcMethods.statement().executeBatch());
    }

    @Override
    public CodeBlock closeResultSet() {
        return blocks.close(names.resultSet());
    }

    @Override
    public CodeBlock closePrepareStatement() {
        return blocks.close(names.statement());
    }

    @Override
    public CodeBlock closeConnection() {
        return blocks.close(names.connection());
    }

    @Override
    public CodeBlock closeState() {
        return blocks.close(names.state());
    }

    @Override
    public CodeBlock executeStatement() {
        return controlFlows.tryWithResource(resultSetVariable());
    }

    @Override
    public CodeBlock openConnection() {
        return controlFlows.tryWithResource(connectionVariable());
    }

    @Override
    public CodeBlock tryPrepareCallable() {
        return controlFlows.tryWithResource(callableVariable());
    }

    @Override
    public CodeBlock createStatement() {
        return controlFlows.tryWithResource(statementVariable());
    }

    @Override
    public CodeBlock prepareBatch(final SqlConfiguration config) {
        return controlFlows.forLoop(
                code("int $N = 0; $N < $N.length; $N++",
                        names.batch(),
                        names.batch(),
                        config.parameters().get(0).name(),
                        names.batch()),
                CodeBlock.builder()
                        .add(setBatchParameters(config))
                        .addStatement(jdbcMethods.statement().addBatch())
                        .build());
    }

    @Override
    public CodeBlock pickVendorQuery(final List<SqlStatement> sqlStatements) {
        final var builder = CodeBlock.builder();
        if (sqlStatements.size() > 1) {
            builder.addStatement(variables.inline(DatabaseMetaData.class, names.databaseMetaData(),
                    jdbcMethods.connection().getMetaData()));
            builder.addStatement(variables.inline(String.class, names.databaseProductName(),
                            jdbcMethods.databaseMetaData().getDatabaseProductName()))
                    .add(logging.vendorDetected());
            if (logging.isEnabled()) {
                builder.addStatement("$T $N = null", String.class, names.rawQuery());
            }
            builder.addStatement("$T $N = null", String.class, names.query())
                    .addStatement("$T $N = null", TypicalTypes.MAP_OF_STRING_AND_ARRAY_OF_INTS, names.indexVariable())
                    .beginControlFlow("switch ($N)", names.databaseProductName());
            sqlStatements.stream()
                    .map(SqlStatement::getConfiguration)
                    .filter(config -> Objects.nonNull(config.vendor()))
                    .forEach(config -> {
                        final var query = fields.constantSqlStatementFieldName(config);
                        builder.add("case $S:\n", config.vendor())
                                .addStatement("$>$N = $N", names.query(), query)
                                .add(logging.vendorQueryPicked(query));
                        finalizeCase(builder, config);
                    });
            final var firstConfigWithoutVendor = sqlStatements.stream()
                    .map(SqlStatement::getConfiguration)
                    .filter(config -> Objects.isNull(config.vendor()))
                    .findFirst();
            if (firstConfigWithoutVendor.isPresent()) {
                final var config = firstConfigWithoutVendor.get();
                final var query = fields.constantSqlStatementFieldName(config);
                builder.add("default:\n")
                        .addStatement("$>$N = $N", names.query(), query)
                        .add(logging.vendorQueryPicked(query));
                finalizeCase(builder, config);
            } else {
                builder.add("default:\n")
                        .addStatement("$>throw new $T($T.format($S, $N))$<", IllegalStateException.class, String.class,
                                "No suitable query defined for vendor [%s]", names.databaseProductName());
            }
            builder.endControlFlow();
        } else {
            final var config = sqlStatements.get(0).getConfiguration();
            final var query = fields.constantSqlStatementFieldName(config);
            builder.addStatement(variables.inline(String.class, names.query(), "$N", query))
                    .add(logging.queryPicked(query));
            if (logging.isEnabled()) {
                final var rawQuery = fields.constantRawSqlStatementFieldName(config);
                builder.addStatement(variables.inline(String.class, names.rawQuery(), "$N", rawQuery));
            }
            if (Buckets.hasEntries(config.parameters())) {
                final var indexFieldName = fields.constantSqlStatementParameterIndexFieldName(config);
                builder.addStatement(variables.inline(TypicalTypes.MAP_OF_STRING_AND_ARRAY_OF_INTS,
                                names.indexVariable(), "$N", indexFieldName))
                        .add(logging.indexPicked(indexFieldName));
            }
        }
        return builder.build();
    }

    private void finalizeCase(final CodeBlock.Builder builder, final SqlConfiguration config) {
        if (logging.isEnabled()) {
            final var rawQuery = fields.constantRawSqlStatementFieldName(config);
            builder.addStatement("$N = $N", names.rawQuery(), rawQuery);
        }
        if (Buckets.hasEntries(config.parameters())) {
            final var indexName = fields.constantSqlStatementParameterIndexFieldName(config);
            builder.addStatement("$N = $N", names.indexVariable(), indexName)
                    .add(logging.vendorIndexPicked(indexName));
        }
        builder.addStatement("break$<");
    }

    @Override
    public CodeBlock logExecutedQuery(final SqlConfiguration sqlConfiguration) {
        final var builder = CodeBlock.builder();
        if (LoggingApis.NONE != runtimeConfiguration.api().loggingApi()) {
            builder.beginControlFlow("if ($L)", logging.shouldLog());
            builder.add(variables.inline(String.class, names.executedQuery(), "$N", names.rawQuery()));
            Stream.ofNullable(sqlConfiguration.parameters())
                    .flatMap(Collection::stream)
                    .forEach(parameter -> {
                        if (TypeGuesser.guessTypeName(parameter.type()).isPrimitive()) {
                            builder.add("\n$>.replace($S, $T.valueOf($N))$<", ":" + parameter.name(),
                                    String.class, parameter.name());
                        } else {
                            builder.add("\n$>.replace($S, $N == null ? $S : $N.toString())$<",
                                    ":" + parameter.name(), parameter.name(), "null",
                                    parameter.name());
                        }
                    });
            builder.add(";\n");
            builder.add(logging.executingQuery());
            builder.endControlFlow();
        }
        return builder.build();
    }

    @Override
    public CodeBlock logExecutedBatchQuery(final SqlConfiguration sqlConfiguration) {
        final var builder = CodeBlock.builder();
        if (LoggingApis.NONE != runtimeConfiguration.api().loggingApi()) {
            builder.beginControlFlow("if ($L)", logging.shouldLog());
            builder.add(variables.inline(String.class, names.executedQuery(), "$N", names.rawQuery()));
            Stream.ofNullable(sqlConfiguration.parameters())
                    .flatMap(Collection::stream)
                    .forEach(parameter -> {
                        if (TypeGuesser.guessTypeName(parameter.type()).isPrimitive()) {
                            builder.add("\n$>.replace($S, $T.toString($N))$<", ":" + parameter.name(),
                                    Arrays.class, parameter.name());
                        } else {
                            builder.add("\n$>.replace($S, $N == null ? $S : $T.toString($N))$<",
                                    ":" + parameter.name(), parameter.name(), "null",
                                    Arrays.class, parameter.name());
                        }
                    });
            builder.add(";\n");
            builder.add(logging.executingQuery());
            builder.endControlFlow();
        }
        return builder.build();
    }

    private CodeBlock.Builder prepareReturnList(final ParameterizedTypeName listOfResults, final String converterAlias) {
        final var java = runtimeConfiguration.java();
        CodeBlock template;
        if (!java.useGenerics()) {
            template = CodeBlock.of("new $T()", ArrayList.class);
        } else if (!java.useDiamondOperator() || java.useVar()) {
            template = CodeBlock.of("new $T()", ParameterizedTypeName.get(
                    ClassName.get(ArrayList.class), listOfResults.typeArguments.get(0)));
        } else {
            template = CodeBlock.of("new $T<>()", ArrayList.class);
        }
        return CodeBlock.builder()
                .addStatement(variables.inline(listOfResults, names.list(), template))
                .add(controlFlows.whileHasNext())
                .addStatement("$N.add($N.$N($N))",
                        names.list(),
                        converterAlias,
                        converterMethod(converterAlias),
                        names.state())
                .endControlFlow();
    }

    private String converterMethod(final String converterAlias) {
        return runtimeConfiguration.converter().rowConverters().stream()
                .filter(converter -> converterAlias.equalsIgnoreCase(converter.alias()))
                .findFirst()
                .or(runtimeConfiguration.converter()::defaultConverter)
                .map(ResultRowConverter::methodName)
                .orElse("apply");
    }

    @Override
    public CodeBlock returnAsList(final ParameterizedTypeName listOfResults, final String converterAlias) {
        return prepareReturnList(listOfResults, converterAlias)
                .addStatement("return $N", names.list())
                .build();
    }

    @Override
    public CodeBlock returnAsFirst(final TypeName resultType, final String converterAlias) {
        return prepareReturnList(TypicalTypes.listOf(resultType), converterAlias)
                .addStatement("return $N.size() > 0 ? $N.get(0) : null", names.list(), names.list())
                .build();
    }

    @Override
    public CodeBlock returnAsOne(final TypeName resultType, final String converterAlias) {
        return prepareReturnList(TypicalTypes.listOf(resultType), converterAlias)
                .beginControlFlow("if ($N.size() != 1)", names.list())
                .addStatement("throw new IllegalStateException()")
                .endControlFlow()
                .addStatement("return $N.get(0)", names.list())
                .build();
    }

    @Override
    public CodeBlock returnAsStream(final ParameterizedTypeName listOfResults, final String converterAlias) {
        return prepareReturnList(listOfResults, converterAlias)
                .addStatement("return $N.stream()", names.list())
                .build();
    }

    @Override
    public CodeBlock returnAsMulti(final ParameterizedTypeName listOfResults, final String converterAlias) {
        return prepareReturnList(listOfResults, converterAlias)
                .addStatement("return $T.createFrom().iterable($N)", Multi.class, names.list())
                .build();
    }

    @Override
    public CodeBlock returnAsFlowable(final ParameterizedTypeName listOfResults, final String converterAlias) {
        return prepareReturnList(listOfResults, converterAlias)
                .addStatement("return $T.fromIterable($N)", Flowable.class, names.list())
                .build();
    }

    @Override
    public CodeBlock returnAsFlux(final ParameterizedTypeName listOfResults, final String converterAlias) {
        return prepareReturnList(listOfResults, converterAlias)
                .addStatement("return $T.fromIterable($N)", Flux.class, names.list())
                .build();
    }

    @Override
    public CodeBlock streamStateful(final TypeSpec spliterator, final TypeSpec closer) {
        return CodeBlock.builder()
                .addStatement("return $T.stream($L, false).onClose($L)",
                        StreamSupport.class,
                        spliterator,
                        closer)
                .build();
    }

    @Override
    public CodeBlock createResultState() {
        return variables.statement(runtimeConfiguration.converter().resultStateClass(), names.state(),
                code("new $T($N, $N, $N)",
                        runtimeConfiguration.converter().resultStateClass(),
                        names.resultSet(),
                        names.resultSetMetaData(),
                        names.columnCount()));
    }

    @Override
    public CodeBlock returnNewFlowState() {
        return CodeBlock.builder()
                .addStatement("return new $T($N, $N, $N, $N, $N)", runtimeConfiguration.converter().flowStateClass(),
                        names.connection(),
                        names.statement(),
                        names.resultSet(),
                        names.resultSetMetaData(),
                        names.columnCount())
                .build();
    }

    @Override
    public CodeBlock newFlowable(final TypeSpec initialState, final TypeSpec generator, final TypeSpec disposer) {
        return CodeBlock.builder()
                .addStatement("return $T.generate($L, $L, $L)",
                        Flowable.class,
                        initialState,
                        generator,
                        disposer)
                .build();
    }

    private CodeBlock parameterAssignment(
            final SqlConfiguration config,
            final String codeStatement,
            final Function<String, Object[]> parameterSetter) {
        final var builder = CodeBlock.builder();
        final var parameters = config.parameters();

        if (parameters != null && !parameters.isEmpty()) {
            for (final var parameter : config.parameters()) {
                builder.add(controlFlows.forLoop(
                        code("final int $N : $N.get($S)",
                                names.jdbcIndexVariable(),
                                names.indexVariable(),
                                parameter.name()),
                        CodeBlock.builder()
                                .addStatement(codeStatement, parameterSetter.apply(parameter.name()))
                                .build()));
            }
        }

        return builder.build();
    }

    @Override
    public CodeBlock setParameters(final SqlConfiguration config) {
        return parameterAssignment(config, "$N.setObject($N, $N)",
                parameterName -> new String[]{
                        names.statement(),
                        names.jdbcIndexVariable(),
                        parameterName});
    }

    @Override
    public CodeBlock setBatchParameters(final SqlConfiguration config) {
        return parameterAssignment(config, "$N.setObject($N, $N[$N])",
                parameterName -> new String[]{
                        names.statement(),
                        names.jdbcIndexVariable(),
                        parameterName,
                        names.batch()});
    }

}
