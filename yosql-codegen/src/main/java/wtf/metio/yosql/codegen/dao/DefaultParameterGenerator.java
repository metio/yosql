/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.models.configuration.GeneratedNames;
import com.palantir.javapoet.ArrayTypeName;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeName;
import wtf.metio.yosql.codegen.blocks.Parameters;
import wtf.metio.yosql.codegen.exceptions.MissingParameterNameException;
import wtf.metio.yosql.codegen.exceptions.MissingParameterTypeNameException;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.sql.Connection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DefaultParameterGenerator implements ParameterGenerator {

    private final Parameters parameters;

    public DefaultParameterGenerator(final Parameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public List<ParameterSpec> asParameterSpecs(final SqlConfiguration configuration) {
        return asParameterSpecs(configuration, this::ofSqlParameter, this::connection);
    }

    private ParameterSpec ofSqlParameter(final SqlParameter parameter) {
        return parameters.parameter(parameter.typeName()
                        .orElseThrow(MissingParameterTypeNameException::new),
                parameter.name().orElseThrow(MissingParameterNameException::new));
    }

    @Override
    public List<ParameterSpec> asParameterSpecsForInterfaces(final SqlConfiguration configuration) {
        return asParameterSpecs(configuration, this::ofSqlParameterForInterfaces, this::connectionForInterfaces);
    }

    private ParameterSpec ofSqlParameterForInterfaces(final SqlParameter parameter) {
        return parameters.parameterForInterfaces(parameter.typeName()
                        .orElseThrow(MissingParameterTypeNameException::new),
                parameter.name().orElseThrow(MissingParameterNameException::new));
    }

    @Override
    public List<ParameterSpec> asBatchParameterSpecs(final SqlConfiguration configuration) {
        return asParameterSpecs(configuration, this::batchOfSqlParameter, this::connection);
    }

    private ParameterSpec batchOfSqlParameter(final SqlParameter parameter) {
        return parameters.parameter(ArrayTypeName.of(parameter.typeName()
                        .orElseThrow(MissingParameterTypeNameException::new)),
                parameter.name().orElseThrow(MissingParameterNameException::new));
    }

    @Override
    public List<ParameterSpec> asBatchParameterSpecsForInterfaces(final SqlConfiguration configuration) {
        return asParameterSpecs(configuration, this::batchOfSqlParameterForInterfaces, this::connectionForInterfaces);
    }

    private ParameterSpec batchOfSqlParameterForInterfaces(final SqlParameter parameter) {
        return parameters.parameterForInterfaces(ArrayTypeName.of(parameter.typeName()
                        .orElseThrow(MissingParameterTypeNameException::new)),
                parameter.name().orElseThrow(MissingParameterNameException::new));
    }

    private ParameterSpec connection() {
        return parameters.parameter(Connection.class, GeneratedNames.CONNECTION);
    }

    private ParameterSpec connectionForInterfaces() {
        return parameters.parameterForInterfaces(TypeName.get(Connection.class), GeneratedNames.CONNECTION);
    }

    /**
     * The connection a statement runs on comes first, so that a repository's two overloads of a
     * name read as one signature with a connection in front of it rather than as two unrelated
     * methods.
     */
    private List<ParameterSpec> asParameterSpecs(
            final SqlConfiguration configuration,
            final Function<SqlParameter, ParameterSpec> asParameter,
            final Supplier<ParameterSpec> asConnection) {
        return Stream.concat(
                        configuration.createConnection()
                                .filter(Boolean.FALSE::equals)
                                .map(value -> asConnection.get())
                                .stream(),
                        configuration.parameters().stream().map(asParameter))
                .toList();
    }

}
