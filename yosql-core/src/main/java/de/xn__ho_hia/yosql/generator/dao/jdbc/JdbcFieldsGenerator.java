/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.dao.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.sql.DataSource;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;

import de.xn__ho_hia.javapoet.TypeGuesser;
import de.xn__ho_hia.yosql.generator.api.FieldsGenerator;
import de.xn__ho_hia.yosql.generator.api.LoggingGenerator;
import de.xn__ho_hia.yosql.generator.helpers.TypicalFields;
import de.xn__ho_hia.yosql.generator.helpers.TypicalNames;
import de.xn__ho_hia.yosql.generator.helpers.TypicalParameters;
import de.xn__ho_hia.yosql.generator.helpers.TypicalTypes;
import de.xn__ho_hia.yosql.model.ResultRowConverter;
import de.xn__ho_hia.yosql.model.SqlConfiguration;
import de.xn__ho_hia.yosql.model.SqlParameter;
import de.xn__ho_hia.yosql.model.SqlStatement;
import de.xn__ho_hia.yosql.model.SqlType;

@SuppressWarnings("nls")
final class JdbcFieldsGenerator implements FieldsGenerator {

    private final TypicalFields    fields;
    private final LoggingGenerator logging;

    JdbcFieldsGenerator(
            final TypicalFields fields,
            final LoggingGenerator logging) {
        this.fields = fields;
        this.logging = logging;
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
                            .forEach(parameter -> addIndexArray(builder, parameter, config));
                });
        return builder.build();
    }

    private static void addIndexArray(final CodeBlock.Builder builder, final SqlParameter parameter,
            final SqlConfiguration config) {
        builder.addStatement("$N.put($S, $L)",
                TypicalFields.constantSqlStatementParameterIndexFieldName(config),
                parameter.getName(),
                indexArray(parameter));
    }

    private static String indexArray(final SqlParameter param) {
        return IntStream.of(param.getIndices())
                .boxed()
                .map(Object::toString)
                .collect(Collectors.joining(", ", "new int[] { ", " }"));
    }

    @Override
    public Iterable<FieldSpec> asFields(final List<SqlStatement> statementsInRepository) {
        final List<FieldSpec> repositoryFields = new ArrayList<>(statementsInRepository.size() * 2 + 2);

        repositoryFields.add(asDataSourceField());
        if (logging.isEnabled() && !statementsInRepository.isEmpty()) {
            // doesn't matter which statement we pick since they all end up in
            // the same repository anyway
            final SqlStatement firstStatement = statementsInRepository.get(0);
            loggerField(firstStatement).ifPresent(repositoryFields::add);
        }
        for (final SqlStatement statement : statementsInRepository) {
            if (logging.isEnabled()) {
                repositoryFields.add(asConstantRawSqlField(statement));
            }
            repositoryFields.add(asConstantSqlField(statement));
            if (statement.getConfiguration().hasParameters()) {
                repositoryFields.add(asConstantSqlParameterIndexField(statement));
            }
        }
        resultConverters(statementsInRepository)
                .map(this::asConverterField)
                .forEach(repositoryFields::add);

        return repositoryFields;
    }

    private Optional<FieldSpec> loggerField(final SqlStatement sqlStatement) {
        return logging.logger(ClassName.bestGuess(sqlStatement.getRepository()));
    }

    private FieldSpec asConstantRawSqlField(final SqlStatement sqlStatement) {
        final SqlConfiguration configuration = sqlStatement.getConfiguration();
        // TODO: add javadocs similar to methods
        return fields.prepareConstant(getClass(), String.class,
                TypicalFields.constantRawSqlStatementFieldName(configuration))
                .initializer("$S", sqlStatement.getRawStatement())
                .build();
    }

    private FieldSpec asConstantSqlField(final SqlStatement sqlStatement) {
        final SqlConfiguration configuration = sqlStatement.getConfiguration();
        final String rawStatement = sqlStatement.getRawStatement();
        final String statement = TypicalParameters.replaceNamedParameters(rawStatement);
        // TODO: add javadocs similar to methods
        return fields.prepareConstant(getClass(), String.class,
                TypicalFields.constantSqlStatementFieldName(configuration))
                .initializer("$S", statement)
                .build();
    }

    @SuppressWarnings("boxing")
    private FieldSpec asConstantSqlParameterIndexField(final SqlStatement sqlStatement) {
        final SqlConfiguration configuration = sqlStatement.getConfiguration();
        return fields.prepareConstant(getClass(), TypicalTypes.MAP_OF_STRING_AND_ARRAY_OF_INTS,
                TypicalFields.constantSqlStatementParameterIndexFieldName(configuration))
                .initializer("new $T<>($L)", HashMap.class, sqlStatement.getConfiguration().getParameters().size())
                .build();
    }

    private FieldSpec asDataSourceField() {
        return fields.field(getClass(), DataSource.class, TypicalNames.DATA_SOURCE);
    }

    private FieldSpec asConverterField(final ResultRowConverter converter) {
        return fields.field(
                getClass(),
                TypeGuesser.guessTypeName(converter.getConverterType()),
                converter.getAlias());
    }

    private static Stream<ResultRowConverter> resultConverters(final List<SqlStatement> sqlStatements) {
        return sqlStatements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(config -> SqlType.READING == config.getType() || SqlType.CALLING == config.getType())
                .map(SqlConfiguration::getResultRowConverter)
                .filter(Objects::nonNull)
                .distinct();
    }

}
