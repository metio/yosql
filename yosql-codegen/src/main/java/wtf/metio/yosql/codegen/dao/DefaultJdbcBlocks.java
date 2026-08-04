/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.models.configuration.GeneratedNames;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeSpec;
import wtf.metio.yosql.codegen.blocks.*;
import wtf.metio.yosql.codegen.exceptions.*;
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.codegen.records.ParameterConversions;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.internals.jdk.Buckets;
import wtf.metio.yosql.models.configuration.LoggingApis;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.StreamSupport;
import java.util.function.BiFunction;

import static wtf.metio.yosql.codegen.blocks.CodeBlocks.code;

public final class DefaultJdbcBlocks implements JdbcBlocks {

    private final RuntimeConfiguration runtimeConfiguration;
    private final CodeBlocks blocks;
    private final ControlFlows controlFlows;
    private final Variables variables;
    private final JdbcMethods jdbcMethods;
    private final LoggingGenerator logging;
    private final FieldsGenerator fields;
    private final Parameters params;
    private final Methods methods;
    private final ParameterConversions parameterConversions;

    public DefaultJdbcBlocks(
            final RuntimeConfiguration runtimeConfiguration,
            final CodeBlocks blocks,
            final ControlFlows controlFlows,
            final Variables variables,
            final FieldsGenerator fields,
            final JdbcMethods jdbcMethods,
            final LoggingGenerator logging,
            final Parameters params,
            final Methods methods,
            final ParameterConversions parameterConversions) {
        this.runtimeConfiguration = runtimeConfiguration;
        this.blocks = blocks;
        this.variables = variables;
        this.controlFlows = controlFlows;
        this.fields = fields;
        this.jdbcMethods = jdbcMethods;
        this.logging = logging;
        this.params = params;
        this.methods = methods;
        this.parameterConversions = parameterConversions;
    }

    @Override
    public CodeBlock getConnectionInline() {
        return variables.inline(Connection.class, GeneratedNames.CONNECTION, jdbcMethods.dataSource().getConnection());
    }

    @Override
    public CodeBlock getConnection(final SqlConfiguration configuration) {
        return configuration.createConnection()
                .filter(Boolean.TRUE::equals)
                .map(value -> variables.statement(Connection.class, GeneratedNames.CONNECTION,
                        jdbcMethods.dataSource().getConnection()))
                .orElseGet(() -> CodeBlock.builder().build());
    }

    @Override
    public CodeBlock prepareStatementInline() {
        return variables.inline(PreparedStatement.class, GeneratedNames.STATEMENT,
                jdbcMethods.connection().prepareStatement());
    }

    @Override
    public CodeBlock prepareCallInline() {
        return variables.inline(CallableStatement.class, GeneratedNames.STATEMENT,
                jdbcMethods.connection().prepareCall());
    }

    @Override
    public CodeBlock getMetaDataStatement() {
        return variables.statement(ResultSetMetaData.class, GeneratedNames.RESULT_SET_META_DATA,
                jdbcMethods.resultSet().getMetaData());
    }

    @Override
    public CodeBlock executeQueryInline() {
        return variables.inline(ResultSet.class, GeneratedNames.RESULT_SET,
                jdbcMethods.statement().executeQuery());
    }

    @Override
    public CodeBlock executeQueryStatement() {
        return variables.statement(ResultSet.class, GeneratedNames.RESULT_SET,
                jdbcMethods.statement().executeQuery());
    }

    @Override
    public CodeBlock getResultSet() {
        return controlFlows.tryWithResource(variables.inline(ResultSet.class, GeneratedNames.RESULT_SET,
                jdbcMethods.statement().getResultSet()));
    }

    @Override
    public CodeBlock getResultSetStatement() {
        return variables.statement(ResultSet.class, GeneratedNames.RESULT_SET,
                jdbcMethods.statement().getResultSet());
    }

    @Override
    public CodeBlock returnExecuteUpdate(final SqlConfiguration configuration) {
        final var executeUpdate = prepares(configuration)
                ? jdbcMethods.statement().executeUpdate()
                : jdbcMethods.statement().executeGivenUpdate();
        return configuration.writesReturnUpdateCount()
                .filter(Boolean.TRUE::equals)
                .map(value -> blocks.returnValue(executeUpdate))
                .orElseGet(() -> CodeBlock.builder()
                        .addStatement(executeUpdate)
                        .build());
    }

    @Override
    public CodeBlock executeForReturning(final SqlConfiguration configuration) {
        return prepares(configuration)
                ? jdbcMethods.statement().execute()
                : jdbcMethods.statement().executeGiven();
    }

