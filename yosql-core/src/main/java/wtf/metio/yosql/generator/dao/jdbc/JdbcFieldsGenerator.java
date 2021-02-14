/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.dao.jdbc;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.files.SqlFileParser;
import wtf.metio.yosql.generator.api.FieldsGenerator;
import wtf.metio.yosql.generator.api.LoggingGenerator;
import wtf.metio.yosql.generator.blocks.generic.Fields;
import wtf.metio.yosql.generator.blocks.generic.Javadoc;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcFields;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcNames;
import wtf.metio.yosql.generator.helpers.TypicalTypes;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.sql.*;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

final class JdbcFieldsGenerator implements FieldsGenerator {

    private final Fields fields;
    private final LoggingGenerator logging;
    private final JdbcFields jdbcFields;
    private final JdbcNames jdbcNames;
    private final Javadoc javadoc;
    private final RuntimeConfiguration runtimeConfiguration;

    JdbcFieldsGenerator(
            final Fields fields,
            final LoggingGenerator logging,
            final JdbcFields jdbcFields,
            final JdbcNames jdbcNames,
            final Javadoc javadoc,
            final RuntimeConfiguration runtimeConfiguration) {
        this.fields = fields;
        this.logging = logging;
        this.jdbcFields = jdbcFields;
        this.jdbcNames = jdbcNames;
        this.javadoc = javadoc;
        this.runtimeConfiguration = runtimeConfiguration;
    }

    @Override
    public CodeBlock staticInitializer(final List<SqlStatement> statements) {
        final var builder = CodeBlock.builder();
        statements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(SqlConfiguration::hasParameters)
                .forEach(config -> config.getParameters().stream()
                        .filter(SqlParameter::hasIndices)
                        .forEach(parameter -> addIndexArray(builder, parameter, config)));
        return builder.build();
    }

    private void addIndexArray(
            final CodeBlock.Builder builder,
            final SqlParameter parameter,
            final SqlConfiguration config) {
        builder.addStatement("$N.put($S, $L)",
                jdbcFields.constantSqlStatementParameterIndexFieldName(config),
                parameter.name(),
                indexArray(parameter));
    }

    private static String indexArray(final SqlParameter param) {
        return IntStream.of(param.indices())
                .boxed()
                .map(Object::toString)
                .collect(Collectors.joining(", ", "new int[] { ", " }"));
    }

    @Override
    public Iterable<FieldSpec> asFields(final List<SqlStatement> statements) {
        final var repositoryFields = new ArrayList<FieldSpec>(statements.size() * 2 + 2);

        repositoryFields.add(asDataSourceField());
        if (logging.isEnabled() && !statements.isEmpty()) {
            // doesn't matter which statement we pick since they all end up in the same repository anyway
            final var firstStatement = statements.get(0);
            loggerField(firstStatement).ifPresent(repositoryFields::add);
        }
        for (final var statement : statements) {
            if (logging.isEnabled()) {
                repositoryFields.add(asConstantRawSqlField(statement));
            }
            repositoryFields.add(asConstantSqlField(statement));
            if (statement.getConfiguration().hasParameters()) {
                repositoryFields.add(asConstantSqlParameterIndexField(statement));
            }
        }
        resultConverters(statements)
                .map(this::asConverterField)
                .forEach(repositoryFields::add);

        return repositoryFields;
    }

    private Optional<FieldSpec> loggerField(final SqlStatement sqlStatement) {
        return logging.logger(ClassName.bestGuess(sqlStatement.getRepository()));
    }

    private FieldSpec asConstantRawSqlField(final SqlStatement sqlStatement) {
        final var configuration = sqlStatement.getConfiguration();
        return fields.prepareConstant(String.class,
                jdbcFields.constantRawSqlStatementFieldName(configuration))
                .initializer("$S", sqlStatement.getRawStatement())
                .addJavadoc(javadoc.fieldJavaDoc(sqlStatement))
                .build();
    }

    private FieldSpec asConstantSqlField(final SqlStatement sqlStatement) {
        final var configuration = sqlStatement.getConfiguration();
        final var rawStatement = sqlStatement.getRawStatement();
        final var statement = replaceNamedParameters(rawStatement);
        return fields.prepareConstant(String.class,
                jdbcFields.constantSqlStatementFieldName(configuration))
                .initializer("$S", statement)
                .addJavadoc(javadoc.fieldJavaDoc(sqlStatement))
                .build();
    }

    private static String replaceNamedParameters(final String rawSqlStatement) {
        return rawSqlStatement.replaceAll(SqlFileParser.PARAMETER_PATTERN.pattern(), "?");
    }

    private FieldSpec asConstantSqlParameterIndexField(final SqlStatement sqlStatement) {
        final var configuration = sqlStatement.getConfiguration();
        return fields.prepareConstant(TypicalTypes.MAP_OF_STRING_AND_ARRAY_OF_INTS,
                jdbcFields.constantSqlStatementParameterIndexFieldName(configuration))
                .initializer("new $T<>($L)", HashMap.class, sqlStatement.getConfiguration().getParameters().size())
                .build();
    }

    private FieldSpec asDataSourceField() {
        return fields.field(DataSource.class, jdbcNames.dataSource());
    }

    private static String alias(final String rawAlias) {
        return rawAlias.endsWith("Converter") ? rawAlias : rawAlias + "Converter";
    }

    private FieldSpec asConverterField(final ResultRowConverter converter) {
        return runtimeConfiguration.converter().converters().stream()
                .filter(rowConverter -> rowConverter.alias().equals(converter.alias()))
                .map(rowConverter -> TypeGuesser.guessTypeName(rowConverter.converterType()))
                .map(typeName -> fields.field(
                        typeName,
                        alias(converter.alias())))
                .findFirst()
                .or(() -> Optional.ofNullable(converter.converterType())
                        .map(String::strip)
                        .filter(not(String::isBlank))
                        .map(type -> fields.field(
                                TypeGuesser.guessTypeName(type),
                                alias(converter.alias()))))
                .orElseGet(() -> fields.field(
                        TypeGuesser.guessTypeName("java.lang.Object"),
                        alias(converter.alias())));
    }

    private static Stream<ResultRowConverter> resultConverters(final List<SqlStatement> statements) {
        return statements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(config -> SqlType.READING == config.getType() || SqlType.CALLING == config.getType())
                .map(SqlConfiguration::getResultRowConverter)
                .filter(Objects::nonNull)
                .distinct();
    }

}
