/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.exceptions.SchemaMismatchException;
import wtf.metio.yosql.codegen.records.ColumnNames;
import wtf.metio.yosql.codegen.records.RecordScanner;
import wtf.metio.yosql.models.configuration.SchemaValidation;
import wtf.metio.yosql.codegen.dao.InLists;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.immutables.SchemaConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Checks a statement against the schema it runs against.
 *
 * <p>Three things are checked, and each is skipped the moment anything it depends on is unknown: a
 * column no table in scope declares, a result row component that cannot hold what its column can
 * contain, and a parameter whose declared type disagrees with the column it is compared against.</p>
 *
 * <p>A statement is checked against every catalog it could run against — one when it names a
 * vendor, all of them when it does not, because a statement naming no vendor is the fallback for
 * every database not named. A disagreement in any of them is a disagreement.</p>
 */
public final class SchemaValidator {

    private final Schemas schemas;
    private final SchemaConfiguration configuration;
    private final RecordScanner scanner;
    private final LocLogger logger;

    public SchemaValidator(
            final Schemas schemas,
            final SchemaConfiguration configuration,
            final RecordScanner scanner,
            final LocLogger logger) {
        this.schemas = schemas;
        this.configuration = configuration;
        this.scanner = scanner;
        this.logger = logger;
    }

    public void validate(final List<SqlStatement> statements) {
        if (configuration.validation() == SchemaValidation.OFF || schemas.isEmpty()) {
            return;
        }
        statements.forEach(this::validate);
    }

    private void validate(final SqlStatement statement) {
        final var config = statement.getConfiguration();
        if (config.validateSchema().map(Boolean.FALSE::equals).orElse(Boolean.FALSE)) {
            return;
        }
        final var sql = statement.getRawStatement();
        final var scope = TableScope.of(sql);
        if (!scope.exhaustive()) {
            // A subquery, a CTE, or SQL nothing here could parse. Any of those can produce a column
            // the catalog never saw, so nothing said about this statement would be evidence.
            return;
        }
        final var complaints = new ArrayList<String>();
        for (final var catalog : schemas.applicableTo(config.vendor())) {
            complaints.addAll(unknownColumns(sql, scope, catalog));
            complaints.addAll(disagreeingParameters(config.parameters(), scope, catalog, config.vendor()));
            complaints.addAll(disagreeingComponents(config.resultRowType(), sql, scope, catalog, config.vendor()));
        }
        if (complaints.isEmpty()) {
            return;
        }
        final var name = config.name().orElse("<unnamed>");
        if (configuration.validation() == SchemaValidation.WARN) {
            complaints.stream().distinct().forEach(complaint ->
                    logger.warn("Statement '{}' in {}: {}", name, statement.getSourcePath(), complaint));
            return;
        }
        throw new SchemaMismatchException(statement.getSourcePath(), name, complaints.stream().distinct().toList());
    }

    /**
     * A column the statement reads that no table in scope declares.
     *
     * <p>What is checked is the column an item <em>reads</em>, never the name it gives the result:
     * {@code amount_cents as minor_units} reads {@code amount_cents}, and complaining that no table
     * has a {@code minor_units} would report every aliased query as a mistake.</p>
     */
    private List<String> unknownColumns(final String sql, final TableScope scope, final Catalog catalog) {
        final var selected = SelectItems.of(sql, scope, catalog);
        if (selected.isEmpty() || !knowsEveryTable(scope, catalog)) {
            return List.of();
        }
        final var complaints = new ArrayList<String>();
        for (final var column : selected.get().sourceColumns()) {
            final var qualifier = qualifierOf(column);
            final var bare = unqualified(column);
            if (qualifier.isPresent()) {
                final var table = scope.resolve(qualifier.get()).flatMap(catalog::table);
                if (table.isPresent() && table.get().column(bare).isEmpty()) {
                    complaints.add("no column '%s' in '%s', which reads as %s%s"
                            .formatted(bare, table.get().name(), columnList(table.get()),
                                    unfollowedNote(catalog, table.get().name())));
                }
            } else if (catalog.declaringTable(bare, scope).isEmpty()) {
                complaints.add("no column '%s' in %s, which read as %s".formatted(bare, tableList(scope),
                        scope.tables().stream()
                                .map(catalog::table)
                                .flatMap(Optional::stream)
                                .map(known -> "'%s' %s".formatted(known.name(), columnList(known)))
                                .collect(Collectors.joining(", "))));
            }
        }
        return complaints;
    }

