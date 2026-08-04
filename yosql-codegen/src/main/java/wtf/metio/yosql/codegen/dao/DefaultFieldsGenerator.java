/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.models.configuration.GeneratedNames;
import com.palantir.javapoet.ArrayTypeName;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import wtf.metio.yosql.codegen.blocks.Fields;
import wtf.metio.yosql.codegen.blocks.Javadoc;
import wtf.metio.yosql.codegen.exceptions.*;
import wtf.metio.yosql.codegen.files.SqlStatementParser;
import wtf.metio.yosql.codegen.files.SqlText;
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.internals.jdk.Buckets;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import javax.sql.DataSource;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

/**
 * Default implementation of the {@link FieldsGenerator} interface.
 */
public final class DefaultFieldsGenerator implements FieldsGenerator {

    private static final String NAME_REGEX = "([a-z])([A-Z])";
    private static final String NAME_REPLACEMENT = "$1_$2";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    private final ConverterConfiguration converters;
    private final RepositoriesConfiguration repositories;
    private final LoggingGenerator logging;
    private final Javadoc javadoc;
    private final Fields fields;

    public DefaultFieldsGenerator(
            final ConverterConfiguration converters,
            final RepositoriesConfiguration repositories,
            final LoggingGenerator logging,
            final Javadoc javadoc,
            final Fields fields) {
        this.converters = converters;
        this.repositories = repositories;
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
                .filter(not(InLists::anyExpands))
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
                parameter.name().orElseThrow(MissingParameterNameException::new),
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

        if (RepositoryConnections.needsDataSource(repositories, statements)) {
            repositoryFields.add(fields.field(DataSource.class, GeneratedNames.DATA_SOURCE));
        }

        if (logging.isEnabled()) {
            statements.stream()
                    .map(SqlStatement::getRepositoryClass)
                    .map(ClassName::bestGuess)
                    .flatMap(clazz -> logging.logger(clazz).stream())
                    .findAny()
                    .ifPresent(repositoryFields::add);
        }
        for (final var statement : statements) {
            if (logging.isEnabled()) {
                repositoryFields.add(asConstantRawSqlField(statement));
            }
            repositoryFields.add(asConstantSqlField(statement));
            // A statement that expands a collection counts its placeholders as it binds them, so the
            // fixed indices would only be a constant nothing reads.
            if (Buckets.hasEntries(statement.getConfiguration().parameters())
                    && !InLists.anyExpands(statement.getConfiguration())) {
                repositoryFields.add(asConstantSqlParameterIndexField(statement));
            }
        }
        ensureAliasesAreUnique(SqlStatement.resultConverters(statements, defaultConverter()).toList())
                .stream()
                .map(this::asConverterField)
                .forEach(repositoryFields::add);

        return repositoryFields;
    }

    private ResultRowConverter defaultConverter() {
        return converters.defaultConverter().orElseThrow(MissingDefaultConverterException::new);
    }

    /**
     * A converter's field is named after its class, so two converter classes with the same simple
     * name would be held in one field. Saying so here beats emitting a class that cannot compile.
     */
    private static List<ResultRowConverter> ensureAliasesAreUnique(final List<ResultRowConverter> converters) {
        final var byAlias = new LinkedHashMap<String, List<String>>();
        for (final var converter : converters) {
            byAlias.computeIfAbsent(converter.alias().orElse(""), alias -> new ArrayList<>())
                    .add(converter.converterType().orElse(""));
        }
        byAlias.forEach((alias, types) -> {
            if (types.size() > 1) {
                throw new DuplicateConverterAliasException(alias, types);
            }
        });
        return converters;
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
        if (InLists.anyExpands(configuration)) {
            return asConstantSqlPartsField(sqlStatement, rawStatement);
        }
        final var statement = replaceNamedParameters(rawStatement);
        return fields.prepareConstant(String.class, constantSqlStatementFieldName(configuration))
                .initializer(fields.initialize(statement))
                .addJavadoc(javadoc.fieldJavaDoc(sqlStatement))
                .build();
    }

    /**
     * The query either side of each collection, so that the placeholders the caller's values need can
     * be put between them.
     */
    private FieldSpec asConstantSqlPartsField(final SqlStatement sqlStatement, final String rawStatement) {
        final var configuration = sqlStatement.getConfiguration();
        final var parts = InLists.queryParts(configuration, rawStatement);
        final var initializer = CodeBlock.builder().add("{");
        for (var index = 0; index < parts.size(); index++) {
            initializer.add(index == 0 ? "\n" : ",\n").add(fields.initialize(parts.get(index)));
        }
        return fields.prepareConstant(ArrayTypeName.of(String.class),
                        constantSqlStatementFieldName(configuration))
                .initializer(initializer.add("}").build())
                .addJavadoc(javadoc.fieldJavaDoc(sqlStatement))
                .build();
    }

    /**
     * Turns the names the author wrote into the placeholders a driver understands — but only the
     * ones outside string literals and comments, which is why the matching runs over the mask and
     * the rewriting over the statement itself.
     */
    private static String replaceNamedParameters(final String rawSqlStatement) {
        final var matcher = SqlStatementParser.NAMED_PARAMETER_PATTERN
                .matcher(SqlText.maskLiteralsAndComments(rawSqlStatement));
        final var statement = new StringBuilder();
        var last = 0;
        while (matcher.find()) {
            statement.append(rawSqlStatement, last, matcher.start()).append('?');
            last = matcher.end();
        }
        return statement.append(rawSqlStatement.substring(last)).toString();
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
                converter.converterTypeName().orElseThrow(MissingConverterTypeNameException::new),
                converter.alias().orElseThrow(MissingConverterAliasException::new));
    }

    @Override
    public String constantSqlStatementFieldName(final SqlConfiguration configuration) {
        return configuration.name()
                .map(name -> NAME_PATTERN.matcher(name).replaceAll(NAME_REPLACEMENT))
                .map(name -> name.toUpperCase(Locale.ROOT))
                .map(name -> name + vendorSuffix(configuration))
                .orElseThrow(MissingSqlConfigurationNameException::new);
    }

    @Override
    public String constantRawSqlStatementFieldName(final SqlConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + GeneratedNames.RAW_SUFFIX;
    }

    @Override
    public String constantSqlStatementParameterIndexFieldName(final SqlConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + GeneratedNames.INDEX_SUFFIX;
    }

    private static String vendorSuffix(final SqlConfiguration configuration) {
        return configuration.vendor()
                .filter(not(Strings::isBlank))
                .map(vendor -> "_" + vendor.replace(" ", "_").toUpperCase(Locale.ROOT))
                .orElse("");
    }

}
