/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.blocks.Annotations;
import wtf.metio.yosql.codegen.blocks.Classes;
import wtf.metio.yosql.codegen.blocks.Methods;
import wtf.metio.yosql.codegen.dao.JdbcParameters;
import wtf.metio.yosql.codegen.dao.MethodExceptionHandler;
import wtf.metio.yosql.codegen.exceptions.AmbiguousValueOfException;
import wtf.metio.yosql.codegen.exceptions.ConflictingColumnOverrideException;
import wtf.metio.yosql.codegen.exceptions.DuplicateConverterNameException;
import wtf.metio.yosql.codegen.exceptions.MissingRecordSourceException;
import wtf.metio.yosql.codegen.exceptions.RecursiveRecordException;
import wtf.metio.yosql.codegen.exceptions.ScalarResultColumnsException;
import wtf.metio.yosql.codegen.exceptions.UnreadableResultRowTypeException;
import wtf.metio.yosql.codegen.exceptions.UnmappedColumnsException;
import wtf.metio.yosql.codegen.exceptions.UnparsableRecordException;
import wtf.metio.yosql.codegen.lifecycle.CodegenLifecycle;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Writes the converter a statement's {@code resultRowType} needs.
 *
 * <p>The record is read from source at build time, and what comes out is one class per record type
 * holding one method: locals read straight off the {@link java.sql.ResultSet}, then the canonical
 * constructor. That is the same code a person writes by hand, which is the point — it is also the
 * only shape that survives a GraalVM native image without a reflection hint.</p>
 *
 * <p>Whether the query and the record actually agree is decided here too, while both are in front
 * of us. A component no column supplies, or a selected column no component claims, stops the
 * build.</p>
 */
public final class RecordConverterGenerator {

    private final LocLogger logger;
    private final RecordScanner scanner;
    private final RecordConverterNames names;
    private final ResultSetReaders readers;
    private final String resultSet;
    private final Annotations annotations;
    private final Classes classes;
    private final Methods methods;
    private final JdbcParameters jdbcParameters;
    private final MethodExceptionHandler exceptions;

    public RecordConverterGenerator(
            final LocLogger logger,
            final RecordScanner scanner,
            final RecordConverterNames names,
            final NamesConfiguration namesConfiguration,
            final Annotations annotations,
            final Classes classes,
            final Methods methods,
            final JdbcParameters jdbcParameters,
            final MethodExceptionHandler exceptions) {
        this.logger = logger;
        this.scanner = scanner;
        this.names = names;
        this.readers = new ResultSetReaders(namesConfiguration.resultSet());
        this.resultSet = namesConfiguration.resultSet();
        this.annotations = annotations;
        this.classes = classes;
        this.methods = methods;
        this.jdbcParameters = jdbcParameters;
        this.exceptions = exceptions;
    }

    public Stream<PackagedTypeSpec> generateConverterClasses(final List<SqlStatement> statements) {
        final var byType = new LinkedHashMap<ClassName, JavaSourceType>();
        final var columnsByType = new LinkedHashMap<ClassName, Map<String, String>>();

        // Two passes, because a column override belongs to the result row type rather than to one
        // query: every statement naming that type shares one set, so the set has to be complete
        // before any statement can be checked against it.
        final var scalars = new LinkedHashMap<ClassName, Scalar>();
        final var declaring = new ArrayList<SqlStatement>();
        for (final var statement : statements) {
            final var declared = statement.getConfiguration().resultRowType();
            if (declared.isEmpty()) {
                continue;
            }
            final var type = resultRowType(declared.get(), statement);
            final var single = scalars.containsKey(type) ? Optional.of(scalars.get(type)) : scalar(type);
            if (single.isPresent()) {
                scalars.putIfAbsent(type, single.get());
                verifyScalar(statement, single.get());
                continue;
            }
            byType.computeIfAbsent(type, key -> read(key, statement));
            mergeOverrides(type, statement, columnsByType);
            declaring.add(statement);
        }
        final var everyType = new LinkedHashSet<>(byType.keySet());
        everyType.addAll(scalars.keySet());
        rejectCollidingNames(everyType);
        for (final var statement : declaring) {
            final var type = ClassName.bestGuess(statement.getConfiguration().resultRowType().orElseThrow().strip());
            verify(statement, byType.get(type), columnsByType.getOrDefault(type, Map.of()));
        }
        return Stream.concat(
                byType.values().stream()
                        .map(record -> generateConverterClass(record, columnsByType.getOrDefault(record.type(), Map.of()))),
                scalars.values().stream().map(this::generateScalarConverter));
    }

