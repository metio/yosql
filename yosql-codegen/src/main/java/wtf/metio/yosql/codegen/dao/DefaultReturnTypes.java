/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.ParameterizedTypeName;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.util.Optional;

import static wtf.metio.yosql.models.configuration.ReturningMode.NONE;

/**
 * Default implementation for {@link ReturnTypes} that uses a {@link ConverterConfiguration} to determine the
 * default converter.
 */
public final class DefaultReturnTypes implements ReturnTypes {

    private final ConverterConfiguration converters;

    /**
     * @param converters The converter configuration to use.
     */
    public DefaultReturnTypes(final ConverterConfiguration converters) {
        this.converters = converters;
    }

    @Override
    public Optional<ParameterizedTypeName> resultType(final SqlConfiguration configuration) {
        return configuration.returningMode()
                .filter(mode -> NONE != mode)
                .map(mode -> switch (mode) {
                    case SINGLE -> singleResultType(configuration);
                    case CURSOR -> cursorResultType(configuration);
                    default -> multiResultType(configuration);
                });
    }

    @Override
    public ParameterizedTypeName singleResultType(final SqlConfiguration configuration) {
        final var converter = configuration.converter(converters::defaultConverter);
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        return TypicalTypes.optionalOf(resultType);
    }

    @Override
    public ParameterizedTypeName multiResultType(final SqlConfiguration configuration) {
        final var converter = configuration.converter(converters::defaultConverter);
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        return TypicalTypes.listOf(resultType);
    }

    @Override
    public ParameterizedTypeName cursorResultType(final SqlConfiguration configuration) {
        final var converter = configuration.converter(converters::defaultConverter);
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        return TypicalTypes.streamOf(resultType);
    }

}
