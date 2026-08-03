/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import wtf.metio.yosql.codegen.blocks.Annotations;
import wtf.metio.yosql.codegen.schema.Catalog;
import wtf.metio.yosql.codegen.schema.SelectItems;
import wtf.metio.yosql.codegen.schema.SqlTypes;
import wtf.metio.yosql.codegen.schema.Schemas;
import wtf.metio.yosql.codegen.schema.TableScope;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Writes the record a statement builds its rows into, instead of checking one somebody wrote.
 *
 * <p>Everything the record needs is already known: which columns the statement selects, what each
 * one holds, and whether it can be null. Asking a reader to write that out by hand and then
 * verifying they got it right is work that only exists because the generator was not allowed to do
 * it.</p>
 *
 * <p>Opt-in per statement, and deliberately so. A {@code resultRowType} naming a record that is not
 * there is far more often a typo than a request, and quietly writing a new record for a misspelled
 * name would replace a build error with a mystery.</p>
 */
public final class SchemaRecords {

    private final Schemas schemas;
    private final Annotations annotations;

    public SchemaRecords(final Schemas schemas, final Annotations annotations) {
        this.schemas = schemas;
        this.annotations = annotations;
    }

    /**
     * @return the record's shape, as though it had been read from a source file, or empty when the
     *         schema cannot describe every column the statement selects
     */
    public Optional<JavaSourceType> shapeOf(final ClassName type, final SqlStatement statement) {
        final var configuration = statement.getConfiguration();
        final var sql = statement.getRawStatement();
        final var scope = TableScope.of(sql);
        if (!scope.exhaustive()) {
            return Optional.empty();
        }
        for (final var catalog : schemas.applicableTo(configuration.vendor())) {
            final var components = componentsOf(sql, scope, catalog, configuration.vendor());
            if (components.isPresent()) {
                return Optional.of(JavaSourceType.record(type, components.get(), List.of(), List.of()));
            }
        }
        return Optional.empty();
    }

    /**
     * All of them or none. A record missing one component would read a row the statement does not
     * select, and the converter would then fail on a column that was never the reader's mistake.
     */
    private Optional<List<JavaSourceComponent>> componentsOf(
            final String sql,
            final TableScope scope,
            final Catalog catalog,
            final Optional<String> vendor) {
        final var selected = SelectItems.of(sql, scope, catalog);
        if (selected.isEmpty()) {
            return Optional.empty();
        }
        final var components = new ArrayList<JavaSourceComponent>();
        for (final var item : selected.get().items()) {
            final var source = item.sourceColumn();
            if (source.isEmpty()) {
                return Optional.empty();
            }
            final var columnName = unqualified(source.get());
            final var column = scope.tables().stream()
                    .map(catalog::table)
                    .flatMap(Optional::stream)
                    .filter(table -> table.column(columnName).isPresent())
                    .findFirst()
                    .flatMap(table -> table.column(columnName));
            if (column.isEmpty()) {
                return Optional.empty();
            }
            final var javaType = SqlTypes.javaType(column.get(), vendor);
            if (javaType.isEmpty()) {
                return Optional.empty();
            }
            components.add(new JavaSourceComponent(camelCase(item.resultName()), javaType.get()));
        }
        return components.isEmpty() ? Optional.empty() : Optional.of(components);
    }

    /**
     * @return the record itself, ready to be written next to the converter that builds it
     */
    public PackagedTypeSpec generateRecord(final JavaSourceType shape) {
        // JavaPoet takes a record's components as the parameters of its canonical constructor.
        final var canonical = MethodSpec.constructorBuilder();
        shape.components().forEach(component ->
                canonical.addParameter(ParameterSpec.builder(component.type(), component.name()).build()));
        final var record = TypeSpec.recordBuilder(shape.type().simpleName())
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("One row of a statement's result, as its columns describe it.\n")
                .addAnnotations(annotations.generatedClass())
                .recordConstructor(canonical.build())
                .build();
        return PackagedTypeSpec.of(record, shape.type().packageName());
    }

    /**
     * A column reads as {@code snake_case} and a component is written {@code camelCase}, which is
     * the same rule the converter applies in the other direction.
     */
    private static String camelCase(final String column) {
        final var parts = column.toLowerCase(Locale.ROOT).split("_");
        final var name = new StringBuilder(parts[0]);
        for (var index = 1; index < parts.length; index++) {
            if (parts[index].isEmpty()) {
                continue;
            }
            name.append(parts[index].substring(0, 1).toUpperCase(Locale.ROOT))
                    .append(parts[index].substring(1));
        }
        return name.toString();
    }

    private static String unqualified(final String column) {
        final var dot = column.lastIndexOf('.');
        return dot < 0 ? column : column.substring(dot + 1);
    }

}
