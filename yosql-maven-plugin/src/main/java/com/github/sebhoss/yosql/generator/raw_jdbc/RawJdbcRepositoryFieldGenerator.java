package com.github.sebhoss.yosql.generator.raw_jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.sql.DataSource;

import com.github.sebhoss.yosql.generator.RepositoryFieldGenerator;
import com.github.sebhoss.yosql.generator.helpers.TypicalFields;
import com.github.sebhoss.yosql.generator.helpers.TypicalModifiers;
import com.github.sebhoss.yosql.generator.helpers.TypicalNames;
import com.github.sebhoss.yosql.generator.helpers.TypicalParameters;
import com.github.sebhoss.yosql.generator.helpers.TypicalTypes;
import com.github.sebhoss.yosql.model.ResultRowConverter;
import com.github.sebhoss.yosql.model.SqlParameter;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.github.sebhoss.yosql.model.SqlStatementConfiguration;
import com.github.sebhoss.yosql.model.SqlStatementType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;

public class RawJdbcRepositoryFieldGenerator implements RepositoryFieldGenerator {

    @Override
    public CodeBlock staticInitializer(final List<SqlStatement> statements) {
        final CodeBlock.Builder builder = CodeBlock.builder();
        statements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(SqlStatementConfiguration::hasParameters)
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
                        .map(RawJdbcRepositoryFieldGenerator::asConstantSqlField),
                statements.stream()
                        .filter(stmt -> stmt.getConfiguration().hasParameters())
                        .map(RawJdbcRepositoryFieldGenerator::asConstantSqlParameterIndexField));
        final Stream<FieldSpec> fields = Stream.concat(Stream.of(asDataSourceField()),
                converterFields(statements));
        return Stream.concat(constants, fields)
                .collect(Collectors.toList());
    }

    private static FieldSpec asConstantSqlField(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        return FieldSpec.builder(String.class, TypicalFields.constantSqlStatementFieldName(configuration))
                .addModifiers(TypicalModifiers.CONSTANT_FIELD)
                .initializer("$S", TypicalParameters.replaceNamedParameters(sqlStatement))
                .build();
    }

    private static FieldSpec asConstantSqlParameterIndexField(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        return FieldSpec.builder(TypicalTypes.MAP_OF_STRING_AND_NUMBERS,
                TypicalFields.constantSqlStatementParameterIndexFieldName(configuration))
                .addModifiers(TypicalModifiers.CONSTANT_FIELD)
                .initializer("new $T<>($L)", HashMap.class, sqlStatement.getConfiguration().getParameters().size())
                .build();
    }

    private static FieldSpec asDataSourceField() {
        return FieldSpec.builder(DataSource.class, TypicalNames.DATA_SOURCE)
                .addModifiers(TypicalModifiers.PRIVATE_FIELD)
                .build();
    }

    private Stream<FieldSpec> converterFields(final List<SqlStatement> sqlStatements) {
        return resultConverters(sqlStatements)
                .map(converter -> {
                    final ClassName converterClass = ClassName.bestGuess(converter.getConverterType());
                    return FieldSpec.builder(converterClass, converter.getAlias())
                            .addModifiers(TypicalModifiers.PRIVATE_FIELD)
                            .build();
                });
    }

    private Stream<ResultRowConverter> resultConverters(final List<SqlStatement> sqlStatements) {
        return sqlStatements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(config -> SqlStatementType.READING == config.getType())
                .map(SqlStatementConfiguration::getResultConverter)
                .filter(Objects::nonNull)
                .distinct();
    }

}
