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
import wtf.metio.yosql.codegen.exceptions.CollidingResultColumnsException;
import wtf.metio.yosql.codegen.exceptions.ConflictingColumnTypeException;
import wtf.metio.yosql.codegen.exceptions.UnusableComponentNameException;
import wtf.metio.yosql.codegen.schema.Catalog;
import wtf.metio.yosql.codegen.schema.Column;
import wtf.metio.yosql.codegen.schema.SelectItems;
import wtf.metio.yosql.codegen.schema.SqlTypes;
import wtf.metio.yosql.codegen.schema.Schemas;
import wtf.metio.yosql.codegen.schema.TableScope;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        final var shapes = new ArrayList<List<JavaSourceComponent>>();
        for (final var catalog : schemas.applicableTo(configuration.vendor())) {
            componentsOf(sql, scope, catalog, configuration.vendor()).ifPresent(shapes::add);
        }
        if (shapes.isEmpty() || !describeTheSameRow(shapes)) {
            return Optional.empty();
        }
        rejectConflictingTypes(shapes, statement);
        final var components = shapes.getFirst();
        rejectUnusableNames(components, statement);
        rejectCollidingComponents(components, statement);
        return Optional.of(JavaSourceType.record(type, components, List.of(), List.of()));
    }

    /**
     * Why {@link #shapeOf} could not answer, in the words of whatever actually stopped it.
     *
     * <p>Four unrelated situations end in the same empty shape — nothing was read, the statement
     * selects from something with no columns to name, a column is not in the catalog, or a column is
     * there and its SQL type maps to nothing — and they are fixed in four different files. Listing
     * every remedy leaves the reader to work out which one they are, so each reason carries the one
     * that answers it and nothing else is printed.</p>
     *
     * <p>The type that maps to nothing is the reason worth naming outright: a vendor-specific type
     * resolves only for a declared vendor, so a Postgres schema read with no {@code vendor} set
     * describes {@code jsonb} and {@code timestamptz} perfectly well and then types neither.</p>
     *
     * <p>Runs only on the way to the exception, and repeats the walk {@link #shapeOf} just made
     * rather than making it carry a reason it discards on every successful build.</p>
     */
    public String whyNot(final SqlStatement statement) {
        final var vendor = statement.getConfiguration().vendor();
        if (schemas.isEmpty()) {
            // Before the per-column reasons, which would otherwise report every column of the
            // statement as one no table declares — true of an empty catalog, and no help at all.
            return say(new Reason("no schema was read at all", Remedy.MORE_DDL));
        }
        final var catalogs = schemas.applicableTo(vendor);
        if (catalogs.isEmpty()) {
            return say(new Reason("no schema describes " + vendor.orElse("this vendor"),
                    Remedy.MORE_DDL));
        }
        final var sql = statement.getRawStatement();
        final var scope = TableScope.of(sql);
        if (!scope.exhaustive()) {
            return say(new Reason("it selects from something the schema cannot name the columns of, "
                    + "such as a subquery or a derived table", Remedy.BY_HAND));
        }
        final var reasons = new LinkedHashSet<Reason>();
        catalogs.forEach(catalog -> reasons.addAll(reasonsAgainst(sql, scope, catalog, vendor)));
        if (reasons.isEmpty()) {
            return new Reason("the schemas that describe it do not agree on which columns it selects",
                    Remedy.BY_HAND).toString();
        }
        return say(reasons.toArray(Reason[]::new));
    }

    /**
     * What stopped the record being written, and what the reader can do about that particular thing.
     */
    private record Reason(String what, Remedy remedy) {
    }

    /**
     * The advice a reason comes with. Each is a sentence, and each is written at most once however
     * many columns asked for it — a table of JSON columns would otherwise repeat one until the
     * reasons themselves are hard to pick out.
     */
    private enum Remedy {

        VENDOR("Set 'schema.vendor', or a 'vendor' on the statement, so that the types only one "
                + "database has are looked up."),
        MORE_DDL("Add the 'create table' statements it reads from to the SQL files YoSQL parses, or "
                + "set 'schema.sqlStatementsDirectory' to where they live."),
        BY_HAND("Write the record by hand and drop 'generateResultRowType'.");

        private final String advice;

        Remedy(final String advice) {
            this.advice = advice;
        }

    }

    private static String say(final Reason... reasons) {
        final var what = Stream.of(reasons).map(Reason::what).collect(Collectors.joining("; "));
        final var advice = Stream.of(reasons)
                .map(Reason::remedy)
                .distinct()
                .sorted()
                .map(remedy -> remedy.advice)
                .collect(Collectors.joining(" "));
        return what + ". " + advice;
    }

    private static Set<Reason> reasonsAgainst(
            final String sql,
            final TableScope scope,
            final Catalog catalog,
            final Optional<String> vendor) {
        final var selected = SelectItems.of(sql, scope, catalog);
        if (selected.isEmpty()) {
            return Set.of(new Reason("the columns it selects cannot be matched to the schema",
                    Remedy.MORE_DDL));
        }
        final var reasons = new LinkedHashSet<Reason>();
        for (final var item : selected.get().items()) {
            final var source = item.sourceColumn();
            if (source.isEmpty()) {
                reasons.add(new Reason("'%s' is not a plain column".formatted(item.resultName()),
                        Remedy.BY_HAND));
                continue;
            }
            final var columnName = unqualified(source.get());
            final var table = catalog.declaringTable(source.get(), scope);
            if (table.isEmpty()) {
                reasons.add(new Reason("no table in scope declares '%s'".formatted(columnName),
                        Remedy.MORE_DDL));
                continue;
            }
            final var column = table.get().column(columnName);
            if (column.isEmpty()) {
                reasons.add(new Reason(
                        "table '%s' has no column '%s'".formatted(table.get().name(), columnName),
                        Remedy.MORE_DDL));
                continue;
            }
            final var dialect = catalog.dialect(vendor);
            if (SqlTypes.javaType(column.get(), dialect).isEmpty()) {
                reasons.add(untypedColumn(table.get().name(), column.get(), dialect));
            }
        }
        return reasons;
    }

    /**
     * The schema knows this column and still cannot say what it holds, which is the one reason that
     * writing more DDL does not answer. For a declared vendor it is a type nothing here maps, and
     * there is nothing the reader can set to change that — so the advice is to write the record.
     */
    private static Reason untypedColumn(
            final String table,
            final Column column,
            final Optional<String> dialect) {
        final var untyped = "'%s.%s' is declared '%s', which is not a type YoSQL maps"
                .formatted(table, column.name(), column.sqlType());
        return dialect
                .map(vendor -> new Reason(untyped + " for " + vendor, Remedy.BY_HAND))
                .orElseGet(() -> new Reason(untyped + " without a vendor", Remedy.VENDOR));
    }

    /**
     * A statement naming no vendor is the fallback for every database not named, so each catalog that
     * can describe it has a say. They have to be describing the same row for the question "what does
     * this record hold" to have one answer.
     */
    private static boolean describeTheSameRow(final List<List<JavaSourceComponent>> shapes) {
        final var names = shapes.getFirst().stream().map(JavaSourceComponent::name).toList();
        return shapes.stream()
                .allMatch(shape -> shape.stream().map(JavaSourceComponent::name).toList().equals(names));
    }

    /**
     * Where two databases genuinely disagree about what a column holds, one record cannot be both.
     * Taking whichever catalog answered first would decide that silently — and, since the catalogs
     * are only as ordered as the DDL that produced them, decide it differently on another machine.
     */
    private static void rejectConflictingTypes(
            final List<List<JavaSourceComponent>> shapes,
            final SqlStatement statement) {
        final var reference = shapes.getFirst();
        final var disagreements = new LinkedHashMap<String, Set<String>>();
        for (var index = 0; index < reference.size(); index++) {
            final var position = index;
            final var types = shapes.stream()
                    .map(shape -> shape.get(position).type().toString())
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            if (types.size() > 1) {
                disagreements.put(reference.get(position).name(), types);
            }
        }
        if (!disagreements.isEmpty()) {
            throw new ConflictingColumnTypeException(
                    statement.getSourcePath(), statement.getName(), disagreements);
        }
    }

    /**
     * A column is under no obligation to be a Java identifier, and `select lat, long from places` is
     * ordinary SQL. Without this, JavaPoet declares the component it can and then refuses the one it
     * cannot, naming neither the statement nor the column.
     */
    private static void rejectUnusableNames(
            final List<JavaSourceComponent> components,
            final SqlStatement statement) {
        for (final var component : components) {
            if (!SourceVersion.isIdentifier(component.name()) || SourceVersion.isKeyword(component.name())) {
                throw new UnusableComponentNameException(
                        statement.getSourcePath(), statement.getName(), component.name());
            }
        }
    }

    /**
     * A star over a join expands to what both tables declare, and two tables sharing a column name is
     * the ordinary case rather than the exotic one. JavaPoet will write {@code record Row(long id,
     * String slug, long id)} without complaint, so nothing but this stands between the reader and a
     * javac error in a file they never wrote.
     */
    private static void rejectCollidingComponents(
            final List<JavaSourceComponent> components,
            final SqlStatement statement) {
        final var seen = new HashSet<String>(components.size());
        for (final var component : components) {
            if (!seen.add(component.name())) {
                throw new CollidingResultColumnsException(
                        statement.getSourcePath(), statement.getName(), component.name());
            }
        }
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
            // Through the qualifier the select item kept, so that `c.id` reads customers' id rather
            // than whichever table in scope happens to declare an `id` first.
            final var column = catalog.declaringTable(source.get(), scope)
                    .flatMap(table -> table.column(columnName));
            if (column.isEmpty()) {
                return Optional.empty();
            }
            final var javaType = SqlTypes.javaType(column.get(), catalog.dialect(vendor));
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
        if (parts.length == 0) {
            // A column named with underscores alone splits into nothing at all.
            return column;
        }
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
