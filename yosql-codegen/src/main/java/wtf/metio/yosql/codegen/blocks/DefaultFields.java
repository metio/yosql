/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class DefaultFields implements Fields {

    private static final String NAME_REGEX = "([a-z])([A-Z])";
    private static final String NAME_REPLACEMENT = "$1_$2";

    private final Annotations annotations;

    public DefaultFields(
            final Annotations annotations) {
        this.annotations = annotations;
    }

    @Override
    public FieldSpec field(final Type type, final String name) {
        return field(TypeName.get(type), name);
    }

    @Override
    public FieldSpec field(final TypeName type, final String name) {
        final var builder = builder(type, name).addModifiers(Modifier.PRIVATE);
        builder.addModifiers(Modifier.FINAL);
        return builder.build();
    }

    @Override
    public FieldSpec.Builder prepareConstant(final Type type, final String name) {
        return prepareConstant(TypeName.get(type), name);
    }

    @Override
    public FieldSpec.Builder prepareConstant(final TypeName type, final String name) {
        return builder(type, name).addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL);
    }

    @Override
    public CodeBlock initialize(final String statement) {
        if (!fitsInATextBlock(statement)) {
            // A plain literal, which JavaPoet escapes in full. Less pleasant to read, and the only
            // form that can hold this one.
            return CodeBlock.of("$S", statement);
        }
        return CodeBlock.builder()
                .add("\"\"\"")
                .add("$>$>\n$L", forTextBlock(statement))
                .add("\"\"\"$<$<")
                .build();
    }

    /**
     * Whether a text block can give this statement back unchanged.
     *
     * <p>Text blocks strip trailing white space by {@link Character#isWhitespace(char)}, and the only
     * whitespace they offer an escape for is the space, the tab, the carriage return and the form
     * feed. A statement carrying any other — an ideographic space, say — cannot be protected: a
     * {@code \\uXXXX} escape would not help, because those are resolved before the text block is
     * read and would arrive as the whitespace itself.</p>
     *
     * <p>An empty statement has no line to hold, and comes back as a line ending instead.</p>
     */
    private static boolean fitsInATextBlock(final String statement) {
        return !statement.isEmpty() && statement.chars().allMatch(DefaultFields::writableInATextBlock);
    }

    /**
     * The whitespace a text block offers an escape for, plus anything that is not whitespace and not
     * a control character. Everything else — a vertical tab, an ideographic space — either gets
     * stripped or changes meaning, and cannot be written back in.
     *
     * <p>The carriage return and the form feed are among them: a text block turns both into a
     * newline, and an escape cannot stop that because it is the block's own content being
     * normalised. A statement carrying either — a file with CRLF endings, most likely — is written
     * as a plain literal instead.</p>
     */
    private static boolean writableInATextBlock(final int character) {
        return character == ' ' || character == '\t' || character == '\n'
                || !Character.isWhitespace(character) && !Character.isISOControl(character);
    }

    /**
     * The statement as a text block reads it back.
     *
     * <p>SQL is dropped into a text block so that the constant stays readable, but a text block is
     * still Java source: a backslash starts an escape sequence there. A regular expression such as
     * {@code '\d+'} is not a legal one and stops the user's compile; {@code '\s+'} is legal and
     * means a single space, so the constant silently becomes a different query than the one written.</p>
     *
     * <p>Whitespace at either end of a line is escaped as well, because a text block decides what to
     * strip by looking at how far its lines are indented. Trailing whitespace it removes outright.
     * Leading whitespace it counts as indentation, and how much of that survives depends on where
     * the closing delimiter lands — which for a single-line statement is the content line itself, so
     * the statement's own leading spaces were taken for indentation and removed. Escaped, none of it
     * is whitespace as far as that rule is concerned, and the text comes back as it went in.</p>
     *
     * <p>Every quote is escaped rather than only the runs of three that would close the block early.
     * Two rules that both rewrite quotes have to agree about what the other already rewrote, and
     * they did not: a statement ending in a quote sits against the closing delimiter, so escaping
     * that one had to happen after the runs were broken up, by which point the trailing run included
     * a quote the first rule had already escaped. One rule cannot disagree with itself.</p>
     */
    private static String forTextBlock(final String statement) {
        final var escaped = statement.replace("\\", "\\\\").replace("\"", "\\\"");
        return Arrays.stream(escaped.split("\n", -1))
                .map(DefaultFields::keepEdgeWhitespace)
                .collect(Collectors.joining("\n"));
    }

    /**
     * @return the line with the whitespace at either end written as escapes
     */
    private static String keepEdgeWhitespace(final String line) {
        var leading = 0;
        while (leading < line.length() && isStrippable(line.charAt(leading))) {
            leading++;
        }
        if (leading == line.length()) {
            return escapeWhitespace(line);
        }
        var trailing = line.length();
        while (trailing > leading && isStrippable(line.charAt(trailing - 1))) {
            trailing--;
        }
        return escapeWhitespace(line.substring(0, leading))
                + line.substring(leading, trailing)
                + escapeWhitespace(line.substring(trailing));
    }

    private static boolean isStrippable(final char character) {
        return character != '\n' && Character.isWhitespace(character);
    }

    private static String escapeWhitespace(final String whitespace) {
        final var escaped = new StringBuilder(whitespace.length() * 2);
        whitespace.chars().forEach(character -> escaped.append(switch (character) {
            case ' ' -> "\\s";
            case '\t' -> "\\t";
            default -> String.valueOf((char) character);
        }));
        return escaped.toString();
    }

    private FieldSpec.Builder builder(final TypeName type, final String name) {
        return FieldSpec.builder(type, name)
                .addAnnotations(annotations.generatedField());
    }

}
