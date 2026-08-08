/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import wtf.metio.yosql.codegen.files.SqlText;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Reads the columns out of a {@code create table} the SQL parser refused.
 *
 * <p>The parser covers a great deal of SQL and never all of it. A column-level
 * {@code references other (id) on delete set null} is ordinary PostgreSQL and JSqlParser 5.3 stops
 * at the {@code set} — as it does for {@code on delete restrict} and every column-level referential
 * action but {@code cascade}. A table it cannot parse is absent from the catalog, so one clause in
 * one column costs the whole table: its statements go unchecked, no parameter reading it can be
 * typed, and no record can be written from it. In one real schema that was forty tables.</p>
 *
 * <p>So the declaration is read structurally instead — the name, then each item of the parenthesised
 * body, taking the leading identifier as a column and what follows as its type. That is a far weaker
 * reading than a parse and it is applied only where the parse already failed, so nothing that parses
 * changes.</p>
 *
 * <p>It refuses rather than guesses. An item it cannot read as a column leaves the whole table
 * unread, because a table missing one column is worse than a table nobody has: the missing one is
 * reported as a mistake in every statement that selects it.</p>
 */
final class LenientCreateTable {

    private static final Pattern HEADER = Pattern.compile(
            "^\\s*create\\s+(?:(?:global|local|temporary|temp|unlogged|external)\\s+)*table\\s+"
                    + "(?:if\\s+not\\s+exists\\s+)?([^\\s(]+)\\s*\\(",
            Pattern.CASE_INSENSITIVE);

    /**
     * Words that end a type and begin what is said about the column. Everything before the first of
     * them is the type, which is what lets {@code timestamp with time zone} and
     * {@code double precision} through without listing them.
     */
    private static final Set<String> ENDS_THE_TYPE = Set.of(
            "not", "null", "default", "references", "check", "primary", "unique", "constraint",
            "generated", "collate", "identity", "auto_increment", "on", "comment", "storage",
            "compression", "using", "as");

    /**
     * Items of the body that describe the table rather than a column.
     */
    private static final Set<String> TABLE_CONSTRAINT = Set.of(
            "primary", "foreign", "unique", "check", "constraint", "exclude", "like", "period");

    private LenientCreateTable() {
        // utility class, call #read() directly
    }

    /**
     * @return the table, or empty when this cannot read every item of the declaration
     */
    static Optional<Table> read(final String sql) {
        // Offsets are preserved by the masking, so structure is found in the masked text and every
        // name and type is taken from the statement as it was written.
        final var masked = SqlText.maskLiteralsAndComments(sql);
        final var header = HEADER.matcher(masked);
        if (!header.find()) {
            return Optional.empty();
        }
        final var body = body(masked, header.end() - 1);
        if (body.isEmpty()) {
            return Optional.empty();
        }
        final var columns = new LinkedHashMap<String, Column>();
        final var primaryKeys = new java.util.ArrayList<String>();
        for (final var bounds : items(masked, header.end(), body.getAsInt())) {
            final var item = sql.substring(bounds.start(), bounds.end()).strip();
            final var words = masked.substring(bounds.start(), bounds.end()).strip().split("\\s+");
            if (words.length == 0 || words[0].isEmpty()) {
                // Nothing left once the literals and comments are blanked. An item that was empty to
                // begin with is the comma a declaration is allowed to trail; one that held something
                // held only a literal or a comment, and this cannot say what it was.
                if (item.isEmpty()) {
                    continue;
                }
                return Optional.empty();
            }
            if (TABLE_CONSTRAINT.contains(words[0].toLowerCase(Locale.ROOT))) {
                primaryKeys.addAll(primaryKeyColumns(words, item));
                continue;
            }
            final var column = column(item, words);
            if (column.isEmpty()) {
                return Optional.empty();
            }
            columns.put(Catalog.normalize(column.get().name()), column.get());
        }
        if (columns.isEmpty()) {
            return Optional.empty();
        }
        for (final var key : primaryKeys) {
            columns.computeIfPresent(key, (_, column) ->
                    new Column(column.name(), column.sqlType(), false));
        }
        return Optional.of(new Table(Catalog.unquote(header.group(1)), columns));
    }

