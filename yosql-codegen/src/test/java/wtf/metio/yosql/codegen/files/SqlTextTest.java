/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("SqlText")
class SqlTextTest {

    @Nested
    @DisplayName("keeps the statement's shape")
    class Shape {

        @Test
        @DisplayName("returns a mask of the same length, so offsets read the same")
        void keepsLength() {
            final var sql = "select * from t where a = 'x:y' -- :z\nand b = :b";
            Assertions.assertEquals(sql.length(), SqlText.maskLiteralsAndComments(sql).length());
        }

        @Test
        @DisplayName("leaves everything outside a literal alone")
        void keepsCodeVerbatim() {
            final var sql = "select a from t where b = :b";
            Assertions.assertEquals(sql, SqlText.maskLiteralsAndComments(sql));
        }

        @Test
        @DisplayName("keeps newlines, so a line comment still ends where it did")
        void keepsNewlines() {
            final var masked = SqlText.maskLiteralsAndComments("-- comment\nselect 1");
            Assertions.assertEquals("          \nselect 1", masked);
        }

    }

    @Nested
    @DisplayName("blanks what is only text")
    class Blanks {

        @Test
        @DisplayName("a string literal, colons and all")
        void blanksStringLiterals() {
            final var masked = SqlText.maskLiteralsAndComments("where t = 'HH24:MI:SS'");
            Assertions.assertFalse(masked.contains(":"), masked);
        }

        @Test
        @DisplayName("a doubled quote, which is a character rather than the end")
        void keepsGoingPastAnEscapedQuote() {
            final var masked = SqlText.maskLiteralsAndComments("where t = 'it''s :here' and x = :x");
            Assertions.assertFalse(masked.contains(":here"), masked);
            Assertions.assertTrue(masked.contains(":x"), masked);
        }

        @Test
        @DisplayName("a quoted identifier")
        void blanksQuotedIdentifiers() {
            final var masked = SqlText.maskLiteralsAndComments("select \"od:d\" from t where a = :a");
            Assertions.assertFalse(masked.contains(":d"), masked);
            Assertions.assertTrue(masked.contains(":a"), masked);
        }

        @Test
        @DisplayName("a line comment")
        void blanksLineComments() {
            final var masked = SqlText.maskLiteralsAndComments("select 1 -- todo: fix\nwhere a = :a");
            Assertions.assertFalse(masked.contains(": fix"), masked);
            Assertions.assertTrue(masked.contains(":a"), masked);
        }

        @Test
        @DisplayName("a block comment, including the nested ones PostgreSQL allows")
        void blanksNestedBlockComments() {
            final var masked = SqlText.maskLiteralsAndComments(
                    "select /* outer :a /* inner :b */ still :c */ 1 where d = :d");
            Assertions.assertFalse(masked.contains(":a"), masked);
            Assertions.assertFalse(masked.contains(":b"), masked);
            Assertions.assertFalse(masked.contains(":c"), masked);
            Assertions.assertTrue(masked.contains(":d"), masked);
        }

        @Test
        @DisplayName("a dollar-quoted body, which exists to hold text like this")
        void blanksDollarQuoted() {
            final var masked = SqlText.maskLiteralsAndComments(
                    "create function f() returns int as $body$ select :nope $body$ language sql");
            Assertions.assertFalse(masked.contains(":nope"), masked);
        }

        @Test
        @DisplayName("a division that only looks like a comment opener")
        void leavesDivisionAlone() {
            final var sql = "select a / b from t where c = :c";
            Assertions.assertEquals(sql, SqlText.maskLiteralsAndComments(sql));
        }

        @Test
        @DisplayName("a lone dollar, which opens nothing")
        void leavesLoneDollarAlone() {
            final var sql = "select a from t where cost = :cost";
            Assertions.assertEquals(sql, SqlText.maskLiteralsAndComments(sql));
        }

    }

}
