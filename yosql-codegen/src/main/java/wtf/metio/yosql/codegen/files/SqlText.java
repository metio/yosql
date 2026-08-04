/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.files;

/**
 * Tells apart the parts of a statement that mean something from the parts that are just text.
 *
 * <p>A colon inside a string literal is a character of that string, and a {@code ?} inside one is a
 * question mark. Reading either as a placeholder puts a parameter where the author wrote none, and
 * rewriting it corrupts the literal — {@code to_char(t, 'HH24:MI:SS')} becomes {@code 'HH24??S'}
 * with two parameters nobody can supply. The same goes for anything inside a comment.</p>
 *
 * <p>Rather than trying to write one regular expression that knows about literals, this walks the
 * statement once and returns a copy of the same length with every such stretch blanked out. Matching
 * happens against that copy and every offset still points at the original, so the callers keep
 * reading and rewriting the statement the author wrote.</p>
 */
public final class SqlText {

    private SqlText() {
        // utility class
    }

    /**
     * @param sql the statement as written
     * @return a copy of the same length with literals, quoted identifiers and comments blanked
     */
    public static String maskLiteralsAndComments(final String sql) {
        final var masked = sql.toCharArray();
        var index = 0;
        while (index < masked.length) {
            final var character = sql.charAt(index);
            index = switch (character) {
                case '\'' -> maskQuoted(sql, masked, index, '\'');
                case '"' -> maskQuoted(sql, masked, index, '"');
                case '-' -> starts(sql, index, "--") ? maskLineComment(sql, masked, index) : index + 1;
                case '/' -> starts(sql, index, "/*") ? maskBlockComment(sql, masked, index) : index + 1;
                case '$' -> maskDollarQuoted(sql, masked, index);
                default -> index + 1;
            };
        }
        return new String(masked);
    }

    /**
     * A quote doubled inside a literal is that character rather than its end, in both SQL's string
     * literals and its quoted identifiers.
     */
    private static int maskQuoted(final String sql, final char[] masked, final int start, final char quote) {
        var index = start + 1;
        masked[start] = ' ';
        while (index < sql.length()) {
            if (sql.charAt(index) == quote) {
                masked[index] = ' ';
                if (index + 1 < sql.length() && sql.charAt(index + 1) == quote) {
                    masked[index + 1] = ' ';
                    index += 2;
                    continue;
                }
                return index + 1;
            }
            blank(masked, index);
            index++;
        }
        // Unterminated: the rest of the statement is inside it, and complaining is the parser's job.
        return index;
    }

    private static int maskLineComment(final String sql, final char[] masked, final int start) {
        var index = start;
        while (index < sql.length() && sql.charAt(index) != '\n') {
            blank(masked, index);
            index++;
        }
        return index;
    }

    /**
     * Block comments nest in PostgreSQL, so the first {@code *&#47;} does not necessarily end one.
     */
    private static int maskBlockComment(final String sql, final char[] masked, final int start) {
        var index = start;
        var depth = 0;
        while (index < sql.length()) {
            if (starts(sql, index, "/*")) {
                depth++;
                blank(masked, index);
                blank(masked, index + 1);
                index += 2;
            } else if (starts(sql, index, "*/")) {
                depth--;
                blank(masked, index);
                blank(masked, index + 1);
                index += 2;
                if (depth == 0) {
                    return index;
                }
            } else {
                blank(masked, index);
                index++;
            }
        }
        return index;
    }

    /**
     * PostgreSQL's dollar quoting, where {@code $tag$} opens and the same {@code $tag$} closes — the
     * spelling a function body uses precisely because it holds text that would otherwise need
     * escaping.
     */
    private static int maskDollarQuoted(final String sql, final char[] masked, final int start) {
        final var tagEnd = sql.indexOf('$', start + 1);
        if (tagEnd < 0) {
            return start + 1;
        }
        final var tag = sql.substring(start, tagEnd + 1);
        if (!tag.substring(1, tag.length() - 1).chars().allMatch(SqlText::tagCharacter)) {
            return start + 1;
        }
        final var close = sql.indexOf(tag, tagEnd + 1);
        final var end = close < 0 ? sql.length() : close + tag.length();
        for (var index = start; index < end; index++) {
            blank(masked, index);
        }
        return end;
    }

    private static boolean tagCharacter(final int character) {
        return Character.isLetterOrDigit(character) || character == '_';
    }

    /**
     * Newlines stay, so that a line comment still ends where it ended and offsets read the same.
     */
    private static void blank(final char[] masked, final int index) {
        if (index < masked.length && masked[index] != '\n') {
            masked[index] = ' ';
        }
    }

    private static boolean starts(final String sql, final int index, final String token) {
        return sql.startsWith(token, index);
    }

}
