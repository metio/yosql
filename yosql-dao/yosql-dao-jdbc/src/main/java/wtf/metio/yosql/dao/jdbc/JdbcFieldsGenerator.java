/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dao.jdbc;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.codegen.api.Fields;
import wtf.metio.yosql.codegen.api.FieldsGenerator;
import wtf.metio.yosql.codegen.api.Javadoc;
import wtf.metio.yosql.codegen.files.SqlStatementParser;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.internals.jdk.Buckets;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.sql.SqlType;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;
import wtf.metio.yosql.models.sql.ResultRowConverter;
import wtf.metio.yosql.models.sql.SqlParameter;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * JDBC implementation of the {@link FieldsGenerator} interface.
 */
public final class JdbcFieldsGenerator implements FieldsGenerator {

    private final ConverterConfiguration converters;
    private final NamesConfiguration names;
    private final LoggingGenerator logging;
    private final Javadoc javadoc;
    private final Fields fields;

    public JdbcFieldsGenerator(
            final ConverterConfiguration converters,
            final NamesConfiguration names,
            final LoggingGenerator logging,
            final Javadoc javadoc,
            final Fields fields) {
        this.converters = converters;
        this.names = names;
        this.logging = logging;
        this.javadoc = javadoc;
        this.fields = fields;
    }

    @Override
    public Optional<CodeBlock> staticInitializer(final List<SqlStatement> statements) {
        final var builder = CodeBlock.builder();
        statements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(config -> Buckets.hasEntries(config.parameters()))
                .forEach(config -> config.parameters().stream()
                        .filter(SqlParameter::hasIndices)
                        .forEach(parameter -> addIndexArray(builder, parameter, config)));
        return Optional.of(builder.build());
    }

    private void addIndexArray(
            final CodeBlock.Builder builder,
            final SqlParameter parameter,
            final SqlConfiguration config) {
        builder.addStatement("$N.put($S, $L)",
                fields.constantSqlStatementParameterIndexFieldName(config),
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
            if (Buckets.hasEntries(statement.getConfiguration().parameters())) {
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
        final var rawStatement = sqlStatement.getRawStatement();
        return fields.prepareConstant(String.class, fields.constantRawSqlStatementFieldName(configuration))
                .initializer(fields.initialize(rawStatement))
                .addJavadoc(javadoc.fieldJavaDoc(sqlStatement))
                .build();
    }

    private FieldSpec asConstantSqlField(final SqlStatement sqlStatement) {
        final var configuration = sqlStatement.getConfiguration();
        final var rawStatement = sqlStatement.getRawStatement();
        final var statement = replaceNamedParameters(rawStatement);
        return fields.prepareConstant(String.class, fields.constantSqlStatementFieldName(configuration))
                .initializer(fields.initialize(statement))
                .addJavadoc(javadoc.fieldJavaDoc(sqlStatement))
                .build();
    }

    private static String replaceNamedParameters(final String rawSqlStatement) {
        return rawSqlStatement.replaceAll(SqlStatementParser.NAMED_PARAMETER_PATTERN.pattern(), "?");
    }

    private FieldSpec asConstantSqlParameterIndexField(final SqlStatement sqlStatement) {
        final var configuration = sqlStatement.getConfiguration();
        return fields.prepareConstant(TypicalTypes.MAP_OF_STRING_AND_ARRAY_OF_INTS,
                        fields.constantSqlStatementParameterIndexFieldName(configuration))
                .initializer("new $T<>($L)", HashMap.class, sqlStatement.getConfiguration().parameters().size())
                .build();
    }

    private FieldSpec asDataSourceField() {
        return fields.field(DataSource.class, names.dataSource());
    }

    private FieldSpec asConverterField(final ResultRowConverter converter) {
        return converters.rowConverters().stream()
                .filter(rowConverter -> rowConverter.alias().equals(converter.alias()))
                .map(rowConverter -> TypeGuesser.guessTypeName(rowConverter.converterType()))
                .map(typeName -> fields.field(
                        typeName,
                        converter.alias()))
                .findFirst()
                .or(() -> converters.defaultConverter()
                        .map(ResultRowConverter::converterType)
                        .map(String::strip)
                        .filter(not(String::isBlank))
                        .map(type -> fields.field(
                                TypeGuesser.guessTypeName(type),
                                converter.alias())))
                .orElseGet(() -> fields.field(
                        TypeGuesser.guessTypeName("java.lang.Object"),
                        converter.alias()));
    }

    private static Stream<ResultRowConverter> resultConverters(final List<SqlStatement> statements) {
        return statements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(config -> SqlType.READING == config.type() || SqlType.CALLING == config.type())
                .flatMap(config -> config.resultRowConverter().stream())
                .filter(Objects::nonNull)
                .distinct();
    }

}
