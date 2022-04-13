/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.codegen.api.Names;
import wtf.metio.yosql.codegen.api.Parameters;
import wtf.metio.yosql.models.immutables.JavaConfiguration;
import wtf.metio.yosql.models.sql.SqlParameter;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class DefaultParameters implements Parameters {

    private final Names names;
    private final JavaConfiguration runtimeConfiguration;
    private final ConcurrentHashMap<TypeName, Integer> args = new ConcurrentHashMap<>();

    public DefaultParameters(final Names names, final JavaConfiguration runtimeConfiguration) {
        this.names = names;
        this.runtimeConfiguration = runtimeConfiguration;
    }

    @Override
    public ParameterSpec parameter(final Class<?> type, final String name) {
        return parameter(ClassName.get(type), name);
    }

    @Override
    public ParameterSpec parameter(final TypeName type, final String name) {
        final var safeName = name == null || name.isBlank() ? "arg" + args.compute(type, (k, v) -> {
            if (v == null) {
                return 1;
            }
            return v + 1;
        }) : name;
        if (runtimeConfiguration.useFinal()) {
            return ParameterSpec.builder(type, safeName, Modifier.FINAL).build();
        }
        return ParameterSpec.builder(type, safeName).build();
    }

    @Override
    public Iterable<ParameterSpec> asParameterSpecs(final List<SqlParameter> parameters) {
        return asParameterSpecs(parameters, this::ofSqlParameter);
    }

    private ParameterSpec ofSqlParameter(final SqlParameter parameter) {
        return parameter(TypeGuesser.guessTypeName(parameter.type()), parameter.name());
    }

    @Override
    public Iterable<ParameterSpec> asBatchParameterSpecs(List<SqlParameter> parameters) {
        return asParameterSpecs(parameters, this::batchOfSqlParameter);
    }

    public ParameterSpec batchOfSqlParameter(final SqlParameter parameter) {
        return parameter(ArrayTypeName.of(TypeGuesser.guessTypeName(parameter.type())), parameter.name());
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
        return Collections.singletonList(parameter(type, names.result()));
    }

}
