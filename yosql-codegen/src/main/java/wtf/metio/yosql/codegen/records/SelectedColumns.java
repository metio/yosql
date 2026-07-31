/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * The names a statement's result set will carry, read off the select list.
 *
 * <p>Knowing them is what lets a mismatch between a query and a record fail the build rather than
 * the query. It is deliberately a narrow reading, not a SQL parser: the select list is enumerable
 * when every item is a plain column or carries an alias, and not otherwise. {@code select *} names
 * nothing the generator can see, and neither does an unaliased expression — for those the answer is
 * "unknown", and the check is skipped rather than guessed at.</p>
 */
public final class SelectedColumns {

    private static final Pattern SELECT = Pattern.compile("^\\s*select\\s+(distinct\\s+)?", Pattern.CASE_INSENSITIVE);
    private static final Pattern ALIAS = Pattern.compile("\\s+as\\s+([A-Za-z_][\\w$]*)\\s*$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PLAIN_COLUMN = Pattern.compile("^[A-Za-z_][\\w$]*(?:\\s*\\.\\s*[A-Za-z_][\\w$]*)?$");
    private static final Pattern QUOTED_ALIAS = Pattern.compile("\\s+as\\s+\"([^\"]+)\"\\s*$", Pattern.CASE_INSENSITIVE);

    /**
     * @param statement the statement's SQL, with its front matter already removed
     * @return the selected names, or empty when the select list cannot be enumerated
     */
    public static Optional<List<String>> of(final String statement) {
        final var sql = statement.strip();
        final var select = SELECT.matcher(sql);
        if (!select.find()) {
            return Optional.empty();
        }
        final var from = topLevelFrom(sql, select.end());
        if (from < 0) {
            return Optional.empty();
        }
        final var columns = new ArrayList<String>();
        for (final var item : splitTopLevel(sql.substring(select.end(), from))) {
            final var name = nameOf(item.strip());
            if (name.isEmpty()) {
                return Optional.empty();
            }
            columns.add(name.get());
        }
        return columns.isEmpty() ? Optional.empty() : Optional.of(columns);
    }

    private static Optional<String> nameOf(final String item) {
        final var quoted = QUOTED_ALIAS.matcher(item);
        if (quoted.find()) {
            return Optional.of(quoted.group(1));
        }
        final var alias = ALIAS.matcher(item);
        if (alias.find()) {
            return Optional.of(alias.group(1).toLowerCase(Locale.ROOT));
        }
        if (PLAIN_COLUMN.matcher(item).matches()) {
            final var dot = item.lastIndexOf('.');
            return Optional.of(item.substring(dot + 1).strip().toLowerCase(Locale.ROOT));
        }
        return Optional.empty();
    }

    /**
     * The {@code from} that closes this select — not one belonging to a subquery, and not the word
     * appearing inside a string literal or an {@code extract(… from …)}.
     */
    private static int topLevelFrom(final String sql, final int start) {
        var depth = 0;
        var index = start;
        while (index < sql.length()) {
            final var character = sql.charAt(index);
            if (character == '\'' || character == '"') {
                index = endOfLiteral(sql, index, character);
                continue;
            }
            if (character == '(') {
                depth++;
            } else if (character == ')') {
                depth--;
            } else if (depth == 0 && isWordAt(sql, index, "from")) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private static boolean isWordAt(final String sql, final int index, final String word) {
        if (index + word.length() > sql.length()) {
            return false;
        }
        if (!sql.regionMatches(true, index, word, 0, word.length())) {
            return false;
        }
        final var before = index == 0 || !isWordCharacter(sql.charAt(index - 1));
        final var afterIndex = index + word.length();
        final var after = afterIndex >= sql.length() || !isWordCharacter(sql.charAt(afterIndex));
        return before && after;
    }

    private static boolean isWordCharacter(final char character) {
        return Character.isLetterOrDigit(character) || character == '_' || character == '$';
    }

    private static int endOfLiteral(final String sql, final int start, final char quote) {
        var index = start + 1;
        while (index < sql.length() && sql.charAt(index) != quote) {
            index++;
        }
        return index + 1;
    }

    private static List<String> splitTopLevel(final String list) {
        final var parts = new ArrayList<String>();
        final var current = new StringBuilder();
        var depth = 0;
        var index = 0;
        while (index < list.length()) {
            final var character = list.charAt(index);
            if (character == '\'' || character == '"') {
                final var end = Math.min(endOfLiteral(list, index, character), list.length());
                current.append(list, index, end);
                index = end;
                continue;
            }
            if (character == '(') {
                depth++;
            } else if (character == ')') {
                depth--;
            }
            if (character == ',' && depth == 0) {
                parts.add(current.toString());
                current.setLength(0);
            } else {
                current.append(character);
            }
            index++;
        }
        if (!current.toString().isBlank()) {
            parts.add(current.toString());
        }
        return parts;
    }

    private SelectedColumns() {
        // constant class
    }

}
