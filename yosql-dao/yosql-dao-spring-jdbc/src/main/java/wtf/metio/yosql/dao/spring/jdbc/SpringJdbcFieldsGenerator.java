/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.spring.jdbc;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import de.xn__ho_hia.javapoet.TypeGuesser;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import wtf.metio.yosql.codegen.api.Fields;
import wtf.metio.yosql.codegen.api.FieldsGenerator;
import wtf.metio.yosql.codegen.api.Javadoc;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.sql.SqlType;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class SpringJdbcFieldsGenerator implements FieldsGenerator {

    private final Fields fields;
    private final LoggingGenerator logging;
    private final Javadoc javadoc;
    private final ConverterConfiguration converters;
    private final NamesConfiguration names;

    public SpringJdbcFieldsGenerator(
            final Fields fields,
            final LoggingGenerator logging,
            final Javadoc javadoc,
            final ConverterConfiguration converters,
            final NamesConfiguration names) {
        this.fields = fields;
        this.logging = logging;
        this.javadoc = javadoc;
        this.converters = converters;
        this.names = names;
    }

    @Override
    public Optional<CodeBlock> staticInitializer(final List<SqlStatement> statements) {
        return Optional.empty();
    }

    @Override
    public Iterable<FieldSpec> asFields(final List<SqlStatement> statements) {
        final var repositoryFields = new ArrayList<FieldSpec>(statements.size() * 2 + 2);

        repositoryFields.add(jdbcTemplateField());
        if (logging.isEnabled() && !statements.isEmpty()) {
            // doesn't matter which statement we pick since they all end up in the same repository anyway
            final var firstStatement = statements.get(0);
            loggerField(firstStatement).ifPresent(repositoryFields::add);
        }
        for (final var statement : statements) {
            if (logging.isEnabled()) {
                repositoryFields.add(constantRawSqlField(statement));
            }
            repositoryFields.add(constantSqlField(statement));
        }
        resultConverters(statements)
                .map(this::converterField)
                .forEach(repositoryFields::add);

        return repositoryFields;
    }

    private Optional<FieldSpec> loggerField(final SqlStatement sqlStatement) {
        return logging.logger(ClassName.bestGuess(sqlStatement.getRepository()));
    }

    private FieldSpec jdbcTemplateField() {
        return fields.field(NamedParameterJdbcTemplate.class, names.jdbcTemplate());
    }

    private FieldSpec constantRawSqlField(final SqlStatement sqlStatement) {
        final var configuration = sqlStatement.getConfiguration();
        final var rawStatement = sqlStatement.getRawStatement();
        return fields.prepareConstant(String.class,
                        fields.constantRawSqlStatementFieldName(configuration))
                .initializer(fields.initialize(rawStatement))
                .addJavadoc(javadoc.fieldJavaDoc(sqlStatement))
                .build();
    }

    private FieldSpec constantSqlField(final SqlStatement sqlStatement) {
        final var configuration = sqlStatement.getConfiguration();
        final var rawStatement = sqlStatement.getRawStatement();
        return fields.prepareConstant(String.class,
                        fields.constantSqlStatementFieldName(configuration))
                .initializer(fields.initialize(rawStatement))
                .addJavadoc(javadoc.fieldJavaDoc(sqlStatement))
                .build();
    }

    private static Stream<ResultRowConverter> resultConverters(final List<SqlStatement> statements) {
        return statements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(config -> SqlType.READING == config.type() || SqlType.CALLING == config.type())
                .flatMap(config -> config.resultRowConverter().stream())
                .filter(Objects::nonNull)
                .distinct();
    }

    private FieldSpec converterField(final ResultRowConverter converter) {
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

}
