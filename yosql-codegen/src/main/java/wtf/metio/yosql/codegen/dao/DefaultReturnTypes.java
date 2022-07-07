/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.codegen.exceptions.MissingConverterResultTypeException;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.util.Optional;

import static wtf.metio.yosql.models.configuration.SqlStatementType.WRITING;

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
    public Optional<TypeName> resultType(final SqlConfiguration configuration) {
        return configuration.returningMode()
                .map(mode -> switch (mode) {
                    case NONE -> noneResultType(configuration);
                    case SINGLE -> singleResultType(configuration);
                    case MULTIPLE -> multiResultType(configuration);
                    case CURSOR -> cursorResultType(configuration);
                });
    }

    @Override
    public TypeName noneResultType(final SqlConfiguration configuration) {
        return configuration.type()
                .filter(WRITING::equals)
                .filter(type -> configuration.writesReturnUpdateCount().orElse(Boolean.FALSE))
                .map(type -> TypeName.INT)
                .orElse(TypeName.VOID);
    }

    @Override
    public TypeName singleResultType(final SqlConfiguration configuration) {
        return TypicalTypes.optionalOf(resultTypeOf(configuration));
    }

    @Override
    public TypeName multiResultType(final SqlConfiguration configuration) {
        return TypicalTypes.listOf(resultTypeOf(configuration));
    }

    @Override
    public TypeName cursorResultType(final SqlConfiguration configuration) {
        return TypicalTypes.streamOf(resultTypeOf(configuration));
    }

    private TypeName resultTypeOf(final SqlConfiguration configuration) {
        return configuration.converter(converters::defaultConverter)
                .resultTypeName()
                .orElseThrow(MissingConverterResultTypeException::new);
    }

}
