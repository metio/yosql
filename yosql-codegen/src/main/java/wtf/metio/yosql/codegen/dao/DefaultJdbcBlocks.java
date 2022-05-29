/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.*;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.codegen.blocks.*;
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.internals.jdk.Buckets;
import wtf.metio.yosql.models.configuration.LoggingApis;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static wtf.metio.yosql.codegen.blocks.CodeBlocks.code;

public final class DefaultJdbcBlocks implements JdbcBlocks {

    private final RuntimeConfiguration runtimeConfiguration;
    private final CodeBlocks blocks;
    private final ControlFlows controlFlows;
    private final NamesConfiguration names;
    private final Variables variables;
    private final JdbcMethods jdbcMethods;
    private final LoggingGenerator logging;
    private final FieldsGenerator fields;
    private final Parameters params;
    private final Methods methods;

    public DefaultJdbcBlocks(
            final RuntimeConfiguration runtimeConfiguration,
            final CodeBlocks blocks,
            final ControlFlows controlFlows,
            final Variables variables,
            final FieldsGenerator fields,
            final JdbcMethods jdbcMethods,
            final LoggingGenerator logging,
            final Parameters params,
            final Methods methods) {
        this.runtimeConfiguration = runtimeConfiguration;
        this.blocks = blocks;
        this.names = runtimeConfiguration.names();
        this.variables = variables;
        this.controlFlows = controlFlows;
        this.fields = fields;
        this.jdbcMethods = jdbcMethods;
        this.logging = logging;
        this.params = params;
        this.methods = methods;
    }

    @Override
    public CodeBlock getConnectionInline() {
        return variables.inline(Connection.class, names.connection(), jdbcMethods.dataSource().getConnection());
    }

    @Override
    public CodeBlock prepareStatementInline() {
        return variables.inline(PreparedStatement.class, names.statement(),
                jdbcMethods.connection().prepareStatement());
    }

    @Override
    public CodeBlock prepareCallInline() {
        return variables.inline(CallableStatement.class, names.statement(),
                jdbcMethods.connection().prepareCall());
    }

    @Override
    public CodeBlock getMetaDataStatement() {
        return variables.statement(ResultSetMetaData.class, names.resultSetMetaData(),
                jdbcMethods.resultSet().getMetaData());
    }

    @Override
    public CodeBlock executeQueryInline() {
        return variables.inline(ResultSet.class, names.resultSet(),
                jdbcMethods.statement().executeQuery());
    }

    @Override
    public CodeBlock executeQueryStatement() {
        return variables.statement(ResultSet.class, names.resultSet(),
                jdbcMethods.statement().executeQuery());
    }

    @Override
    public CodeBlock getResultSet() {
        return controlFlows.tryWithResource(variables.inline(ResultSet.class, names.resultSet(),
                jdbcMethods.statement().getResultSet()));
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
        return controlFlows.tryWithResource(executeQueryInline());
    }

    @Override
    public CodeBlock openConnection() {
        return controlFlows.tryWithResource(getConnectionInline());
    }

    @Override
    public CodeBlock tryPrepareCallable() {
        return controlFlows.tryWithResource(prepareCallInline());
    }

    @Override
    public CodeBlock createStatement() {
        return controlFlows.tryWithResource(prepareStatementInline());
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
        if (LoggingApis.NONE != runtimeConfiguration.logging().api()) {
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
        if (LoggingApis.NONE != runtimeConfiguration.logging().api()) {
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

    private CodeBlock.Builder prepareReturnList(final ParameterizedTypeName listOfResults, final ResultRowConverter converter) {
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
                        converter.alias(),
                        converter.methodName(),
                        names.resultSet())
                .endControlFlow();
    }

    @Override
    public CodeBlock returnAsMultiple(final ParameterizedTypeName listOfResults, final ResultRowConverter converter) {
        return prepareReturnList(listOfResults, converter)
                .addStatement("return $N", names.list())
                .build();
    }

    @Override
    public CodeBlock returnAsSingle(final TypeName resultType, final ResultRowConverter converter) {
        return prepareReturnList(TypicalTypes.listOf(resultType), converter)
                .addStatement("return $N.size() > 0 ? $T.of($N.get(0)) : $T.empty()",
                        names.list(), Optional.class, names.list(), Optional.class)
                .build();
    }

    @Override
    public CodeBlock streamStateful(final ResultRowConverter converter) {
        return CodeBlock.builder()
                .addStatement("return $T.stream($L, false).onClose($L)",
                        StreamSupport.class,
                        lazyStreamSpliterator(converter),
                        lazyStreamCloser())
                .build();
    }


    private TypeSpec lazyStreamSpliterator(final ResultRowConverter converter) {
        final var spliteratorClass = ClassName.get(Spliterators.AbstractSpliterator.class);
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        final var superinterface = ParameterizedTypeName.get(spliteratorClass, resultType);
        final var consumerType = TypicalTypes.consumerOf(resultType);
        return TypeSpec
                .anonymousClassBuilder("$T.MAX_VALUE, $T.ORDERED", Long.class, Spliterator.class)
                .addSuperinterface(superinterface)
                .addMethod(methods.implementation("tryAdvance")
                        .addParameter(params.parameter(consumerType, names.action()))
                        .returns(boolean.class)
                        .addCode(controlFlows.startTryBlock())
                        .addCode(controlFlows.ifHasNext())
                        .addStatement("$N.accept($N.$N($N))", names.action(), converter.alias(), converter.methodName(),
                                names.resultSet())
                        .addCode(blocks.returnTrue())
                        .addCode(controlFlows.endIf())
                        .addCode(blocks.returnFalse())
                        .addCode(controlFlows.endTryBlock())
                        .addCode(controlFlows.catchAndRethrow())
                        .build())
                .build();
    }

    private TypeSpec lazyStreamCloser() {
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(Runnable.class)
                .addMethod(methods.implementation("run")
                        .returns(void.class)
                        .addCode(controlFlows.startTryBlock())
                        .addCode(closeResultSet())
                        .addCode(closePrepareStatement())
                        .addCode(closeConnection())
                        .addCode(controlFlows.endTryBlock())
                        .addCode(controlFlows.catchAndRethrow())
                        .build())
                .build();
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

}
