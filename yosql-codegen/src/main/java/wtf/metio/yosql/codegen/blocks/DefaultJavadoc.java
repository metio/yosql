/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import ch.qos.cal10n.IMessageConveyor;
import com.palantir.javapoet.ArrayTypeName;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;
import wtf.metio.yosql.internals.javapoet.TypeGuesser;
import wtf.metio.yosql.internals.jdk.FileNames;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.function.Predicate.not;

public final class DefaultJavadoc implements Javadoc {

    private static final ClassName CONNECTION = ClassName.get("java.sql", "Connection");
    private static final ClassName DATA_SOURCE = ClassName.get("javax.sql", "DataSource");

    /**
     * An {@code @} that starts a line, which javadoc reads as a block tag wherever it sits —
     * being inside {@code <pre>} does not protect it.
     */
    private static final Pattern LEADING_TAG = Pattern.compile("(?m)^(\\s*)@");

    /**
     * What starts a unicode escape. Java resolves these everywhere in a source file, comments
     * included, so this is the one sequence a statement cannot carry into javadoc as it was written —
     * not even inside {@code {@code}}, where an entity would be shown rather than resolved.
     */
    private static final String UNICODE_ESCAPE = "\\u";

    private final FilesConfiguration files;
    private final IMessageConveyor messages;

    public DefaultJavadoc(final FilesConfiguration files, final IMessageConveyor messages) {
        this.files = files;
        this.messages = messages;
    }

    @Override
    public CodeBlock repositoryJavadoc(final List<SqlStatement> statements) {
        final var builder = CodeBlock.builder()
                .add(messages.getMessage(Javadocs.USED_FILES))
                .add(messages.getMessage(Javadocs.LIST_START));
        final var input = files.inputBaseDirectory();
        statements.stream()
                .map(SqlStatement::getSourcePath)
                .distinct()
                .map(input::relativize)
                .forEach(path -> builder.add(messages.getMessage(Javadocs.LIST_ITEM), escape(FileNames.toSlashes(path))));
        builder.add(messages.getMessage(Javadocs.LIST_END));
        return builder.build();
    }

    @Override
    public CodeBlock fieldJavaDoc(final SqlStatement statement) {
        final var input = files.inputBaseDirectory();
        final var builder = CodeBlock.builder()
                .add(messages.getMessage(Javadocs.USED_FILE))
                .add(messages.getMessage(Javadocs.LIST_START))
                .add(messages.getMessage(Javadocs.LIST_ITEM), escape(FileNames.toSlashes(input.relativize(statement.getSourcePath()))));
        builder.add(messages.getMessage(Javadocs.LIST_END));
        return builder.build();
    }

    @Override
    public CodeBlock methodJavadoc(final List<SqlStatement> statements, final String configuration) {
        final var builder = CodeBlock.builder();
        statements.stream()
                .map(SqlStatement::getConfiguration)
                .flatMap(config -> config.description().stream())
                .filter(not(Strings::isBlank))
                .forEach(description -> builder.add(messages.getMessage(Javadocs.DESCRIPTION), escape(description)));
        if (statements.size() > 1) {
            builder.add(messages.getMessage(Javadocs.EXECUTED_STATEMENTS));
        } else {
            builder.add(messages.getMessage(Javadocs.EXECUTED_STATEMENT));
        }
        for (final var statement : statements) {
            statement.getConfiguration().vendor()
                    .filter(not(Strings::isBlank))
                    .ifPresentOrElse(vendor ->
                                    builder.add(messages.getMessage(Javadocs.VENDOR), escape(vendor)),
                            () -> {
                                if (statements.size() > 1) {
                                    builder.add(messages.getMessage(Javadocs.FALLBACK));
                                }
                            });
            addStatement(builder, statement.getRawStatement());
        }
        builder.add(messages.getMessage(Javadocs.USED_FILES_METHOD))
                .add(messages.getMessage(Javadocs.LIST_START));
        final var input = files.inputBaseDirectory();
        statements.stream()
                .map(SqlStatement::getSourcePath)
                .distinct()
                .map(input::relativize)
                .forEach(path -> builder.add(messages.getMessage(Javadocs.LIST_ITEM), escape(FileNames.toSlashes(path))));
        builder.add(messages.getMessage(Javadocs.LIST_END));
        builder.add(messages.getMessage(Javadocs.DISABLE_WITH), configuration);
        statements.stream()
                .map(SqlStatement::getConfiguration)
                .map(SqlConfiguration::resultRowConverter)
                .flatMap(Optional::stream)
                .map(ResultRowConverter::resultType)
                .flatMap(Optional::stream)
                .filter(type -> !type.startsWith("java"))
                .map(type -> type.substring(0, type.contains("<") ? type.indexOf("<") : type.length()))
                .distinct()
                .map(TypeGuesser::guessTypeName)
                .filter(type -> !type.isPrimitive())
                .filter(type -> !type.isBoxedPrimitive())
                .forEach(type -> builder.add(messages.getMessage(Javadocs.SEE), type));
        return builder.build();
    }

