/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public final class DefaultMethodConverterConfigurer implements MethodConverterConfigurer {

    private final ConverterConfiguration converter;

    public DefaultMethodConverterConfigurer(final ConverterConfiguration converter) {
        this.converter = converter;
    }

    @Override
    public SqlConfiguration configureConverter(final SqlConfiguration configuration) {
        if (configuration.resultRowConverter().isEmpty()) {
            return SqlConfiguration.copyOf(configuration).withResultRowConverter(getDefaultRowConverter());
        }
        final var currentConverter = configuration.resultRowConverter().get();
        final var converter = ResultRowConverter.builder()
                .setAlias(Strings.isBlank(currentConverter.alias()) ?
                        getDefaultAlias(currentConverter) : currentConverter.alias())
                .setConverterType(Strings.isBlank(currentConverter.converterType()) ?
                        getDefaultConverterType(currentConverter) : currentConverter.converterType())
                .setMethodName(Strings.isBlank(currentConverter.methodName()) ?
                        getDefaultMethodName(currentConverter) : currentConverter.methodName())
                .setResultType(Strings.isBlank(currentConverter.resultType()) ?
                        getDefaultResultType(currentConverter) : currentConverter.resultType())
                .build();

        return SqlConfiguration.copyOf(configuration)
                .withResultRowConverter(converter);
    }

    private Optional<ResultRowConverter> getDefaultRowConverter() {
        final var resultRowConverter = converter.defaultConverter();
        return converter.rowConverters().stream()
                .filter(converter -> resultRowConverter.isEmpty() || resultRowConverter.get().equals(converter))
                .findFirst()
                .or(() -> resultRowConverter);
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
            final Function<ResultRowConverter, String> mapper) {
        return converter.rowConverters().stream()
                .filter(predicate)
                .map(mapper)
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
