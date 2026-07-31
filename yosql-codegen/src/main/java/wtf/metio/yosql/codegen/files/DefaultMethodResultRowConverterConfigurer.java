/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import com.squareup.javapoet.ClassName;
import wtf.metio.yosql.codegen.records.RecordConverterNames;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public final class DefaultMethodResultRowConverterConfigurer implements MethodResultRowConverterConfigurer {

    private final ConverterConfiguration converters;
    private final RecordConverterNames recordConverters;

    public DefaultMethodResultRowConverterConfigurer(
            final ConverterConfiguration converters,
            final RecordConverterNames recordConverters) {
        this.converters = converters;
        this.recordConverters = recordConverters;
    }

    @Override
    public SqlConfiguration configureResultRowConverter(final SqlConfiguration configuration) {
        return SqlConfiguration.copyOf(configuration)
                .withResultRowConverter(configuration.resultRowConverter()
                        .map(this::setMissingValuesFromRuntimeConfig)
                        .filter(DefaultMethodResultRowConverterConfigurer::ensureConverterIsFullyConfigured)
                        .or(() -> generatedRecordConverter(configuration))
                        .or(this::getDefaultRowConverter));
    }

    /**
     * Points the statement at the converter that will be generated for its {@code resultRowType}.
     * The repository then treats it like any other converter — a field of that type, called by that
     * name — which is why naming a record needs no other configuration.
     *
     * <p>A statement that names both a converter and a record type keeps the converter: naming a
     * converter names the exact code to call, and there is nothing to infer.</p>
     */
    private Optional<ResultRowConverter> generatedRecordConverter(final SqlConfiguration configuration) {
        return configuration.resultRowType()
                .map(String::strip)
                .filter(Predicate.not(String::isEmpty))
                .map(ClassName::bestGuess)
                .map(type -> ResultRowConverter.builder()
                        .setAlias(recordConverters.alias(type))
                        .setConverterType(recordConverters.converterClass(type).toString())
                        .setMethodName(recordConverters.methodName())
                        .setResultType(type.toString())
                        .build());
    }

    private static boolean ensureConverterIsFullyConfigured(final ResultRowConverter converter) {
        return converter.alias().isPresent()
                && converter.converterType().isPresent()
                && converter.methodName().isPresent()
                && converter.resultType().isPresent();
    }

    private ResultRowConverter setMissingValuesFromRuntimeConfig(final ResultRowConverter original) {
        return ResultRowConverter.builder()
                .setAlias(original.alias().or(() -> getAliasFromRuntimeConfig(original)))
                .setConverterType(original.converterType().or(() -> getConverterTypeFromRuntimeConfig(original)))
                .setMethodName(original.methodName().or(() -> getMethodNameFromRuntimeConfig(original)))
                .setResultType(original.resultType().or(() -> getResultTypeFromRuntimeConfig(original)))
                .build();
    }

    private Optional<ResultRowConverter> getDefaultRowConverter() {
        final var defaultConverter = converters.defaultConverter();
        return converters.rowConverters().stream()
                .filter(converter -> defaultConverter.isEmpty() || defaultConverter.get().equals(converter))
                .findFirst()
                .or(() -> defaultConverter);
    }

    private Optional<String> getAliasFromRuntimeConfig(final ResultRowConverter resultConverter) {
        return getConverterFieldFromRuntimeConfig(
                converter -> converterTypeMatches(resultConverter, converter),
                ResultRowConverter::alias);
    }

    private Optional<String> getConverterTypeFromRuntimeConfig(final ResultRowConverter resultConverter) {
        return getConverterFieldFromRuntimeConfig(
                converter -> aliasMatches(resultConverter, converter),
                ResultRowConverter::converterType);
    }

    private Optional<String> getResultTypeFromRuntimeConfig(final ResultRowConverter resultConverter) {
        return getConverterFieldFromRuntimeConfig(
                converter -> aliasMatches(resultConverter, converter)
                        || converterTypeMatches(resultConverter, converter),
                ResultRowConverter::resultType);
    }

    private Optional<String> getMethodNameFromRuntimeConfig(final ResultRowConverter resultConverter) {
        return getConverterFieldFromRuntimeConfig(
                converter -> aliasMatches(resultConverter, converter)
                        || converterTypeMatches(resultConverter, converter),
                ResultRowConverter::methodName);
    }

    private Optional<String> getConverterFieldFromRuntimeConfig(
            final Predicate<ResultRowConverter> predicate,
            final Function<ResultRowConverter, Optional<String>> mapper) {
        return converters.rowConverters().stream()
                .filter(predicate)
                .map(mapper)
                .flatMap(Optional::stream)
                .findFirst();
    }

    private static boolean aliasMatches(
            final ResultRowConverter resultConverter,
            final ResultRowConverter converter) {
        return converter.alias().equals(resultConverter.alias());
    }

    private static boolean converterTypeMatches(
            final ResultRowConverter resultConverter,
            final ResultRowConverter converter) {
        return converter.converterType().equals(resultConverter.converterType());
    }

}
