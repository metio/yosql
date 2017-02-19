package com.github.sebhoss.yosql.generator.raw_jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.sql.DataSource;

import com.github.sebhoss.yosql.generator.LoggingGenerator;
import com.github.sebhoss.yosql.generator.RepositoryFieldGenerator;
import com.github.sebhoss.yosql.generator.helpers.TypicalFields;
import com.github.sebhoss.yosql.generator.helpers.TypicalNames;
import com.github.sebhoss.yosql.generator.helpers.TypicalParameters;
import com.github.sebhoss.yosql.generator.helpers.TypicalTypes;
import com.github.sebhoss.yosql.generator.logging.DelegatingLoggingGenerator;
import com.github.sebhoss.yosql.model.ResultRowConverter;
import com.github.sebhoss.yosql.model.SqlConfiguration;
import com.github.sebhoss.yosql.model.SqlParameter;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.github.sebhoss.yosql.model.SqlType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;

@Named
@Singleton
public class RawJdbcRepositoryFieldGenerator implements RepositoryFieldGenerator {

    private final TypicalFields    fields;
    private final LoggingGenerator logging;

    @Inject
    public RawJdbcRepositoryFieldGenerator(
            final TypicalFields fields,
            final DelegatingLoggingGenerator logging) {
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

    private void addIndexArray(final CodeBlock.Builder builder, final SqlParameter parameter,
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
        return fields.prepareConstant(getClass(), String.class,
                TypicalFields.constantRawSqlStatementFieldName(configuration))
                .initializer("$S", sqlStatement.getRawStatement())
                .build();
    }

    private FieldSpec asConstantSqlField(final SqlStatement sqlStatement) {
        final SqlConfiguration configuration = sqlStatement.getConfiguration();
        final String rawStatement = sqlStatement.getRawStatement();
        final String statement = TypicalParameters.replaceNamedParameters(rawStatement);
        final String fieldValue = SqlType.CALLING == configuration.getType() ? String.format("{call %s}", statement)
                : statement;
        return fields.prepareConstant(getClass(), String.class,
                TypicalFields.constantSqlStatementFieldName(configuration))
                .initializer("$S", fieldValue)
                .build();
    }

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
                TypicalTypes.guessTypeName(converter.getConverterType()),
                converter.getAlias());
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
