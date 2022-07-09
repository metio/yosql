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
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.sql.Connection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class DefaultParameterGenerator implements ParameterGenerator {

    private final Parameters parameters;
    private final NamesConfiguration names;

    public DefaultParameterGenerator(final Parameters parameters, final NamesConfiguration names) {
        this.parameters = parameters;
        this.names = names;
    }

    @Override
    public List<ParameterSpec> asParameterSpecs(final SqlConfiguration configuration) {
        return asParameterSpecs(configuration, this::ofSqlParameter);
    }

    private ParameterSpec ofSqlParameter(final SqlParameter parameter) {
        return parameters.parameter(parameter.typeName()
                        .orElseThrow(MissingParameterTypeNameException::new),
                parameter.name().orElseThrow(MissingParameterNameException::new));
    }

    @Override
    public List<ParameterSpec> asParameterSpecsForInterfaces(final SqlConfiguration configuration) {
        return asParameterSpecs(configuration, this::ofSqlParameterForInterfaces);
    }

    private ParameterSpec ofSqlParameterForInterfaces(final SqlParameter parameter) {
        return parameters.parameterForInterfaces(parameter.typeName()
                        .orElseThrow(MissingParameterTypeNameException::new),
                parameter.name().orElseThrow(MissingParameterNameException::new));
    }

    @Override
    public List<ParameterSpec> asBatchParameterSpecs(final SqlConfiguration configuration) {
        return asParameterSpecs(configuration, this::batchOfSqlParameter);
    }

    private ParameterSpec batchOfSqlParameter(final SqlParameter parameter) {
        return parameters.parameter(ArrayTypeName.of(parameter.typeName()
                        .orElseThrow(MissingParameterTypeNameException::new)),
                parameter.name().orElseThrow(MissingParameterNameException::new));
    }

    @Override
    public List<ParameterSpec> asBatchParameterSpecsForInterfaces(final SqlConfiguration configuration) {
        return asParameterSpecs(configuration, this::batchOfSqlParameterForInterfaces);
    }

    private ParameterSpec batchOfSqlParameterForInterfaces(final SqlParameter parameter) {
        return parameters.parameterForInterfaces(ArrayTypeName.of(parameter.typeName()
                        .orElseThrow(MissingParameterTypeNameException::new)),
                parameter.name().orElseThrow(MissingParameterNameException::new));
    }

    private List<ParameterSpec> asParameterSpecs(
            final SqlConfiguration configuration,
            final Function<SqlParameter, ParameterSpec> asParameter) {
        return Stream.concat(
                        configuration.createConnection()
                                .filter(Boolean.FALSE::equals)
                                .map(value -> parameters.parameter(Connection.class, names.connection()))
                                .stream(),
                        configuration.parameters().stream().map(asParameter))
                .toList();
    }

}
