/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.records;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.blocks.Annotations;
import wtf.metio.yosql.codegen.blocks.Classes;
import wtf.metio.yosql.codegen.blocks.Methods;
import wtf.metio.yosql.codegen.dao.JdbcParameters;
import wtf.metio.yosql.codegen.dao.MethodExceptionHandler;
import wtf.metio.yosql.codegen.exceptions.DuplicateConverterNameException;
import wtf.metio.yosql.codegen.exceptions.MissingRecordSourceException;
import wtf.metio.yosql.codegen.exceptions.RecursiveRecordException;
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
        this.annotations = annotations;
        this.classes = classes;
        this.methods = methods;
        this.jdbcParameters = jdbcParameters;
        this.exceptions = exceptions;
    }

    public Stream<PackagedTypeSpec> generateConverterClasses(final List<SqlStatement> statements) {
        final var byType = new LinkedHashMap<ClassName, JavaSourceType>();
        for (final var statement : statements) {
            final var declared = statement.getConfiguration().resultRowType();
            if (declared.isEmpty()) {
                continue;
            }
            final var type = ClassName.bestGuess(declared.get().strip());
            final var record = byType.computeIfAbsent(type, key -> read(key, statement));
            verify(statement, record);
        }
        rejectCollidingNames(byType.keySet());
        return byType.values().stream().map(this::generateConverterClass);
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

    private void verify(final SqlStatement statement, final JavaSourceType record) {
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
        for (final var leaf : flatten(record)) {
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

    private PackagedTypeSpec generateConverterClass(final JavaSourceType record) {
        final var converterClass = names.converterClass(record.type());
        final var type = classes.publicClass(converterClass)
                .addJavadoc("Builds $T from a result set row.\n", record.type())
                .addAnnotations(annotations.generatedClass())
                .addMethod(converterMethod(record))
                .build();
        logger.debug(CodegenLifecycle.TYPE_GENERATED, converterClass.packageName(), converterClass.simpleName());
        return PackagedTypeSpec.of(type, converterClass.packageName());
    }

    private MethodSpec converterMethod(final JavaSourceType record) {
        final var body = CodeBlock.builder();
        final var taken = new LinkedHashSet<String>();
        final var construction = build(record, List.of(), body, taken,
                new LinkedHashSet<>(List.of(record.type())));
        return methods.publicMethod(names.methodName())
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
            final Set<ClassName> enclosing) {
        final var arguments = new ArrayList<CodeBlock>(record.components().size());
        for (final var component : record.components()) {
            final var componentPath = append(path, component.name());
            final var nested = nestedRecord(component.type());
            if (nested.isPresent()) {
                rejectCycle(nested.get().type(), componentPath, enclosing);
                final var deeper = new LinkedHashSet<>(enclosing);
                deeper.add(nested.get().type());
                arguments.add(build(nested.get(), componentPath, body, taken, deeper));
                continue;
            }
            final var variable = variableFor(componentPath, taken);
            body.add(readers.read(
                    component.type(),
                    isEnum(component.type()),
                    ColumnNames.columnFor(component.name()),
                    variable,
                    String.join(".", componentPath)));
            arguments.add(CodeBlock.of("$N", variable));
        }
        return CodeBlock.of("new $T($L)", record.type(), CodeBlock.join(arguments, ", "));
    }

    private List<Leaf> flatten(final JavaSourceType record) {
        final var leaves = new ArrayList<Leaf>();
        collect(record, List.of(), leaves, new LinkedHashSet<>(List.of(record.type())));
        return leaves;
    }

    private void collect(
            final JavaSourceType record,
            final List<String> path,
            final List<Leaf> leaves,
            final Set<ClassName> enclosing) {
        for (final var component : record.components()) {
            final var componentPath = append(path, component.name());
            final var nested = nestedRecord(component.type());
            if (nested.isPresent()) {
                rejectCycle(nested.get().type(), componentPath, enclosing);
                final var deeper = new LinkedHashSet<>(enclosing);
                deeper.add(nested.get().type());
                collect(nested.get(), componentPath, leaves, deeper);
            } else {
                leaves.add(new Leaf(String.join(".", componentPath), ColumnNames.columnFor(component.name())));
            }
        }
    }

    private static void rejectCycle(
            final ClassName type, final List<String> path, final Set<ClassName> enclosing) {
        if (enclosing.contains(type)) {
            throw new RecursiveRecordException(type, String.join(".", path));
        }
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
