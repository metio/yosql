/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import wtf.metio.yosql.codegen.blocks.Fields;
import wtf.metio.yosql.codegen.blocks.Javadoc;
import wtf.metio.yosql.codegen.files.SqlStatementParser;
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.internals.jdk.Buckets;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

/**
 * Default implementation of the {@link FieldsGenerator} interface.
 */
public final class DefaultFieldsGenerator implements FieldsGenerator {

    private static final String NAME_REGEX = "([a-z])([A-Z])";
    private static final String NAME_REPLACEMENT = "$1_$2";

    private final ConverterConfiguration converters;
    private final NamesConfiguration names;
    private final LoggingGenerator logging;
    private final Javadoc javadoc;
    private final Fields fields;

    public DefaultFieldsGenerator(
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
                constantSqlStatementParameterIndexFieldName(config),
                parameter.name().orElseThrow(), // TODO: throw business exception
                indexArray(parameter));
    }

    private static String indexArray(final SqlParameter param) {
        return param.indices()
                .stream()
                .map(IntStream::of)
                .flatMap(IntStream::boxed)
                .map(Object::toString)
                .collect(Collectors.joining(", ", "new int[] { ", " }"));
    }

    @Override
    public Iterable<FieldSpec> asFields(final List<SqlStatement> statements) {
        final var repositoryFields = new ArrayList<FieldSpec>(statements.size() * 2 + 2);

        repositoryFields.add(fields.field(DataSource.class, names.dataSource()));
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
        SqlStatement.resultConverters(statements, converters.defaultConverter().orElseThrow())
                .map(this::asConverterField)
                .forEach(repositoryFields::add);

        return repositoryFields;
    }

    private Optional<FieldSpec> loggerField(final SqlStatement sqlStatement) {
        return logging.logger(ClassName.bestGuess(sqlStatement.getRepositoryClass()));
    }

    private FieldSpec asConstantRawSqlField(final SqlStatement sqlStatement) {
        final var configuration = sqlStatement.getConfiguration();
        final var rawStatement = sqlStatement.getRawStatement();
        return fields.prepareConstant(String.class, constantRawSqlStatementFieldName(configuration))
                .initializer(fields.initialize(rawStatement))
                .addJavadoc(javadoc.fieldJavaDoc(sqlStatement))
                .build();
    }

    private FieldSpec asConstantSqlField(final SqlStatement sqlStatement) {
        final var configuration = sqlStatement.getConfiguration();
        final var rawStatement = sqlStatement.getRawStatement();
        final var statement = replaceNamedParameters(rawStatement);
        return fields.prepareConstant(String.class, constantSqlStatementFieldName(configuration))
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
                        constantSqlStatementParameterIndexFieldName(configuration))
                .initializer("new $T<>($L)", HashMap.class, sqlStatement.getConfiguration().parameters().size())
                .build();
    }

    private FieldSpec asConverterField(final ResultRowConverter converter) {
        return fields.field(
                converter.converterTypeName().orElseThrow(), // TODO: throw business exception
                converter.alias().orElseThrow()); // TODO: throw business exception
    }

    @Override
    public String constantSqlStatementFieldName(final SqlConfiguration configuration) {
        return configuration.name()
                .map(name -> name.replaceAll(NAME_REGEX, NAME_REPLACEMENT))
                .map(name -> name.toUpperCase(Locale.ROOT))
                .map(name -> name + vendorSuffix(configuration))
                .orElseThrow();
    }

    @Override
    public String constantRawSqlStatementFieldName(final SqlConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + names.rawSuffix();
    }

    @Override
    public String constantSqlStatementParameterIndexFieldName(final SqlConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + names.indexSuffix();
    }

    private static String vendorSuffix(final SqlConfiguration configuration) {
        return configuration.vendor()
                .filter(not(Strings::isBlank))
                .map(vendor -> "_" + vendor.replace(" ", "_").toUpperCase(Locale.ROOT))
                .orElse("");
    }

}