    @Override
    public CodeBlock constructorJavadoc() {
        return CodeBlock.builder().add(messages.getMessage(Javadocs.CONSTRUCTOR)).build();
    }

    @Override
    public MethodSpec withSignatureTags(final MethodSpec method) {
        final var builder = method.toBuilder();
        for (final var parameter : method.parameters()) {
            builder.addJavadoc(messages.getMessage(parameterTag(parameter.type())), parameter.name());
        }
        if (method.returnType() != null && !TypeName.VOID.equals(method.returnType())) {
            builder.addJavadoc(messages.getMessage(countsRows(method.returnType())
                    ? Javadocs.RETURN_AFFECTED_ROWS
                    : Javadocs.RETURN_RESULT));
        }
        return builder.build();
    }

    private static Javadocs parameterTag(final TypeName type) {
        if (CONNECTION.equals(type)) {
            return Javadocs.CONNECTION_PARAMETER;
        }
        return DATA_SOURCE.equals(type) ? Javadocs.DATA_SOURCE_PARAMETER : Javadocs.PARAMETER;
    }

    /**
     * A write answers with a count and everything else answers with what it selected, so the shape
     * of the result says which: a bare number is the only thing a repository returns that is not a
     * row.
     */
    private static boolean countsRows(final TypeName type) {
        if (type instanceof final ArrayTypeName array) {
            return array.componentType().isPrimitive();
        }
        return type.isPrimitive();
    }

    /**
     * Shows a statement the way javadoc shows code: {@code <pre>} keeps the line breaks and
     * indentation, and {@code {@code}} makes the content literal, so a {@code <} in the SQL is a
     * less-than sign rather than the start of a tag. The generated file then reads as the SQL that
     * was written, which entity escaping would not.
     *
     * <p>Three things that block cannot carry, so a statement containing any of them is escaped into
     * a plain {@code <pre>} instead: a {@code *}{@code /} ends the comment whatever encloses it, an
     * unbalanced brace closes the inline tag early, and an {@code @} starting a line is read as a
     * block tag wherever it sits — a MySQL statement assigning {@code @rownum} is enough. None can be
     * escaped inside {@code {@code}}, because entity references there are shown rather than
     * resolved.</p>
     */
    private void addStatement(final CodeBlock.Builder builder, final String statement) {
        if (isLiteral(statement)) {
            builder.add(messages.getMessage(Javadocs.STATEMENT), statement);
        } else {
            builder.add(messages.getMessage(Javadocs.ESCAPED_STATEMENT), escape(statement));
        }
    }

    private static boolean isLiteral(final String statement) {
        if (statement.contains("*/") || statement.contains(UNICODE_ESCAPE)
                || LEADING_TAG.matcher(statement).find()) {
            return false;
        }
        var depth = 0;
        for (final var character : statement.toCharArray()) {
            if (character == '{') {
                depth++;
            } else if (character == '}' && --depth < 0) {
                return false;
            }
        }
        return depth == 0;
    }

    /**
     * Renders text taken from a user's SQL file so that it survives being read as javadoc.
     *
     * <p>The statement is shown as written, and what a user writes is under no obligation to be
     * HTML: a {@code <} opens a tag, an {@code &} opens an entity, a {@code *}{@code /} ends the
     * comment and takes the rest of the file with it, and an {@code @} in the first column starts a
     * block tag. Escaping them is what keeps the generated file compiling and its javadoc run
     * quiet, both of which happen in a build that is not ours.</p>
     */
    private static String escape(final String text) {
        final var escaped = text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("*/", "*&#47;")
                .replace("{@", "&#123;@")
                // A unicode escape is resolved before the file is even lexed, comments included, so a
                // Windows path in a statement — a backslash followed by a u — is an "illegal unicode
                // escape" in a file the author never wrote. Writing that backslash as an entity leaves
                // nothing for that pass to find, and a <pre> block renders it back as a backslash.
                // (This comment cannot show the sequence for the very same reason.)
                .replace(UNICODE_ESCAPE, "&#92;u");
        return LEADING_TAG.matcher(escaped).replaceAll("$1&#64;");
    }

}
