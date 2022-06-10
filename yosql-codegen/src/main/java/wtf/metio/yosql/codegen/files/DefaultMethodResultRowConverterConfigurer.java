/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public final class DefaultMethodResultRowConverterConfigurer implements MethodResultRowConverterConfigurer {

    private final ConverterConfiguration converters;

    public DefaultMethodResultRowConverterConfigurer(final ConverterConfiguration converters) {
        this.converters = converters;
    }

    @Override
    public SqlConfiguration configureResultRowConverter(final SqlConfiguration configuration) {
        return SqlConfiguration.copyOf(configuration)
                .withResultRowConverter(configuration.resultRowConverter()
                        .map(this::adjustConverter)
                        .or(this::getDefaultRowConverter));
    }

    private ResultRowConverter adjustConverter(final ResultRowConverter original) {
        return ResultRowConverter.builder()
                .setAlias(original.alias().orElse(getDefaultAlias(original)))
                .setConverterType(original.converterType().orElse(getDefaultConverterType(original)))
                .setMethodName(original.methodName().orElse(getDefaultMethodName(original)))
                .setResultType(original.resultType().orElse(getDefaultResultType(original)))
                .build();
    }

    private Optional<ResultRowConverter> getDefaultRowConverter() {
        final var defaultConverter = converters.defaultConverter();
        return converters.rowConverters().stream()
                .filter(converter -> defaultConverter.isEmpty() || defaultConverter.get().equals(converter))
                .findFirst()
                .or(() -> defaultConverter);
    }

    private String getDefaultAlias(final ResultRowConverter resultConverter) {
        return getConverterFieldOrEmptyString(
                converter -> converterTypeMatches(resultConverter, converter),
                ResultRowConverter::alias);
    }

    private String getDefaultConverterType(final ResultRowConverter resultConverter) {
        return getConverterFieldOrEmptyString(
                converter -> aliasMatches(resultConverter, converter),
                ResultRowConverter::converterType);
    }

    private String getDefaultResultType(final ResultRowConverter resultConverter) {
        return getConverterFieldOrEmptyString(
                converter -> aliasMatches(resultConverter, converter)
                        || converterTypeMatches(resultConverter, converter),
                ResultRowConverter::resultType);
    }

    private String getDefaultMethodName(final ResultRowConverter resultConverter) {
        return getConverterFieldOrEmptyString(
                converter -> aliasMatches(resultConverter, converter)
                        || converterTypeMatches(resultConverter, converter),
                ResultRowConverter::methodName);
    }

    private String getConverterFieldOrEmptyString(
            final Predicate<ResultRowConverter> predicate,
            final Function<ResultRowConverter, Optional<String>> mapper) {
        return converters.rowConverters().stream()
                .filter(predicate)
                .map(mapper)
                .flatMap(Optional::stream)
                .findFirst()
                .orElse("");
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