    /**
     * Folds one statement's overrides into the type's set. Two statements mapping the same component
     * to different columns is a contradiction rather than a precedence question — there is one
     * converter, and it cannot read both.
     */
    private static ClassName resultRowType(final String declared, final SqlStatement statement) {
        final var name = declared.strip();
        try {
            return ClassName.bestGuess(name);
        } catch (final IllegalArgumentException exception) {
            throw new UnreadableResultRowTypeException(statement.getName(), name,
                    "is not a type a result can be built into. A primitive cannot be the result of a "
                            + "statement that may return no row — name its wrapper instead.");
        }
    }

    private static void mergeOverrides(
            final ClassName type,
            final SqlStatement statement,
            final Map<ClassName, Map<String, String>> columnsByType) {
        final var declared = statement.getConfiguration().resultRowColumns().orElse(Map.of());
        if (declared.isEmpty()) {
            return;
        }
        final var merged = columnsByType.computeIfAbsent(type, key -> new LinkedHashMap<>());
        for (final var override : declared.entrySet()) {
            final var previous = merged.put(override.getKey(), override.getValue());
            if (previous != null && !previous.equals(override.getValue())) {
                throw new ConflictingColumnOverrideException(
                        type.toString(), override.getKey(), previous, override.getValue(), statement.getName());
            }
        }
    }

    /**
     * Two records sharing a simple name would be served by one converter class name, and the second
     * would silently overwrite the first. What a consumer would see is a type error in generated
     * source, which is the worst way to learn about it.
     */
    private void rejectCollidingNames(final Set<ClassName> types) {
        final var byConverter = new LinkedHashMap<ClassName, ClassName>();
        for (final var type : types) {
            final var previous = byConverter.putIfAbsent(names.converterClass(type), type);
            if (previous != null) {
                throw new DuplicateConverterNameException(names.converterClass(type), previous, type);
            }
        }
    }

    private JavaSourceType read(final ClassName type, final SqlStatement statement) {
        final var source = scanner.scan(type)
                .orElseThrow(() -> new MissingRecordSourceException(type, scanner.locationOf(type), statement.getName()));
        if (!source.isRecord()) {
            throw new UnparsableRecordException(scanner.locationOf(type), type,
                    "it is not a record, and only a record's canonical constructor tells the generator "
                            + "which columns to read");
        }
        return source;
    }

    /**
     * Whether a result row type holds one value rather than a row of them.
     *
     * <p>True for anything a column can hold directly, for an enum, and for a type that says how to
     * build itself from one — the same rule that decides it at component level, applied to the type
     * naming the whole result. So `resultRowType: java.lang.Long` reads a count, and a value type
     * reads the column it wraps rather than becoming a row of one.</p>
     */
    private Optional<Scalar> scalar(final ClassName type) {
        final var enumeration = isEnum(type);
        if (readers.supports(type, enumeration) && !isRecordWithoutFactory(type)) {
            return Optional.of(new Scalar(type, enumeration, Optional.empty()));
        }
        return scanner.scan(type)
                .filter(JavaSourceType::hasValueOf)
                .map(source -> new Scalar(type, false, valueOfParameter(type, List.of("value"))));
    }

    private boolean isRecordWithoutFactory(final ClassName type) {
        return scanner.scan(type).map(source -> source.isRecord() && !source.hasValueOf()).orElse(Boolean.FALSE);
    }

    private PackagedTypeSpec generateScalarConverter(final Scalar scalar) {
        final var converterClass = names.converterClass(scalar.type());
        final var body = scalar.valueOf()
                .map(parameter -> readers.readFirstColumnVia(scalar.type(), parameter, "value", "the result"))
                .orElseGet(() -> readers.readFirstColumn(scalar.type(), scalar.enumeration(), "value", "the result"));
        final var method = methods.publicMethod(names.methodName())
                .addJavadoc("Reads a $T from the first column of the current row.\n", scalar.type())
                .addJavadoc("\n@param $L The result set, positioned on the row to read.", resultSet)
                .addJavadoc("\n@return The value the first column holds.")
                .addParameters(jdbcParameters.toMapConverterParameterSpecs())
                .addException(exceptions.thrownException())
                .returns(scalar.type())
                .addCode(body)
                .addStatement("return $N", "value")
                .build();
        final var type = classes.publicClass(converterClass)
                .addJavadoc("Reads a $T from the first column of a result set row.\n", scalar.type())
                .addAnnotations(annotations.generatedClass())
                .addMethod(method)
                .build();
        logger.debug(CodegenLifecycle.TYPE_GENERATED, converterClass.packageName(), converterClass.simpleName());
        return PackagedTypeSpec.of(type, converterClass.packageName());
    }