    /**
     * @return where the body's closing parenthesis is, or empty when it is unbalanced
     */
    private static java.util.OptionalInt body(final String masked, final int open) {
        var depth = 0;
        for (var index = open; index < masked.length(); index++) {
            final var character = masked.charAt(index);
            if (character == '(') {
                depth++;
            } else if (character == ')') {
                depth--;
                if (depth == 0) {
                    return java.util.OptionalInt.of(index);
                }
            }
        }
        return java.util.OptionalInt.empty();
    }

    /**
     * The body's items, cut at the commas that separate them — the ones at the body's own depth, so
     * that {@code numeric(6, 2)} and {@code primary key (a, b)} stay whole.
     */
    private static List<Bounds> items(final String masked, final int from, final int close) {
        final var bounds = new java.util.ArrayList<Bounds>();
        var depth = 0;
        var start = from;
        for (var index = from; index < close; index++) {
            final var character = masked.charAt(index);
            if (character == '(') {
                depth++;
            } else if (character == ')') {
                depth--;
            } else if (character == ',' && depth == 0) {
                bounds.add(new Bounds(start, index));
                start = index + 1;
            }
        }
        bounds.add(new Bounds(start, close));
        return bounds;
    }

    private static Optional<Column> column(final String item, final String[] words) {
        final var name = Catalog.unquote(words[0]);
        if (name.isBlank() || !isIdentifier(name)) {
            return Optional.empty();
        }
        final var type = new StringBuilder();
        var index = 1;
        while (index < words.length && !ENDS_THE_TYPE.contains(bareWord(words[index]))) {
            if (!type.isEmpty()) {
                type.append(' ');
            }
            type.append(words[index]);
            index++;
        }
        if (type.isEmpty()) {
            // A column with no type at all is not something this can read as a column.
            return Optional.empty();
        }
        final var specs = List.of(words).subList(Math.min(index, words.length), words.length);
        return Optional.of(new Column(name, type.toString(), nullable(specs, item)));
    }

    /**
     * The same rule the parsed path applies: a column holds nulls unless its declaration says
     * otherwise, and a primary key says otherwise whether or not it also says {@code not null}.
     */
    private static boolean nullable(final List<String> specs, final String item) {
        for (var index = 0; index < specs.size() - 1; index++) {
            final var word = bareWord(specs.get(index));
            final var next = bareWord(specs.get(index + 1));
            if ("not".equals(word) && "null".equals(next)) {
                return false;
            }
            if ("primary".equals(word) && "key".equals(next)) {
                return false;
            }
        }
        return true;
    }

    private static List<String> primaryKeyColumns(final String[] words, final String item) {
        if (words.length < 2 || !"primary".equals(bareWord(words[0])) || !"key".equals(bareWord(words[1]))) {
            return List.of();
        }
        final var open = item.indexOf('(');
        final var close = item.lastIndexOf(')');
        if (open < 0 || close < open) {
            return List.of();
        }
        return List.of(item.substring(open + 1, close).split(","))
                .stream()
                .map(String::strip)
                .filter(name -> !name.isEmpty())
                .map(Catalog::normalize)
                .toList();
    }

    /**
     * A word as it is compared against the keywords: lower case, and without the punctuation a
     * declaration runs together with it — {@code null,} at the end of an item, {@code varchar(64)}.
     */
    private static String bareWord(final String word) {
        final var bare = word.toLowerCase(Locale.ROOT);
        final var parenthesis = bare.indexOf('(');
        return parenthesis < 0 ? bare : bare.substring(0, parenthesis);
    }

    private static boolean isIdentifier(final String name) {
        if (name.isEmpty() || Character.isDigit(name.charAt(0))) {
            return false;
        }
        return name.chars().allMatch(character ->
                Character.isLetterOrDigit(character) || character == '_' || character == '$');
    }

    private record Bounds(int start, int end) {
    }

}
