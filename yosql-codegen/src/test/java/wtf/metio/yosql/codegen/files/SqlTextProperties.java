/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import org.junit.jupiter.api.Assertions;

import java.util.List;

/**
 * What masking a statement must hold true for, whatever the statement.
 *
 * <p>The example-based tests here name the cases somebody thought of. These name what has to be true
 * of every case, and let jqwik go looking for one that is not — which is the useful direction for a
 * function whose whole job is to survive input nobody anticipated.</p>
 */
class SqlTextProperties {

    /**
     * Fragments a statement is built from, mixing the things that end a literal or a comment with
     * the things that only look like they do.
     */
    @Provide
    Arbitrary<String> sqlFragments() {
        return Arbitraries.of(
                "select", " ", "\n", "id", ",", "from", "t", "where", "a", "=", ":name", "?",
                "'", "\"", "--", "/*", "*/", "$$", "$tag$", "::", "text", "'it''s'", "\t",
                ":", "'unterminated", "/* nested /* deeper */ */", "e'\\''", "%", "(", ")");
    }

    @Provide
    Arbitrary<String> statements() {
        return sqlFragments().list().ofMinSize(0).ofMaxSize(40).map(parts -> String.join("", parts));
    }

    /**
     * Offsets have to keep pointing at the same characters, because every caller matches against the
     * mask and then reads or rewrites the statement itself.
     */
    @Property(tries = 2000)
    void maskKeepsEveryOffset(@ForAll("statements") final String sql) {
        Assertions.assertEquals(sql.length(), SqlText.maskLiteralsAndComments(sql).length(),
                () -> "mask changed length for: " + sql);
    }

    /**
     * Masking says which characters are code. Asking again cannot change that answer, and a function
     * that is not idempotent here would mean the first answer depended on something it had already
     * removed.
     */
    @Property(tries = 2000)
    void maskIsIdempotent(@ForAll("statements") final String sql) {
        final var once = SqlText.maskLiteralsAndComments(sql);
        Assertions.assertEquals(once, SqlText.maskLiteralsAndComments(once),
                () -> "masking twice differs from masking once for: " + sql);
    }

    /**
     * A character is either left alone or blanked. Turning one character into a different character
     * would put a placeholder where the author wrote none — the very failure this exists to stop.
     */
    @Property(tries = 2000)
    void maskOnlyBlanks(@ForAll("statements") final String sql) {
        final var masked = SqlText.maskLiteralsAndComments(sql);
        for (var index = 0; index < sql.length(); index++) {
            final var position = index;
            final var original = sql.charAt(index);
            final var result = masked.charAt(index);
            Assertions.assertTrue(result == original || result == ' ',
                    () -> "character at " + position + " became '" + result + "' in: " + sql);
        }
    }

    /**
     * Newlines survive, because a line comment ends at one and blanking it would swallow the rest of
     * the statement.
     */
    @Property(tries = 2000)
    void maskKeepsNewlines(@ForAll("statements") final String sql) {
        final var masked = SqlText.maskLiteralsAndComments(sql);
        for (var index = 0; index < sql.length(); index++) {
            if (sql.charAt(index) == '\n') {
                Assertions.assertEquals('\n', masked.charAt(index),
                        () -> "a newline was blanked in: " + sql);
            }
        }
    }

    /**
     * A statement carrying none of the characters that open a literal or a comment is all code, so
     * the mask is the statement.
     */
    @Property(tries = 1000)
    void plainSqlIsUntouched(@ForAll("plainStatements") final String sql) {
        Assertions.assertEquals(sql, SqlText.maskLiteralsAndComments(sql),
                () -> "code was blanked in: " + sql);
    }

    @Provide
    Arbitrary<String> plainStatements() {
        return Arbitraries.of("select", " ", "\n", "id", ",", "from", "t", "where", "=", ":name",
                        "?", "::", "(", ")", "a", "1")
                .list().ofMaxSize(30).map(parts -> String.join("", parts));
    }