    /**
     * A parameter whose declared type is not the one its column reads into. Only a parameter that
     * shares a column's name is checked: which column a parameter is compared against is a question
     * about the where clause, and answering it wrongly would produce complaints nobody could act on.
     */
    private List<String> disagreeingParameters(
            final List<SqlParameter> parameters,
            final TableScope scope,
            final Catalog catalog,
            final Optional<String> vendor) {
        final var complaints = new ArrayList<String>();
        for (final var parameter : parameters) {
            final var name = parameter.name().orElse("");
            // A collection binds one value per placeholder, so what its column has to agree
            // with is what it holds rather than the collection itself.
            final var declared = InLists.elementType(parameter);
            if (name.isBlank() || declared.isEmpty()) {
                continue;
            }
            namedColumn(name, scope, catalog)
                    .flatMap(column -> SqlTypes.javaType(column, catalog.dialect(vendor)))
                    .filter(expected -> !compatible(expected, declared.get()))
                    .ifPresent(expected -> complaints.add(
                            "parameter '%s' is declared %s but the column it names reads as %s"
                                    .formatted(name, declared.get(), expected)));
        }
        return complaints;
    }

    /**
     * A result row component that cannot hold what its column can. The one that matters is a
     * nullable column read into a primitive: it compiles, and throws on the first row that has a
     * null in it.
     */
    private List<String> disagreeingComponents(
            final Optional<String> resultRowType,
            final String sql,
            final TableScope scope,
            final Catalog catalog,
            final Optional<String> vendor) {
        final var type = resultRowType.map(String::strip).filter(Predicate.not(String::isEmpty));
        final var selected = SelectItems.of(sql, scope, catalog);
        if (type.isEmpty() || selected.isEmpty()) {
            return List.of();
        }
        final var record = className(type.get()).flatMap(scanner::scan).filter(it -> it.isRecord());
        if (record.isEmpty()) {
            return List.of();
        }
        final var complaints = new ArrayList<String>();
        for (final var component : record.get().components()) {
            final var column = columnFor(component.name(), selected.get(), scope, catalog);
            if (column.isEmpty()) {
                continue;
            }
            if (column.get().nullable() && component.type().isPrimitive()) {
                complaints.add("component '%s' is %s but column '%s' is nullable"
                        .formatted(component.name(), component.type(), column.get().name()));
                continue;
            }
            SqlTypes.javaType(column.get(), catalog.dialect(vendor))
                    .filter(expected -> !compatible(expected, component.type()))
                    .ifPresent(expected -> complaints.add(
                            "component '%s' is %s but column '%s' reads as %s"
                                    .formatted(component.name(), component.type(), column.get().name(), expected)));
        }
        return complaints;
    }

    /**
     * The column a component reads: the one whose name it matches, read as {@code snake_case}, and
     * only when the query actually selects it.
     */
    private Optional<Column> columnFor(
            final String component,
            final SelectItems selected,
            final TableScope scope,
            final Catalog catalog) {
        // A component matches the name the row carries, and then the column behind that name is
        // what says whether it can hold it. For `created_at as at` those are different words.
        final var resultName = snakeCase(component);
        final var source = selected.sourceOf(resultName);
        if (source.isEmpty()) {
            return Optional.empty();
        }
        final var columnName = unqualified(source.get());
        return catalog.declaringTable(source.get(), scope).flatMap(table -> table.column(columnName));
    }

    /**
     * Whether a component's or parameter's declared type can stand for what the column reads as.
     *
     * <p>Generous on purpose. A type of the reader's own — a {@code TenantId} wrapping a
     * {@code UUID}, an enum written as text — is the point of the converter machinery, and
     * complaining about one would make this useless. Only a disagreement between two types the
     * generator itself knows how to read is worth reporting.</p>
     */
    private static boolean compatible(final TypeName expected, final TypeName declared) {
        if (expected.equals(declared) || expected.box().equals(declared.box())) {
            return true;
        }
        // Anything the type map never produces is a type of the caller's own, and its converter
        // decides what it reads from.
        return !PRODUCED_BY_THE_TYPE_MAP.contains(declared.box().toString());
    }