    @Override
    public CodeBlock executeBatch() {
        return blocks.returnValue(jdbcMethods.statement().executeBatch());
    }

    @Override
    public CodeBlock closeResultSet() {
        return blocks.close(GeneratedNames.RESULT_SET);
    }

    @Override
    public CodeBlock closePrepareStatement() {
        return blocks.close(GeneratedNames.STATEMENT);
    }

    @Override
    public CodeBlock closeConnection(final SqlConfiguration configuration) {
        return configuration.createConnection()
                .filter(Boolean.TRUE::equals)
                .map(value -> blocks.close(GeneratedNames.CONNECTION))
                .orElseGet(() -> CodeBlock.builder().build());
    }

    @Override
    public CodeBlock executeStatement(final SqlConfiguration configuration) {
        return configuration.usePreparedStatement()
                .filter(Boolean.TRUE::equals)
                .map(value -> controlFlows.tryWithResource(executeQueryInline()))
                .orElseGet(() -> controlFlows.tryWithResource(executeStatementQueryInline()));
    }

    private CodeBlock executeStatementQueryInline() {
        return variables.inline(ResultSet.class, GeneratedNames.RESULT_SET,
                jdbcMethods.statement().executeGivenQuery());
    }

    @Override
    public CodeBlock openConnection(final SqlConfiguration configuration) {
        return configuration.createConnection()
                .filter(Boolean.TRUE::equals)
                .map(value -> controlFlows.tryWithResource(getConnectionInline()))
                .or(() -> configuration.catchAndRethrow()
                        .filter(Boolean.TRUE::equals)
                        .map(value -> controlFlows.startTryBlock()))
                .orElseGet(() -> CodeBlock.builder().build());
    }

    @Override
    public int connectionFlows(final SqlConfiguration configuration) {
        final var opensConnection = configuration.createConnection().filter(Boolean.TRUE::equals).isPresent();
        final var catchesAndRethrows = configuration.catchAndRethrow().filter(Boolean.TRUE::equals).isPresent();
        return opensConnection || catchesAndRethrows ? 1 : 0;
    }

    @Override
    public CodeBlock tryPrepareCallable() {
        return controlFlows.tryWithResource(prepareCallInline());
    }

    @Override
    public CodeBlock createStatement(final SqlConfiguration configuration) {
        if (prepares(configuration)) {
            return controlFlows.tryWithResource(prepareStatementInline());
        }
        refuseParametersWithoutPreparedStatement(configuration);
        return controlFlows.tryWithResource(statementInline());
    }

    private static boolean prepares(final SqlConfiguration configuration) {
        return configuration.usePreparedStatement().filter(Boolean.TRUE::equals).isPresent();
    }

    /**
     * A plain statement carries its query as text, so there is nowhere for a value to be bound.
     * Emitting the binding calls anyway is how this produced a repository that does not compile.
     */
    private static void refuseParametersWithoutPreparedStatement(final SqlConfiguration configuration) {
        configuration.parameters().stream()
                .findFirst()
                .ifPresent(parameter -> {
                    throw new UnbindableStatementException(
                            configuration.name().orElseThrow(MissingSqlConfigurationNameException::new),
                            parameter.name().orElseThrow(MissingParameterNameException::new));
                });
    }

    private CodeBlock statementInline() {
        return variables.inline(Statement.class, GeneratedNames.STATEMENT,
                jdbcMethods.connection().createStatement());
    }

    @Override
    public CodeBlock prepareBatch(final SqlConfiguration config) {
        final var statement = config.name().orElseThrow(MissingSqlConfigurationNameException::new);
        if (config.parameters().isEmpty()) {
            throw UnbatchableStatementException.withoutParameters(statement);
        }
        config.parameters().stream()
                .filter(InLists::expands)
                .findFirst()
                .ifPresent(parameter -> {
                    throw UnbatchableStatementException.withCollection(statement,
                            parameter.name().orElseThrow(MissingParameterNameException::new));
                });
        return controlFlows.forLoop(
                code("int $N = 0; $N < $N.length; $N++",
                        GeneratedNames.BATCH,
                        GeneratedNames.BATCH,
                        config.parameters().getFirst().name().orElseThrow(MissingParameterNameException::new),
                        GeneratedNames.BATCH),
                CodeBlock.builder()
                        .add(setBatchParameters(config))
                        .addStatement(prepares(config)
                                ? jdbcMethods.statement().addBatch()
                                : jdbcMethods.statement().addGivenBatch())
                        .build());
    }

