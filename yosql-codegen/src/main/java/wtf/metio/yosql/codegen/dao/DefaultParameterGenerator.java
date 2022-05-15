/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ParameterSpec;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.codegen.blocks.Parameters;
import wtf.metio.yosql.models.configuration.SqlParameter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultParameterGenerator implements ParameterGenerator {

    private final Parameters parameters;

    public DefaultParameterGenerator(final Parameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public Iterable<ParameterSpec> asParameterSpecs(final List<SqlParameter> parameters) {
        return asParameterSpecs(parameters, this::ofSqlParameter);
    }

    private ParameterSpec ofSqlParameter(final SqlParameter parameter) {
        return parameters.parameter(TypeGuesser.guessTypeName(parameter.type()), parameter.name());
    }

    @Override
    public Iterable<ParameterSpec> asBatchParameterSpecs(List<SqlParameter> parameters) {
        return asParameterSpecs(parameters, this::batchOfSqlParameter);
    }

    public ParameterSpec batchOfSqlParameter(final SqlParameter parameter) {
        return parameters.parameter(ArrayTypeName.of(TypeGuesser.guessTypeName(parameter.type())), parameter.name());
    }

    private static Iterable<ParameterSpec> asParameterSpecs(
            final List<SqlParameter> parameters,
            final Function<SqlParameter, ParameterSpec> asParameter) {
        return Optional.ofNullable(parameters)
                .map(params -> params.stream()
                        .map(asParameter)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

}
