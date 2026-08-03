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
import wtf.metio.yosql.codegen.exceptions.ConflictingColumnTypeException;
import wtf.metio.yosql.codegen.records.JavaSourceComponent;
import wtf.metio.yosql.codegen.records.RecordScanner;
import wtf.metio.yosql.codegen.schema.Catalog;
import wtf.metio.yosql.codegen.schema.Schemas;
import wtf.metio.yosql.codegen.schema.SqlTypes;
import wtf.metio.yosql.codegen.schema.TableScope;
import wtf.metio.yosql.codegen.exceptions.ReservedParameterNameException;
import wtf.metio.yosql.models.configuration.GeneratedNames;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
    private final Schemas schemas;

    public DefaultMethodParameterConfigurer(
            final LocLogger logger,
            final ExecutionErrors errors,
            final IMessageConveyor messages,
            final RecordScanner scanner,
            final Schemas schemas) {
        this.logger = logger;
        this.errors = errors;
        this.messages = messages;
        this.scanner = scanner;
        this.schemas = schemas;
    }

    @Override
    public SqlConfiguration configureParameters(
            final SqlConfiguration configuration,
            final Path source,
            final String sql,
            final Map<String, List<Integer>> parameterIndices) {
        if (!parametersAreValid(source, parameterIndices, configuration)) {
            return configuration;
        }
        rejectReservedNames(source, configuration, parameterIndices.keySet());
        final var declared = updateIndices(configuration.parameters(), parameterIndices);
        final var all = addMissingParameters(declared, parameterIndices);
        final var typed = inferTypes(all, configuration, source, sql);
        return SqlConfiguration.copyOf(configuration).withParameters(typed);
    }

    /**
     * Every name the statement binds, declared or not: a parameter YoSQL only learns about from the
     * SQL becomes a method parameter too, and collides just the same.
     */
    private static void rejectReservedNames(
            final Path source,
            final SqlConfiguration configuration,
            final Set<String> bound) {
        for (final var name : bound) {
            if (GeneratedNames.TAKEN.contains(name)) {
                throw new ReservedParameterNameException(source,
                        configuration.name().orElse("<unnamed>"), name);
            }
        }
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
            final Path source,
            final String sql) {
        if (parameters.stream().allMatch(DefaultMethodParameterConfigurer::hasType)) {
            return parameters;
        }
        final var components = componentTypes(configuration);
        final var columns = columnTypes(configuration, sql, source);
        final var typed = new ArrayList<SqlParameter>(parameters.size());
        final var untyped = new ArrayList<String>();
        for (final var parameter : parameters) {
            if (hasType(parameter)) {
                typed.add(parameter);
                continue;
            }
            final var name = parameter.name().orElse("<unnamed>");
            final var fromComponent = components.get(name);
            final var fromColumn = columns.get(Catalog.normalize(name));
            if (fromComponent != null) {
                logger.debug("Parameter '{}' takes the type of the matching component: {}", name, fromComponent);
                typed.add(SqlParameter.copyOf(parameter).withType(fromComponent));
            } else if (fromColumn != null) {
                logger.debug("Parameter '{}' takes the type of the matching column: {}", name, fromColumn);
                typed.add(SqlParameter.copyOf(parameter).withType(fromColumn));
            } else {
                untyped.add(name);
            }
        }
        if (!untyped.isEmpty()) {
            throw new UntypedParameterException(source, configuration.name().orElse("<unnamed>"), untyped);
        }
        return typed;
    }

    /**
     * The type of every column a parameter could be named after, from the schema the statement runs
     * against.
     *
     * <p>A statement that names no vendor is the fallback for every database not named, so it is
     * looked up in all of them. Where those disagree — a column that is a {@code uuid} on one and a
     * {@code varchar} on another — there is no answer: the method has one signature and cannot have
     * both types. That is reported rather than resolved by picking one, and naming the type in the
     * front matter settles it.</p>
     */
    private Map<String, String> columnTypes(
            final SqlConfiguration configuration,
            final String sql,
            final Path source) {
        if (schemas.isEmpty()) {
            return Map.of();
        }
        final var scope = TableScope.of(sql);
        if (!scope.exhaustive()) {
            return Map.of();
        }
        final var vendor = configuration.vendor();
        final var types = new LinkedHashMap<String, String>();
        final var disagreements = new LinkedHashMap<String, Set<String>>();
        for (final var catalog : schemas.applicableTo(vendor)) {
            for (final var table : scope.tables()) {
                catalog.table(table).ifPresent(known -> known.columns().forEach((name, column) ->
                        SqlTypes.javaType(column, vendor).ifPresent(type -> {
                            final var previous = types.put(name, type.toString());
                            if (previous != null && !previous.equals(type.toString())) {
                                disagreements.computeIfAbsent(name, _ -> new LinkedHashSet<>())
                                        .addAll(List.of(previous, type.toString()));
                            }
                        })));
            }
        }
        if (!disagreements.isEmpty()) {
            throw new ConflictingColumnTypeException(source, configuration.name().orElse("<unnamed>"),
                    disagreements);
        }
        return types;
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