    /**
     * A single value read from one column is only unambiguous when the statement selects one. When
     * the select list cannot be enumerated — `count(*)` names nothing — there is nothing to check.
     */
    private static void verifyScalar(final SqlStatement statement, final Scalar scalar) {
        SelectedColumns.of(statement.getRawStatement())
                .filter(columns -> columns.size() != 1)
                .ifPresent(columns -> {
                    throw new ScalarResultColumnsException(statement.getSourcePath(), statement.getName(),
                            scalar.type().toString(), columns);
                });
    }

    private record Scalar(ClassName type, boolean enumeration, Optional<TypeName> valueOf) {
    }

    private void verify(
            final SqlStatement statement,
            final JavaSourceType record,
            final Map<String, String> overrides) {
        final var selected = SelectedColumns.of(statement.getRawStatement());
        if (selected.isEmpty()) {
            // `select *` and unaliased expressions name nothing checkable. Saying so beats
            // inventing a column list and failing a build over it.
            logger.debug(CodegenLifecycle.TYPE_GENERATED, record.type().packageName(), record.type().simpleName());
            return;
        }
        final var columns = new LinkedHashSet<>(selected.get());
        final var claimed = new LinkedHashSet<String>();
        final var missing = new ArrayList<String>();
        for (final var leaf : flatten(record, overrides)) {
            if (columns.contains(leaf.column())) {
                claimed.add(leaf.column());
            } else {
                missing.add("%s (column '%s')".formatted(leaf.path(), leaf.column()));
            }
        }
        final var unclaimed = new LinkedHashSet<>(columns);
        unclaimed.removeAll(claimed);
        if (!missing.isEmpty() || !unclaimed.isEmpty()) {
            throw new UnmappedColumnsException(statement.getSourcePath(), statement.getName(),
                    record.type().toString(), missing, unclaimed);
        }
    }

    private PackagedTypeSpec generateConverterClass(
            final JavaSourceType record, final Map<String, String> overrides) {
        final var converterClass = names.converterClass(record.type());
        final var type = classes.publicClass(converterClass)
                .addJavadoc("Builds $T from a result set row.\n", record.type())
                .addAnnotations(annotations.generatedClass())
                .addMethod(converterMethod(record, overrides))
                .build();
        logger.debug(CodegenLifecycle.TYPE_GENERATED, converterClass.packageName(), converterClass.simpleName());
        return PackagedTypeSpec.of(type, converterClass.packageName());
    }

    private MethodSpec converterMethod(final JavaSourceType record, final Map<String, String> overrides) {
        final var body = CodeBlock.builder();
        final var taken = new LinkedHashSet<String>();
        final var construction = build(record, List.of(), body, taken,
                new LinkedHashSet<>(List.of(record.type())), overrides);
        return methods.publicMethod(names.methodName())
                .addJavadoc("Builds $T from the current row.\n", record.type())
                .addJavadoc("\n@param $L The result set, positioned on the row to build from.", resultSet)
                .addJavadoc("\n@return The row, as $T.", record.type())
                .addParameters(jdbcParameters.toMapConverterParameterSpecs())
                .addException(exceptions.thrownException())
                .returns(record.type())
                .addCode(body.build())
                .addStatement("return $L", construction)
                .build();
    }

    /**
     * Emits the reads for one record and returns the expression constructing it. Nested records
     * recurse: their components read from the same flat row, and the constructor call nests.
     */
    private CodeBlock build(
            final JavaSourceType record,
            final List<String> path,
            final CodeBlock.Builder body,
            final Set<String> taken,
            final Set<ClassName> enclosing,
            final Map<String, String> overrides) {
        final var arguments = new ArrayList<CodeBlock>(record.components().size());
        for (final var component : record.components()) {
            final var componentPath = append(path, component.name());
            final var factory = valueOfParameter(component.type(), componentPath);
            final var nested = factory.isPresent() ? Optional.<JavaSourceType>empty() : nestedRecord(component.type());
            if (nested.isPresent()) {
                rejectCycle(nested.get().type(), componentPath, enclosing);
                final var deeper = new LinkedHashSet<>(enclosing);
                deeper.add(nested.get().type());
                arguments.add(build(nested.get(), componentPath, body, taken, deeper, overrides));
                continue;
            }
            final var variable = variableFor(componentPath, taken);
            final var column = columnFor(componentPath, component.name(), overrides);
            final var where = String.join(".", componentPath);
            body.add(factory
                    .map(parameter -> readers.readVia(component.type(), parameter, column, variable, where))
                    .orElseGet(() -> readers.read(
                            component.type(), isEnum(component.type()), column, variable, where)));
            arguments.add(CodeBlock.of("$N", variable));
        }
        return CodeBlock.of("new $T($L)", record.type(), CodeBlock.join(arguments, ", "));
    }

