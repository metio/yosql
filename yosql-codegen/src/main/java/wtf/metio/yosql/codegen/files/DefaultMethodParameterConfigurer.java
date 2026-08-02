/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import ch.qos.cal10n.IMessageConveyor;
import com.palantir.javapoet.ClassName;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.exceptions.UntypedParameterException;
import wtf.metio.yosql.codegen.lifecycle.ValidationErrors;
import wtf.metio.yosql.codegen.orchestration.ExecutionErrors;
import wtf.metio.yosql.codegen.records.JavaSourceComponent;
import wtf.metio.yosql.codegen.records.RecordScanner;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Works out the Java type of every parameter a statement binds.
 *
 * <p>A parameter is typed by the front matter, or by the record the statement already names as its
 * {@code resultRowType}: a query selecting a tenant by its id binds {@code :id} against the same
 * type the record's {@code id} component is declared as, and repeating that in the front matter
 * says nothing the record does not already say.</p>
 *
 * <p>What neither source can settle is an error rather than a default. {@code java.lang.Object}
 * would compile and would accept anything, which is precisely the type safety a generated
 * repository exists to provide.</p>
 */
public final class DefaultMethodParameterConfigurer implements MethodParameterConfigurer {

    private final LocLogger logger;
    private final ExecutionErrors errors;
    private final IMessageConveyor messages;
    private final RecordScanner scanner;

    public DefaultMethodParameterConfigurer(
            final LocLogger logger,
            final ExecutionErrors errors,
            final IMessageConveyor messages,
            final RecordScanner scanner) {
        this.logger = logger;
        this.errors = errors;
        this.messages = messages;
        this.scanner = scanner;
    }

    @Override
    public SqlConfiguration configureParameters(
            final SqlConfiguration configuration,
            final Path source,
            final Map<String, List<Integer>> parameterIndices) {
        if (!parametersAreValid(source, parameterIndices, configuration)) {
            return configuration;
        }
        final var declared = updateIndices(configuration.parameters(), parameterIndices);
        final var all = addMissingParameters(declared, parameterIndices);
        final var typed = inferTypes(all, configuration, source);
        return SqlConfiguration.copyOf(configuration).withParameters(typed);
    }

    private boolean parametersAreValid(
            final Path source,
            final Map<String, List<Integer>> parameterIndices,
            final SqlConfiguration configuration) {
        final var parameterErrors = configuration.parameters()
                .stream()
                .map(SqlParameter::name)
                .flatMap(Optional::stream)
                .filter(name -> !parameterIndices.containsKey(name))
                .map(name -> messages.getMessage(ValidationErrors.UNKNOWN_PARAMETER, source, name))
                .peek(errors::illegalArgument)
                .peek(logger::error)
                .toList();
        return parameterErrors.isEmpty();
    }

    /**
     * Fills in the type of every parameter that has none, and fails naming all of those that are
     * still without one — all of them, so that a statement missing several is corrected once rather
     * than a build at a time.
     */
    private List<SqlParameter> inferTypes(
            final List<SqlParameter> parameters,
            final SqlConfiguration configuration,
            final Path source) {
        if (parameters.stream().allMatch(DefaultMethodParameterConfigurer::hasType)) {
            return parameters;
        }
        final var components = componentTypes(configuration);
        final var typed = new ArrayList<SqlParameter>(parameters.size());
        final var untyped = new ArrayList<String>();
        for (final var parameter : parameters) {
            if (hasType(parameter)) {
                typed.add(parameter);
                continue;
            }
            final var name = parameter.name().orElse("<unnamed>");
            final var inferred = components.get(name);
            if (inferred == null) {
                untyped.add(name);
            } else {
                logger.debug("Parameter '{}' takes the type of the matching component: {}", name, inferred);
                typed.add(SqlParameter.copyOf(parameter).withType(inferred));
            }
        }
        if (!untyped.isEmpty()) {
            throw new UntypedParameterException(source, configuration.name().orElse("<unnamed>"), untyped);
        }
        return typed;
    }

    /**
     * The components of the record a statement builds its rows into, by name.
     *
     * <p>Only the record's own components count. A component of a nested record is addressed
     * through the component holding it, and a parameter is one plain name, so descending would have
     * to pick between candidates with equal claim to it — which is a guess, not an inference.</p>
     */
    private Map<String, String> componentTypes(final SqlConfiguration configuration) {
        return configuration.resultRowType()
                .map(String::strip)
                .filter(Predicate.not(String::isEmpty))
                .flatMap(DefaultMethodParameterConfigurer::classNameOf)
                .flatMap(scanner::scan)
                .filter(type -> type.isRecord())
                .map(type -> {
                    final var byName = new LinkedHashMap<String, String>();
                    for (final JavaSourceComponent component : type.components()) {
                        byName.put(component.name(), component.type().toString());
                    }
                    return Map.copyOf(byName);
                })
                .orElseGet(Map::of);
    }

    private static Optional<ClassName> classNameOf(final String type) {
        try {
            return Optional.of(ClassName.bestGuess(type));
        } catch (final IllegalArgumentException _) {
            return Optional.empty();
        }
    }

    private static boolean hasType(final SqlParameter parameter) {
        return parameter.type()
                .map(String::strip)
                .filter(Predicate.not(String::isEmpty))
                .isPresent();
    }

    private static List<SqlParameter> updateIndices(final List<SqlParameter> parameters, final Map<String, List<Integer>> indices) {
        return parameters.stream()
                .<SqlParameter>map(parameter -> SqlParameter.copyOf(parameter)
                        .withIndices(parameter.name()
                                .map(indices::get)
                                .map(DefaultMethodParameterConfigurer::asIntArray)))
                .toList();
    }

    private static int[] asIntArray(final List<Integer> numbers) {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    private static List<SqlParameter> addMissingParameters(final List<SqlParameter> parameters, final Map<String, List<Integer>> indices) {
        final var all = new ArrayList<>(parameters);
        for (final var entry : indices.entrySet()) {
            final var parameterName = entry.getKey();
            if (isMissingParameter(all, parameterName)) {
                all.add(SqlParameter.builder()
                        .setName(parameterName)
                        .setIndices(asIntArray(entry.getValue()))
                        .build());
            }
        }
        return all;
    }

    private static boolean isMissingParameter(final List<SqlParameter> parameters, final String parameterName) {
        return parameters.stream().noneMatch(nameMatches(parameterName));
    }

    private static Predicate<? super SqlParameter> nameMatches(final String parameterName) {
        return parameter -> parameter.name()
                .map(parameterName::equals)
                .orElse(Boolean.FALSE);
    }

}