    @Override
    public CodeBlock pickVendorQuery(final List<SqlStatement> sqlStatements) {
        final var builder = CodeBlock.builder();
        if (sqlStatements.size() > 1) {
            sqlStatements.stream()
                    .map(SqlStatement::getConfiguration)
                    .filter(InLists::anyExpands)
                    .findFirst()
                    .ifPresent(config -> {
                        throw new UnexpandableParameterException(
                                config.name().orElseThrow(MissingSqlConfigurationNameException::new),
                                config.parameters().stream()
                                        .filter(InLists::expands)
                                        .findFirst()
                                        .flatMap(SqlParameter::name)
                                        .orElseThrow(MissingParameterNameException::new));
                    });
            builder.addStatement(variables.inline(DatabaseMetaData.class, GeneratedNames.DATABASE_META_DATA,
                    jdbcMethods.connection().getMetaData()));
            builder.addStatement(variables.inline(String.class, GeneratedNames.DATABASE_PRODUCT_NAME,
                            jdbcMethods.databaseMetaData().getDatabaseProductName()))
                    .add(logging.vendorDetected());
            if (logging.isEnabled()) {
                builder.addStatement("$T $N = null", String.class, GeneratedNames.RAW_QUERY);
            }
            builder.addStatement("$T $N = null", String.class, GeneratedNames.QUERY)
                    .addStatement("$T $N = null", TypicalTypes.MAP_OF_STRING_AND_ARRAY_OF_INTS, GeneratedNames.INDEX)
                    .beginControlFlow("switch ($N)", GeneratedNames.DATABASE_PRODUCT_NAME);
            sqlStatements.stream()
                    .map(SqlStatement::getConfiguration)
                    .filter(config -> config.vendor().isPresent())
                    .forEach(config -> {
                        final var query = fields.constantSqlStatementFieldName(config);
                        builder.add("case $S:\n", config.vendor().orElseThrow(MissingSqlConfigurationVendorException::new))
                                .addStatement("$>$N = $N", GeneratedNames.QUERY, query)
                                .add(logging.vendorQueryPicked(query));
                        finalizeCase(builder, config);
                    });
            sqlStatements.stream()
                    .map(SqlStatement::getConfiguration)
                    .filter(config -> config.vendor().isEmpty())
                    .findFirst()
                    .ifPresentOrElse(config -> {
                        final var query = fields.constantSqlStatementFieldName(config);
                        builder.add("default:\n")
                                .addStatement("$>$N = $N", GeneratedNames.QUERY, query)
                                .add(logging.vendorQueryPicked(query));
                        finalizeCase(builder, config);
                    }, () -> builder.add("default:\n")
                            .addStatement("$>throw new $T($T.format($S, $N))$<", IllegalStateException.class, String.class,
                                    "No suitable query defined for vendor [%s]", GeneratedNames.DATABASE_PRODUCT_NAME));

            builder.endControlFlow();
        } else {
            final var config = sqlStatements.getFirst().getConfiguration();
            final var query = fields.constantSqlStatementFieldName(config);
            if (InLists.anyExpands(config)) {
                return expandedQuery(sqlStatements.getFirst(), query);
            }
            builder.addStatement(variables.inline(String.class, GeneratedNames.QUERY, "$N", query))
                    .add(logging.queryPicked(query));
            if (logging.isEnabled()) {
                final var rawQuery = fields.constantRawSqlStatementFieldName(config);
                builder.addStatement(variables.inline(String.class, GeneratedNames.RAW_QUERY, "$N", rawQuery));
            }
            if (Buckets.hasEntries(config.parameters())) {
                final var indexFieldName = fields.constantSqlStatementParameterIndexFieldName(config);
                builder.addStatement(variables.inline(TypicalTypes.MAP_OF_STRING_AND_ARRAY_OF_INTS,
                                GeneratedNames.INDEX, "$N", indexFieldName))
                        .add(logging.indexPicked(indexFieldName));
            }
        }
        return builder.build();
    }

    private void finalizeCase(final CodeBlock.Builder builder, final SqlConfiguration config) {
        if (logging.isEnabled()) {
            final var rawQuery = fields.constantRawSqlStatementFieldName(config);
            builder.addStatement("$N = $N", GeneratedNames.RAW_QUERY, rawQuery);
        }
        if (Buckets.hasEntries(config.parameters())) {
            final var indexName = fields.constantSqlStatementParameterIndexFieldName(config);
            builder.addStatement("$N = $N", GeneratedNames.INDEX, indexName)
                    .add(logging.vendorIndexPicked(indexName));
        }
        builder.addStatement("break$<");
    }

