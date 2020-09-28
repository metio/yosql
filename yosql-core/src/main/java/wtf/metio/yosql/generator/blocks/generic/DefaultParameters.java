/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.generator.blocks.api.Names;
import wtf.metio.yosql.generator.blocks.api.Parameters;
import wtf.metio.yosql.model.sql.SqlParameter;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

final class DefaultParameters implements Parameters {

    private final Names names;

    DefaultParameters(final Names names) {
        this.names = names;
    }

    @Override
    public ParameterSpec parameter(final Class<?> type, final String name) {
        return parameter(ClassName.get(type), name);
    }

    @Override
    public ParameterSpec parameter(final TypeName type, final String name) {
        return ParameterSpec.builder(type, name, Modifier.FINAL).build();
    }

    @Override
    public Iterable<ParameterSpec> asParameterSpecs(final List<SqlParameter> parameters) {
        return asParameterSpecs(parameters, this::ofSqlParameter);
    }

    private ParameterSpec ofSqlParameter(final SqlParameter parameter) {
        return parameter(TypeGuesser.guessTypeName(parameter.getType()), parameter.getName());
    }

    @Override
    public Iterable<ParameterSpec> asBatchParameterSpecs(List<SqlParameter> parameters) {
        return asParameterSpecs(parameters, this::batchOfSqlParameter);
    }

    public ParameterSpec batchOfSqlParameter(final SqlParameter parameter) {
        return parameter(ArrayTypeName.of(TypeGuesser.guessTypeName(parameter.getType())), parameter.getName());
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

    @Override
    public Iterable<ParameterSpec> resultState(final TypeName type) {
        // TODO: rename/remove method?
        return Collections.singletonList(parameter(type, names.result()));
    }

}
