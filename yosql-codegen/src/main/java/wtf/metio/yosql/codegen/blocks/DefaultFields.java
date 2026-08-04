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
        return CodeBlock.builder()
                .add("\"\"\"")
                .add("$>$>\n$L", forTextBlock(statement))
                .add("\"\"\"$<$<")
                .build();
    }

    /**
     * The statement as a text block reads it back.
     *
     * <p>SQL is dropped into a text block so that the constant stays readable, but a text block is
     * still Java source: a backslash starts an escape sequence there. A regular expression such as
     * {@code '\d+'} is not a legal one and stops the user's compile; {@code '\s+'} is legal and
     * means a single space, so the constant silently becomes a different query than the one written.
     * Trailing whitespace is stripped by the text block itself, which matters when it falls inside a
     * string literal the statement spans two lines.</p>
     */
    private static String forTextBlock(final String statement) {
        final var escaped = statement.replace("\\", "\\\\").replace("\"\"\"", "\"\"\\\"");
        return Arrays.stream(escaped.split("\n", -1))
                .map(DefaultFields::keepTrailingWhitespace)
                .collect(Collectors.joining("\n"));
    }

    private static String keepTrailingWhitespace(final String line) {
        final var kept = line.stripTrailing();
        if (kept.length() == line.length()) {
            return line;
        }
        final var escaped = new StringBuilder(kept);
        line.substring(kept.length()).chars().forEach(character -> escaped.append(switch (character) {
            case ' ' -> "\\s";
            case '\t' -> "\\t";
            case '\r' -> "\\r";
            case '\f' -> "\\f";
            default -> String.valueOf((char) character);
        }));
        return escaped.toString();
    }

    private FieldSpec.Builder builder(final TypeName type, final String name) {
        return FieldSpec.builder(type, name)
                .addAnnotations(annotations.generatedField());
    }

}