    private static final List<String> PRODUCED_BY_THE_TYPE_MAP = List.of(
            "java.lang.String", "java.lang.Short", "java.lang.Integer", "java.lang.Long",
            "java.lang.Boolean", "java.lang.Float", "java.lang.Double", "java.lang.Byte",
            "java.math.BigDecimal", "java.time.Instant", "java.time.LocalDate", "java.time.LocalTime",
            "java.time.LocalDateTime", "java.util.UUID", "byte[]");

    /**
     * Checks only run when the catalog knows every table the statement reads from. A statement
     * touching one table the catalog never saw could take any of its columns from there.
     */
    private static boolean knowsEveryTable(final TableScope scope, final Catalog catalog) {
        return !scope.tables().isEmpty()
                && scope.tables().stream().allMatch(table -> catalog.table(table).isPresent());
    }

    /**
     * What the reader passed over while assembling this table, said beside the column that is not
     * there.
     *
     * <p>The two questions a reader has at this point are "is my query wrong" and "is the schema
     * short", and the columns alone answer only the first. A migration this reader skipped is the
     * commonest way the second happens, and naming it turns bisecting a migration directory into
     * reading one line.</p>
     */
    private static String unfollowedNote(final Catalog catalog, final String table) {
        final var passedOver = new ArrayList<String>(catalog.unfollowedFor(table));
        catalog.unparsedMentioning(table).forEach(statement ->
                passedOver.add(statement + " — this reader could not parse it"));
        return passedOver.isEmpty() ? "" : ". Reading the schema, this was passed over: "
                + String.join("; ", passedOver);
    }

    /**
     * The column a parameter names, spelled either as the column is or in the {@code camelCase} a
     * Java parameter is written in — the same two spellings the type is inferred from, so that a
     * parameter typed from {@code account_id} is also checked against it.
     */
    private static Optional<Column> namedColumn(
            final String parameter,
            final TableScope scope,
            final Catalog catalog) {
        final var literal = catalog.declaringTable(parameter, scope)
                .flatMap(table -> table.column(parameter));
        if (literal.isPresent()) {
            return literal;
        }
        final var column = ColumnNames.columnFor(parameter);
        return catalog.declaringTable(column, scope).flatMap(table -> table.column(column));
    }

    private static String tableList(final TableScope scope) {
        return "'" + String.join("', '", scope.tables()) + "'";
    }

    /**
     * What the catalog holds for a table, said out loud beside a column it does not hold.
     *
     * <p>"No column 'update_window' in 'tenant'" reads as a mistake in the query, and is just as
     * often a schema this reader assembled short — a migration it could not follow, a directory it
     * was not pointed at, an {@code alter table} in a dialect it does not parse. The reader can tell
     * those apart at a glance once the message says what the table did come out holding, and cannot
     * tell them apart at all without it.</p>
     */
    private static String columnList(final Table table) {
        return table.columnNames().isEmpty()
                ? "a table with no columns at all"
                : "'" + String.join("', '", table.columnNames()) + "'";
    }

    private static Optional<String> qualifierOf(final String column) {
        final var dot = column.indexOf('.');
        return dot < 0 ? Optional.empty() : Optional.of(column.substring(0, dot));
    }

    private static String unqualified(final String column) {
        final var dot = column.lastIndexOf('.');
        return dot < 0 ? column : column.substring(dot + 1);
    }

    /**
     * The same rule the generator reads a component by, rather than a second one that agrees with it
     * most of the time. Where they disagreed — a run of capitals, so {@code isVATRate} — this looked
     * up a column that does not exist, found nothing, and skipped the check it exists to make.
     */
    private static String snakeCase(final String component) {
        return ColumnNames.columnFor(component);
    }

    private static Optional<ClassName> className(final String type) {
        try {
            return Optional.of(ClassName.bestGuess(type));
        } catch (final IllegalArgumentException _) {
            return Optional.empty();
        }
    }

}