    @Override
    public CodeBlock logExecutedQuery(final SqlConfiguration sqlConfiguration) {
        return logQuery(sqlConfiguration, DefaultJdbcBlocks::singleValue);
    }

    @Override
    public CodeBlock logExecutedBatchQuery(final SqlConfiguration sqlConfiguration) {
        return logQuery(sqlConfiguration, DefaultJdbcBlocks::arrayOfValues);
    }

    /**
     * Builds the statement that fills a query's placeholders in for the log.
     *
     * <p>The single-row and batch variants differ in one thing: how a parameter's value is turned
     * into text. Everything around it — the guard, the local, the terminating semicolon — is the
     * same, and was the same twice.</p>
     */
    private CodeBlock logQuery(
            final SqlConfiguration sqlConfiguration,
            final BiFunction<TypeName, String, CodeBlock> asText) {
        final var builder = CodeBlock.builder();
        if (LoggingApis.NONE == runtimeConfiguration.logging().api()) {
            return builder.build();
        }
        builder.beginControlFlow("if ($L)", logging.shouldLog());
        builder.add(variables.inline(String.class, GeneratedNames.EXECUTED_QUERY, "$N", GeneratedNames.RAW_QUERY));
        sqlConfiguration.parameters().forEach(parameter -> {
            final var type = parameter.typeName().orElseThrow(MissingParameterTypeNameException::new);
            final var name = parameter.name().orElseThrow(MissingParameterNameException::new);
            builder.add("\n$>.replace($S, $L)$<", ":" + name, asText.apply(type, name));
        });
        builder.add(";\n");
        builder.add(logging.executingQuery());
        builder.endControlFlow();
        return builder.build();
    }

    /**
     * A primitive cannot be null, so it needs no guard; anything else does, or the log line throws
     * where the query would have succeeded.
     */
    private static CodeBlock singleValue(final TypeName type, final String name) {
        return type.isPrimitive()
                ? CodeBlock.of("$T.valueOf($N)", String.class, name)
                : CodeBlock.of("$N == null ? $S : $N.toString()", name, "null", name);
    }

    private static CodeBlock arrayOfValues(final TypeName type, final String name) {
        return type.isPrimitive()
                ? CodeBlock.of("$T.toString($N)", Arrays.class, name)
                : CodeBlock.of("$N == null ? $S : $T.toString($N)", name, "null", Arrays.class, name);
    }

    private CodeBlock.Builder prepareReturnList(final ParameterizedTypeName listOfResults, final ResultRowConverter converter) {
        final var template = CodeBlock.of("new $T()", ParameterizedTypeName.get(
                ClassName.get(ArrayList.class), listOfResults.typeArguments().getFirst()));
        return CodeBlock.builder()
                .addStatement(variables.inline(listOfResults, GeneratedNames.LIST, template))
                .add(controlFlows.whileHasNext())
                .addStatement("$N.add($N.$N($N))",
                        GeneratedNames.LIST,
                        converter.alias().orElseThrow(MissingConverterAliasException::new),
                        converter.methodName().orElseThrow(MissingConverterMethodNameException::new),
                        GeneratedNames.RESULT_SET)
                .endControlFlow();
    }

    @Override
    public CodeBlock returnAsMultiple(final ResultRowConverter converter) {
        return prepareReturnList(TypicalTypes.listOf(converter.resultTypeName()
                .orElseThrow(MissingConverterResultTypeException::new)), converter)
                .addStatement("return $N", GeneratedNames.LIST)
                .build();
    }

    @Override
    public CodeBlock returnAsSingle(final SqlConfiguration configuration) {
        final var converters = runtimeConfiguration.converter();
        final var converter = configuration.converter(converters::defaultConverter);
        final var builder = prepareReturnList(TypicalTypes.listOf(converter.resultTypeName()
                .orElseThrow(MissingConverterResultTypeException::new)), converter);
        if (configuration.throwOnMultipleResults().orElse(Boolean.FALSE)) {
            builder.beginControlFlow("if ($N.size() > 1)", GeneratedNames.LIST)
                    .addStatement("throw new $T()", IllegalStateException.class)
                    .endControlFlow();
        }
        // ofNullable, not of: a statement whose result is a single value can select a row holding
        // SQL NULL, and Optional.of would answer that with a NullPointerException from inside
        // generated code. A converter that builds a record never returns null, so nothing else
        // changes.
        return builder.addStatement("return $N.isEmpty() ? $T.empty() : $T.ofNullable($N.getFirst())",
                        GeneratedNames.LIST, Optional.class, Optional.class, GeneratedNames.LIST)
                .build();
    }

