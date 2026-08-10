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
import wtf.metio.yosql.codegen.records.ColumnNames;
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
import java.util.Arrays;
import java.util.Comparator;
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
        final var all = inSqlOrder(addMissingParameters(declared, parameterIndices));
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
            for (final var other : bound) {
                if (GeneratedNames.derivesFrom(name, other)) {
                    throw new ReservedParameterNameException(source,
                            configuration.name().orElse("<unnamed>"), name, other);
                }
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
            final var fromColumn = columnType(columns, name);
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
            final var scope = TableScope.of(sql);
            throw new UntypedParameterException(source, configuration.name().orElse("<unnamed>"), untyped,
                    tablesRead(configuration.vendor()), scope.exhaustive() ? scope.tables() : null,
                    columns.keySet());
        }
        return typed;
    }

    /**
     * Every table the schema came out holding, for a statement that could not be typed from it.
     *
     * <p>Across the catalogs the statement could run against, since a table described for one vendor
     * and not another is exactly the case a reader would otherwise have to guess at.</p>
     */
    private List<String> tablesRead(final Optional<String> vendor) {
        return schemas.applicableTo(vendor).stream()
                .map(Catalog::tableNames)
                .flatMap(Set::stream)
                .distinct()
                .sorted()
                .toList();
    }

    /**
     * The column a parameter is named after, read the way the other half of the same statement reads
     * a name.
     *
     * <p>A result row component named {@code createdAt} takes the {@code created_at} column, and a
     * parameter written {@code :createdAt} has to mean the same column or one statement holds two
     * conventions. Matching the spelling literally left every parameter named after a
     * {@code snake_case} column — most of them, in a schema written the usual way — falling through
     * to "no type known for", which reads as "the schema cannot answer this" when the schema
     * answered a name away.</p>
     *
     * <p>The literal spelling is tried first, so a parameter written {@code :created_at} keeps
     * naming the column it always did.</p>
     */
    private static String columnType(final Map<String, String> columns, final String parameter) {
        final var literal = columns.get(Catalog.normalize(parameter));
        return literal != null ? literal : columns.get(ColumnNames.columnFor(parameter));
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
     *
     * <p>Two tables of the <em>same</em> schema declaring a column of one name is not that. Any join
     * of tables that both have an {@code id} does it, and the statement said which one it meant by
     * writing {@code t.id}; the first table in scope is the reading everything else here takes for a
     * name written bare. Comparing them as though they were vendors made a plain two-table join fail
     * the build over a column no parameter was named after.</p>
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
            final var withinCatalog = new LinkedHashMap<String, String>();
            for (final var table : scope.tables()) {
                catalog.table(table).ifPresent(known -> known.columns().forEach((name, column) ->
                        SqlTypes.javaType(column, catalog.dialect(vendor)).ifPresent(type ->
                                withinCatalog.putIfAbsent(name, type.toString()))));
            }
            withinCatalog.forEach((name, type) -> {
                final var previous = types.put(name, type);
                if (previous != null && !previous.equals(type)) {
                    disagreements.computeIfAbsent(name, _ -> new LinkedHashSet<>())
                            .addAll(List.of(previous, type));
                }
            });
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

    /**
     * The order the statement binds them in, whichever of them the front matter happens to name.
     *
     * <p>A method's parameters are a property of its SQL — {@code :id} before {@code :slug} because
     * the insert lists them that way — and the front matter is telling us types. Appending the ones
     * it did not name put every declared parameter first, so a block naming one of six moved that
     * one to the front of a method whose SQL had not changed. Where the moved parameter shares a
     * type with the one it displaced, every call site still compiles and now passes them the wrong
     * way round.</p>
     *
     * <p>By first occurrence, since a parameter bound more than once is one method parameter and
     * the earliest position is the one a reader of the statement sees.</p>
     */
    private static List<SqlParameter> inSqlOrder(final List<SqlParameter> parameters) {
        return parameters.stream()
                .sorted(Comparator.comparingInt(DefaultMethodParameterConfigurer::firstIndex))
                .toList();
    }

    private static int firstIndex(final SqlParameter parameter) {
        return parameter.indices()
                .filter(indices -> indices.length > 0)
                .map(indices -> Arrays.stream(indices).min().orElse(Integer.MAX_VALUE))
                // Nothing says where it goes, so it keeps its place at the back rather than
                // jumping to the front of a sort that cannot see it.
                .orElse(Integer.MAX_VALUE);
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
