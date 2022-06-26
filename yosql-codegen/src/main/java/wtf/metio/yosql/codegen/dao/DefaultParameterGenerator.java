/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ParameterSpec;
import wtf.metio.yosql.codegen.blocks.Parameters;
import wtf.metio.yosql.codegen.exceptions.MissingParameterNameException;
import wtf.metio.yosql.codegen.exceptions.MissingParameterTypeNameException;
import wtf.metio.yosql.models.configuration.SqlParameter;

import java.util.List;
import java.util.function.Function;

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
        return parameters.parameter(parameter.typeName()
                        .orElseThrow(MissingParameterTypeNameException::new),
                parameter.name().orElseThrow(MissingParameterNameException::new));
    }

    @Override
    public Iterable<ParameterSpec> asParameterSpecsForInterfaces(final List<SqlParameter> parameters) {
        return asParameterSpecs(parameters, this::ofSqlParameterForInterfaces);
    }

    private ParameterSpec ofSqlParameterForInterfaces(final SqlParameter parameter) {
        return parameters.parameterForInterfaces(parameter.typeName()
                        .orElseThrow(MissingParameterTypeNameException::new),
                parameter.name().orElseThrow(MissingParameterNameException::new));
    }

    @Override
    public Iterable<ParameterSpec> asBatchParameterSpecs(final List<SqlParameter> parameters) {
        return asParameterSpecs(parameters, this::batchOfSqlParameter);
    }

    private ParameterSpec batchOfSqlParameter(final SqlParameter parameter) {
        return parameters.parameter(ArrayTypeName.of(parameter.typeName()
                        .orElseThrow(MissingParameterTypeNameException::new)),
                parameter.name().orElseThrow(MissingParameterNameException::new));
    }

    @Override
    public Iterable<ParameterSpec> asBatchParameterSpecsForInterfaces(final List<SqlParameter> parameters) {
        return asParameterSpecs(parameters, this::batchOfSqlParameterForInterfaces);
    }

    private ParameterSpec batchOfSqlParameterForInterfaces(final SqlParameter parameter) {
        return parameters.parameterForInterfaces(ArrayTypeName.of(parameter.typeName()
                        .orElseThrow(MissingParameterTypeNameException::new)),
                parameter.name().orElseThrow(MissingParameterNameException::new));
    }

    private static List<ParameterSpec> asParameterSpecs(
            final List<SqlParameter> parameters,
            final Function<SqlParameter, ParameterSpec> asParameter) {
        return parameters.stream()
                .map(asParameter)
                .toList();
    }

}