    /**
     * Whatever is inside a literal is text. Wrapping any fragment in quotes has to hide every
     * placeholder in it, however the fragment is spelled.
     */
    @Property(tries = 2000)
    void quotedFragmentsHideTheirPlaceholders(@ForAll("quotable") final String fragment) {
        final var sql = "select * from t where a = '" + fragment + "' and b = :b";
        final var masked = SqlText.maskLiteralsAndComments(sql);
        final var literal = masked.substring(sql.indexOf('\'') , sql.lastIndexOf('\'') + 1);
        Assertions.assertTrue(literal.isBlank(),
                () -> "a literal kept content [" + literal + "] in: " + sql);
        Assertions.assertTrue(masked.contains(":b"),
                () -> "the parameter after the literal was lost in: " + sql);
    }

    /**
     * Without a quote of its own, so that the fragment cannot close the literal it is put inside.
     */
    @Provide
    Arbitrary<String> quotable() {
        return Arbitraries.of(":id", "?", "--", "/*", "*/", "20:30", "a", " ", "::", "$$", "%s")
                .list().ofMaxSize(8).map(parts -> String.join("", parts));
    }

    /**
     * Every placeholder the parser finds sits outside a literal — which is the same statement
     * SqlText makes, read from the other end.
     */
    @Property(tries = 2000)
    void everyFoundParameterIsCode(@ForAll("statements") final String sql) {
        final var masked = SqlText.maskLiteralsAndComments(sql);
        SqlStatementParser.extractParameterIndices(sql).keySet().stream()
                .filter(name -> !SqlStatementParser.UNNAMED_PARAMETERS.equals(name))
                .forEach(name -> Assertions.assertTrue(masked.contains(":" + name),
                        () -> "parameter '" + name + "' is not in the masked statement: " + sql));
    }

    /**
     * A placeholder's number is its position among all of them, so the numbers a statement yields
     * are exactly 1..n with nothing missing and nothing repeated. A gap means a JDBC index nobody
     * binds; a repeat means two values bound to one.
     */
    @Property(tries = 2000)
    void indicesAreContiguousFromOne(@ForAll("statements") final String sql) {
        final var indices = SqlStatementParser.extractParameterIndices(sql).values().stream()
                .flatMap(List::stream)
                .sorted()
                .toList();
        for (var offset = 0; offset < indices.size(); offset++) {
            final var expected = offset + 1;
            final var position = offset;
            Assertions.assertEquals(expected, indices.get(position),
                    () -> "indices " + indices + " are not 1.." + indices.size() + " for: " + sql);
        }
    }

    /**
     * Reading a statement cannot fail. Every diagnostic about a statement is raised later by
     * something that knows which file it came from, so an exception escaping here would name
     * nothing.
     */
    @Property(tries = 2000)
    void readingNeverThrows(@ForAll("statements") final String sql) {
        Assertions.assertDoesNotThrow(() -> {
            SqlText.maskLiteralsAndComments(sql);
            SqlStatementParser.extractParameterIndices(sql);
        }, () -> "reading threw for: " + sql);
    }

    /**
     * Arbitrary text, not only text built from SQL-shaped pieces: a `.sql` file can hold anything a
     * person can type.
     */
    @Property(tries = 2000)
    void readingNeverThrowsForArbitraryText(@ForAll final String text) {
        Assertions.assertDoesNotThrow(() -> {
            SqlText.maskLiteralsAndComments(text);
            SqlStatementParser.extractParameterIndices(text);
        }, () -> "reading threw for: " + text);
    }

    /**
     * A statement whose parameters are all named binds as many placeholders as it writes names.
     */
    @Property(tries = 1000)
    void namedParametersAreCountedOnce(@ForAll("names") final List<String> names) {
        final var sql = "select * from t where "
                + String.join(" and ", names.stream().map(name -> name + " = :" + name).toList());
        final var indices = SqlStatementParser.extractParameterIndices(sql);
        Assertions.assertEquals(names.stream().distinct().count(), indices.size(),
                () -> "wrong number of parameters for: " + sql);
    }

    @Provide
    Arbitrary<List<String>> names() {
        return Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(6)
                .list().ofMinSize(1).ofMaxSize(6);
    }


}