    @Override
    public CodeBlock streamStateful(final SqlConfiguration configuration) {
        final var converters = runtimeConfiguration.converter();
        final var converter = configuration.converter(converters::defaultConverter);
        return CodeBlock.builder()
                .addStatement("return $T.stream($L, false).onClose($L)",
                        StreamSupport.class,
                        lazyStreamSpliterator(converter),
                        lazyStreamCloser(configuration))
                .build();
    }


    private TypeSpec lazyStreamSpliterator(final ResultRowConverter converter) {
        final var spliteratorClass = ClassName.get(Spliterators.AbstractSpliterator.class);
        final var resultType = converter.resultTypeName()
                .orElseThrow(MissingConverterResultTypeException::new);
        final var superinterface = ParameterizedTypeName.get(spliteratorClass, resultType);
        final var consumerType = TypicalTypes.consumerOf(resultType);
        return TypeSpec
                .anonymousClassBuilder("$T.MAX_VALUE, $T.ORDERED", Long.class, Spliterator.class)
                .addSuperinterface(superinterface)
                .addMethod(methods.implementation("tryAdvance")
                        .addParameter(params.parameter(consumerType, GeneratedNames.ACTION))
                        .returns(boolean.class)
                        .addCode(controlFlows.startTryBlock())
                        .addCode(controlFlows.ifHasNext())
                        .addStatement("$N.accept($N.$N($N))", GeneratedNames.ACTION, converter.alias()
                                        .orElseThrow(MissingConverterAliasException::new),
                                converter.methodName().orElseThrow(MissingConverterMethodNameException::new),
                                GeneratedNames.RESULT_SET)
                        .addCode(blocks.returnTrue())
                        .addCode(controlFlows.endIf())
                        .addCode(blocks.returnFalse())
                        .addCode(controlFlows.endTryBlock())
                        .addCode(controlFlows.catchAndRethrow())
                        .build())
                .build();
    }

    private TypeSpec lazyStreamCloser(final SqlConfiguration configuration) {
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(Runnable.class)
                .addMethod(methods.implementation("run")
                        .returns(void.class)
                        .addCode(controlFlows.startTryBlock())
                        .addCode(closeResultSet())
                        .addCode(closePrepareStatement())
                        .addCode(closeConnection(configuration))
                        .addCode(controlFlows.endTryBlock())
                        .addCode(controlFlows.catchAndRethrow())
                        .build())
                .build();
    }

    /**
     * The query with one placeholder per value the caller passed, instead of the one placeholder the
     * author wrote.
     */
    private CodeBlock expandedQuery(final SqlStatement statement, final String constant) {
        final var config = statement.getConfiguration();
        final var builder = CodeBlock.builder();
        final var expanded = InLists.placeholderOrder(config).stream().filter(InLists::expands).toList();
        for (final var parameter : expanded) {
            final var name = parameter.name().orElseThrow(MissingParameterNameException::new);
            if (InLists.isNegated(statement.getRawStatement(), name)) {
                builder.beginControlFlow("if ($N.isEmpty())", name)
                        .addStatement("throw new $T($S)", IllegalArgumentException.class,
                                ("'" + name + "' must not be empty: 'not in' on an empty set matches every "
                                        + "row, and that cannot be written as a list of placeholders."))
                        .endControlFlow();
            }
        }
        final var expression = CodeBlock.builder().add("$N[0]", constant);
        for (var index = 0; index < expanded.size(); index++) {
            expression.add(" + $N($N.size()) + $N[$L]",
                    InLists.EXPANSION_METHOD,
                    expanded.get(index).name().orElseThrow(MissingParameterNameException::new),
                    constant,
                    index + 1);
        }
        builder.addStatement(variables.inline(String.class, GeneratedNames.QUERY, "$L", expression.build()))
                .add(logging.queryPicked(constant));
        if (logging.isEnabled()) {
            final var rawQuery = fields.constantRawSqlStatementFieldName(config);
            builder.addStatement(variables.inline(String.class, GeneratedNames.RAW_QUERY, "$N", rawQuery));
        }
        return builder.build();
    }

