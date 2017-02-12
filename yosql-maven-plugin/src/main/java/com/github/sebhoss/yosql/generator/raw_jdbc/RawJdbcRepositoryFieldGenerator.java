package com.github.sebhoss.yosql.generator.raw_jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.sql.DataSource;

import com.github.sebhoss.yosql.generator.RepositoryFieldGenerator;
import com.github.sebhoss.yosql.generator.helpers.TypicalFields;
import com.github.sebhoss.yosql.generator.helpers.TypicalNames;
import com.github.sebhoss.yosql.generator.helpers.TypicalParameters;
import com.github.sebhoss.yosql.generator.helpers.TypicalTypes;
import com.github.sebhoss.yosql.model.ResultRowConverter;
import com.github.sebhoss.yosql.model.SqlConfiguration;
import com.github.sebhoss.yosql.model.SqlParameter;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.github.sebhoss.yosql.model.SqlType;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;

@Named
@Singleton
public class RawJdbcRepositoryFieldGenerator implements RepositoryFieldGenerator {

    private final TypicalFields fields;

    @Inject
    public RawJdbcRepositoryFieldGenerator(final TypicalFields fields) {
        this.fields = fields;
    }

    @Override
    public CodeBlock staticInitializer(final List<SqlStatement> statements) {
        final CodeBlock.Builder builder = CodeBlock.builder();
        statements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(SqlConfiguration::hasParameters)
                .forEach(config -> {
                    config.getParameters().stream()
                            .filter(SqlParameter::hasIndices)
                            .forEach(param -> builder.addStatement("$N.put($S, $L)",
                                    TypicalFields.constantSqlStatementParameterIndexFieldName(config),
                                    param.getName(),
                                    indexArray(param)));
                });
        return builder.build();
    }

    private static String indexArray(final SqlParameter param) {
        return IntStream.of(param.getIndices())
                .boxed()
                .map(Object::toString)
                .collect(Collectors.joining(", ", "new int[] { ", " }"));
    }

    @Override
    public Iterable<FieldSpec> asFields(final List<SqlStatement> statements) {
        final Stream<FieldSpec> constants = Stream.concat(
                statements.stream()
                        .map(this::asConstantSqlField),
                statements.stream()
                        .filter(stmt -> stmt.getConfiguration().hasParameters())
                        .map(this::asConstantSqlParameterIndexField));
        final Stream<FieldSpec> fields = Stream.concat(Stream.of(asDataSourceField()),
                converterFields(statements));
        return Stream.concat(constants, fields)
                .collect(Collectors.toList());
    }

    private FieldSpec asConstantSqlField(final SqlStatement sqlStatement) {
        final SqlConfiguration configuration = sqlStatement.getConfiguration();
        return fields.prepareConstant(getClass(), String.class,
                TypicalFields.constantSqlStatementFieldName(configuration))
                .initializer("$S", TypicalParameters.replaceNamedParameters(sqlStatement))
                .build();
    }

    private FieldSpec asConstantSqlParameterIndexField(final SqlStatement sqlStatement) {
        final SqlConfiguration configuration = sqlStatement.getConfiguration();
        return fields.prepareConstant(getClass(), TypicalTypes.MAP_OF_STRING_AND_NUMBERS,
                TypicalFields.constantSqlStatementParameterIndexFieldName(configuration))
                .initializer("new $T<>($L)", HashMap.class, sqlStatement.getConfiguration().getParameters().size())
                .build();
    }

    private FieldSpec asDataSourceField() {
        return fields.field(getClass(), DataSource.class, TypicalNames.DATA_SOURCE);
    }

    private Stream<FieldSpec> converterFields(final List<SqlStatement> sqlStatements) {
        return resultConverters(sqlStatements)
                .map(converter -> fields.field(
                        getClass(),
                        TypicalTypes.guessTypeName(converter.getConverterType()),
                        converter.getAlias()));
    }

    private Stream<ResultRowConverter> resultConverters(final List<SqlStatement> sqlStatements) {
        return sqlStatements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(config -> SqlType.READING == config.getType() || SqlType.CALLING == config.getType())
                .map(SqlConfiguration::getResultConverter)
                .filter(Objects::nonNull)
                .distinct();
    }

}
