/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import ch.qos.cal10n.IMessageConveyor;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.lifecycle.ExecutionErrors;
import wtf.metio.yosql.codegen.lifecycle.ValidationErrors;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DefaultMethodParameterConfigurer implements MethodParameterConfigurer {

    private final LocLogger logger;
    private final ExecutionErrors errors;
    private final IMessageConveyor messages;

    public DefaultMethodParameterConfigurer(
            final LocLogger logger,
            final ExecutionErrors errors,
            final IMessageConveyor messages) {
        this.logger = logger;
        this.errors = errors;
        this.messages = messages;
    }

    @Override
    public SqlConfiguration configureParameters(final SqlConfiguration configuration, final Path source, final Map<String, List<Integer>> parameterIndices) {
        final var currentParameters = configuration.parameters();
        if (parametersAreValid(source, parameterIndices, configuration)) {
            final var updatedParameters = updateIndices(currentParameters, parameterIndices);
            final var allParameters = addMissingParameters(updatedParameters, parameterIndices);
            return SqlConfiguration.copyOf(configuration).withParameters(allParameters);
        }
        return configuration;
    }

    private boolean parametersAreValid(
            final Path source,
            final Map<String, List<Integer>> parameterIndices,
            final SqlConfiguration configuration) {
        final var parameterErrors = Stream.ofNullable(configuration.parameters())
                .flatMap(Collection::stream)
                .filter(param -> !parameterIndices.containsKey(param.name()))
                .map(param -> messages.getMessage(ValidationErrors.UNKNOWN_PARAMETER, source, param.name()))
                .peek(errors::illegalArgument)
                .peek(logger::error)
                .toList();
        return parameterErrors.isEmpty();
    }

    private static List<SqlParameter> updateIndices(final List<SqlParameter> parameters, final Map<String, List<Integer>> indices) {
        return parameters.stream()
                .map(parameter -> SqlParameter.builder()
                        .setName(parameter.name())
                        .setType(parameter.type())
                        .setIndices(asIntArray(indices.get(parameter.name())))
                        .setConverter(parameter.converter())
                        .build())
                .collect(Collectors.toList());
    }

    private static int[] asIntArray(final List<Integer> numbers) {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    private List<SqlParameter> addMissingParameters(final List<SqlParameter> parameters, final Map<String, List<Integer>> indices) {
        final var all = new ArrayList<>(parameters);
        for (final var entry : indices.entrySet()) {
            final var parameterName = entry.getKey();
            if (isMissingParameter(all, parameterName)) {
                all.add(SqlParameter.builder()
                        .setName(parameterName)
                        .setType("java.lang.Object")
                        .setIndices(asIntArray(entry.getValue()))
                        .build());
            }
        }
        return all;
    }

    private static boolean isMissingParameter(final List<SqlParameter> parameters, final String parameterName) {
        return Stream.ofNullable(parameters).flatMap(Collection::stream).noneMatch(nameMatches(parameterName));
    }

    private static Predicate<? super SqlParameter> nameMatches(final String parameterName) {
        return parameter -> parameterName.equals(parameter.name());
    }

}