    @Override
    public CodeBlock setParameters(final SqlConfiguration config) {
        if (InLists.anyExpands(config)) {
            return expandedParameterAssignment(config);
        }
        return parameterAssignment(config, parameter -> CodeBlock.of("$N",
                parameter.name().orElseThrow(MissingParameterNameException::new)));
    }

    /**
     * Binds by counting rather than by looking an index up.
     *
     * <p>The fixed indices a statement is normally bound by say where each parameter sits in the
     * query, which stops being true the moment a collection turns one placeholder into as many as it
     * holds. Walking the placeholders in the order the statement writes them and counting along the
     * way needs no such table, and lands on the same indices when no collection is involved.</p>
     */
    private CodeBlock expandedParameterAssignment(final SqlConfiguration config) {
        final var builder = CodeBlock.builder();
        // Hoisted, and once per parameter rather than once per placeholder: a parameter named twice
        // is bound twice, and converting it twice would declare the same local twice.
        final var bound = new LinkedHashMap<String, CodeBlock>();
        for (final var parameter : config.parameters()) {
            final var name = parameter.name().orElseThrow(MissingParameterNameException::new);
            if (InLists.expands(parameter)) {
                continue;
            }
            final var conversion = parameterConversions.convert(parameter, CodeBlock.of("$N", name));
            conversion.ifPresent(converted -> builder.add(converted.declarations()));
            bound.put(name, conversion
                    .map(converted -> CodeBlock.of("$N", converted.boundName()))
                    .orElseGet(() -> CodeBlock.of("$N", name)));
        }
        builder.addStatement("$T $N = 1", TypeName.INT, GeneratedNames.JDBC_INDEX);
        for (final var parameter : InLists.placeholderOrder(config)) {
            final var name = parameter.name().orElseThrow(MissingParameterNameException::new);
            if (InLists.expands(parameter)) {
                final var element = name + GeneratedNames.ELEMENT_SUFFIX;
                // Inside the loop, because what it converts is the element the loop is holding.
                final var conversion = InLists.elementType(parameter).flatMap(type ->
                        parameterConversions.convert(type, element, CodeBlock.of("$N", element)));
                final var body = CodeBlock.builder();
                conversion.ifPresent(converted -> body.add(converted.declarations()));
                body.addStatement("$N.setObject($N++, $L)", GeneratedNames.STATEMENT,
                        GeneratedNames.JDBC_INDEX, conversion
                                .map(converted -> CodeBlock.of("$N", converted.boundName()))
                                .orElseGet(() -> CodeBlock.of("$N", element)));
                builder.add(controlFlows.forLoop(
                        code("final var $N : $N", element, name), body.build()));
            } else {
                builder.addStatement("$N.setObject($N++, $L)", GeneratedNames.STATEMENT,
                        GeneratedNames.JDBC_INDEX, bound.get(name));
            }
        }
        return builder.build();
    }

    @Override
    public CodeBlock setBatchParameters(final SqlConfiguration config) {
        return parameterAssignment(config, parameter -> CodeBlock.of("$N[$N]",
                parameter.name().orElseThrow(MissingParameterNameException::new), GeneratedNames.BATCH));
    }

    /**
     * Binds every parameter of a statement, converting the ones a driver would not take as they
     * were declared.
     *
     * <p>A conversion is hoisted in front of the index loop rather than repeated inside it: a
     * parameter named twice in one statement is bound twice, and unwrapping or allocating twice for
     * that would be work nobody asked for. A parameter needing no conversion emits exactly what it
     * always emitted.</p>
     */
    private CodeBlock parameterAssignment(
            final SqlConfiguration config,
            final Function<SqlParameter, CodeBlock> source) {
        final var builder = CodeBlock.builder();
        config.parameters().forEach(parameter -> {
            final var name = parameter.name().orElseThrow(MissingParameterNameException::new);
            final var conversion = parameterConversions.convert(parameter, source.apply(parameter));
            final var bound = conversion
                    .map(converted -> CodeBlock.of("$N", converted.boundName()))
                    .orElseGet(() -> source.apply(parameter));
            conversion.ifPresent(converted -> builder.add(converted.declarations()));
            builder.add(controlFlows.forLoop(
                    code("final int $N : $N.get($S)",
                            GeneratedNames.JDBC_INDEX,
                            GeneratedNames.INDEX,
                            name),
                    CodeBlock.builder()
                            .addStatement("$N.setObject($N, $L)",
                                    GeneratedNames.STATEMENT, GeneratedNames.JDBC_INDEX, bound)
                            .build()));
        });

        return builder.build();
    }

}