    private List<Leaf> flatten(final JavaSourceType record, final Map<String, String> overrides) {
        final var leaves = new ArrayList<Leaf>();
        collect(record, List.of(), leaves, new LinkedHashSet<>(List.of(record.type())), overrides);
        return leaves;
    }

    /**
     * The column a component reads: its own name as snake_case, unless a statement named one.
     */
    private static String columnFor(
            final List<String> path, final String componentName, final Map<String, String> overrides) {
        final var override = overrides.get(String.join(".", path));
        return override != null ? override : ColumnNames.columnFor(componentName);
    }

    private void collect(
            final JavaSourceType record,
            final List<String> path,
            final List<Leaf> leaves,
            final Set<ClassName> enclosing,
            final Map<String, String> overrides) {
        for (final var component : record.components()) {
            final var componentPath = append(path, component.name());
            final var nested = valueOfParameter(component.type(), componentPath).isPresent()
                    ? Optional.<JavaSourceType>empty()
                    : nestedRecord(component.type());
            if (nested.isPresent()) {
                rejectCycle(nested.get().type(), componentPath, enclosing);
                final var deeper = new LinkedHashSet<>(enclosing);
                deeper.add(nested.get().type());
                collect(nested.get(), componentPath, leaves, deeper, overrides);
            } else {
                leaves.add(new Leaf(String.join(".", componentPath),
                        columnFor(componentPath, component.name(), overrides)));
            }
        }
    }

    private static void rejectCycle(
            final ClassName type, final List<String> path, final Set<ClassName> enclosing) {
        if (enclosing.contains(type)) {
            throw new RecursiveRecordException(type, String.join(".", path));
        }
    }

    /**
     * The single value a type's own {@code valueOf} takes, when it has one.
     *
     * <p>Checked before nesting, and that order is the rule: a one-component record with a factory
     * is a value wrapped around a column, and without one it is a record whose component reads a
     * column of its own. The type says which by declaring the factory or not.</p>
     */
    private Optional<TypeName> valueOfParameter(final TypeName type, final List<String> path) {
        if (!(type instanceof ClassName className)) {
            return Optional.empty();
        }
        final var source = scanner.scan(className);
        if (source.isEmpty() || !source.get().hasValueOf()) {
            return Optional.empty();
        }
        final var readable = source.get().valueOfParameters().stream()
                .filter(parameter -> readers.supports(parameter, isEnum(parameter)))
                .distinct()
                .toList();
        if (readable.size() > 1) {
            throw new AmbiguousValueOfException(String.join(".", path), type, readable);
        }
        return readable.stream().findFirst();
    }

    private Optional<JavaSourceType> nestedRecord(final TypeName type) {
        if (!(type instanceof ClassName className)) {
            return Optional.empty();
        }
        return scanner.scan(className).filter(JavaSourceType::isRecord);
    }

    private boolean isEnum(final TypeName type) {
        return type instanceof ClassName className
                && scanner.scan(className).map(JavaSourceType::isEnum).orElse(Boolean.FALSE);
    }

    private static List<String> append(final List<String> path, final String segment) {
        final var extended = new ArrayList<>(path);
        extended.add(segment);
        return List.copyOf(extended);
    }

    /**
     * One local per leaf, named after its path so two components called {@code id} in different
     * nested records do not collide.
     */
    private static String variableFor(final List<String> path, final Set<String> taken) {
        final var name = new StringBuilder(path.get(0));
        for (var index = 1; index < path.size(); index++) {
            final var segment = path.get(index);
            name.append(segment.substring(0, 1).toUpperCase(Locale.ROOT)).append(segment.substring(1));
        }
        var candidate = name.toString();
        var suffix = 2;
        while (!taken.add(candidate)) {
            candidate = name + String.valueOf(suffix++);
        }
        return candidate;
    }

    private record Leaf(String path, String column) {
    }

}
