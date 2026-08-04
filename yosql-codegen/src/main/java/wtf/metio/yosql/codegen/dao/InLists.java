/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeName;
import wtf.metio.yosql.codegen.files.SqlStatementParser;
import wtf.metio.yosql.codegen.files.SqlText;
import wtf.metio.yosql.internals.javapoet.TypeGuesser;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Objects;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * What a statement's {@code in (:names)} needs to become, worked out while the SQL is still here.
 *
 * <p>A prepared statement has one placeholder per value, and how many values a collection holds is
 * only known when the method is called. So the query cannot be one constant: it is the constant text
 * either side of the collection, with as many placeholders between them as the caller passed.</p>
 *
 * <p>Splitting the SQL is done here rather than in the generated code, because here the placeholders
 * are the ones {@code YoSQL} substituted for {@code :names}. A {@code ?} the author wrote inside a
 * string literal is text, and code that counted question marks at run time would take it for a
 * placeholder and cut the query in the wrong place.</p>
 */
public final class InLists {

    /**
     * Names the helper every repository with a collection parameter carries. Not configurable, and
     * private in the generated class, so it cannot collide with anything a caller wrote.
     */
    public static final String EXPANSION_METHOD = "expandPlaceholders";

    /**
     * The collections a driver will not take as one value. An array is not among them: an array
     * parameter is how a batch statement passes one value per execution, and reading it as a list of
     * placeholders would change what those statements mean.
     */
    private static final Set<String> COLLECTIONS = Set.of(
            "java.util.Collection", "java.util.List", "java.util.Set", "java.lang.Iterable",
            "Collection", "List", "Set", "Iterable");

    private InLists() {
        // utility class
    }

    /**
     * @return whether the values of this parameter each need a placeholder of their own
     */
    public static boolean expands(final SqlParameter parameter) {
        return parameter.type()
                .map(String::strip)
                .map(type -> {
                    final var generic = type.indexOf('<');
                    return generic < 0 ? type : type.substring(0, generic).strip();
                })
                .filter(COLLECTIONS::contains)
                .isPresent();
    }

    /**
     * What the values of an expanding parameter are, rather than what holds them.
     *
     * <p>Everything else compares a parameter against the column it names, and a collection is
     * compared the same way — one value at a time, because that is how they are bound.</p>
     */
    public static Optional<TypeName> elementType(final SqlParameter parameter) {
        if (!expands(parameter)) {
            return parameter.typeName();
        }
        return parameter.type()
                .map(String::strip)
                .filter(type -> type.endsWith(">"))
                .map(type -> type.substring(type.indexOf('<') + 1, type.length() - 1).strip())
                .filter(element -> !element.isBlank() && !element.contains(","))
                .map(TypeGuesser::guessTypeName);
    }

    public static boolean anyExpands(final SqlConfiguration configuration) {
        return configuration.parameters().stream().anyMatch(InLists::expands);
    }

    private static Set<String> expandingNames(final SqlConfiguration configuration) {
        final var names = new LinkedHashSet<String>();
        for (final var parameter : configuration.parameters()) {
            if (expands(parameter)) {
                parameter.name().ifPresent(names::add);
            }
        }
        return names;
    }

    /**
     * The parameter each placeholder binds, in the order the statement writes them.
     *
     * <p>Which is not the order the parameters were declared in, and not one entry per parameter
     * either — a name used twice is bound twice. Binding walks this, counting as it goes, because
     * once a collection has been expanded the index of everything after it depends on how many
     * values it held.</p>
     */
    public static List<SqlParameter> placeholderOrder(final SqlConfiguration configuration) {
        final var order = new ArrayList<SqlParameter>();
        for (final var parameter : configuration.parameters()) {
            for (final var index : parameter.indices().orElseGet(() -> new int[0])) {
                while (order.size() < index) {
                    order.add(null);
                }
                order.set(index - 1, parameter);
            }
        }
        order.removeIf(Objects::isNull);
        return order;
    }

    /**
     * The constant text around the collections: one part more than there are collection placeholders,
     * so that joining them with a placeholder list between each pair rebuilds the query.
     */
    public static List<String> queryParts(final SqlConfiguration configuration, final String rawStatement) {
        final var expanding = expandingNames(configuration);
        final var parts = new ArrayList<String>();
        final var part = new StringBuilder();
        final var matcher = SqlStatementParser.NAMED_PARAMETER_PATTERN
                .matcher(SqlText.maskLiteralsAndComments(rawStatement));
        var last = 0;
        while (matcher.find()) {
            part.append(rawStatement, last, matcher.start());
            last = matcher.end();
            if (expanding.contains(matcher.group().substring(1))) {
                parts.add(part.toString());
                part.setLength(0);
            } else {
                part.append('?');
            }
        }
        part.append(rawStatement.substring(last));
        parts.add(part.toString());
        return parts;
    }

    /**
     * Whether an empty collection would be read as {@code not in (null)}, which excludes every row
     * rather than none of them.
     *
     * <p>{@code in} on an empty set matches nothing and {@code null} says exactly that, so the empty
     * case needs no special handling there. Negated, the same expansion is not merely unhelpful but
     * wrong, and a list of placeholders cannot say "every row" — so a statement that would hit it
     * says so when it is called rather than quietly returning nothing.</p>
     */
    /**
     * The helper that turns a count into that many placeholders.
     */
    public static MethodSpec expansion() {
        return MethodSpec.methodBuilder(EXPANSION_METHOD)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(String.class)
                .addParameter(ParameterSpec.builder(TypeName.INT, "count").addModifiers(Modifier.FINAL).build())
                .addJavadoc("Builds the placeholders for one collection parameter.\n\n"
                        + "@param count How many values are bound where the statement wrote one placeholder.\n"
                        + "@return That many placeholders, or {@code null} for none: {@code in ()} is not "
                        + "valid SQL, and {@code in (null)} matches no row, which is what an empty set means.\n")
                .beginControlFlow("if (count < 1)")
                .addStatement("return $S", "null")
                .endControlFlow()
                .addStatement("final var placeholders = new $T(3 * count)", StringBuilder.class)
                .addStatement("placeholders.append('?')")
                .beginControlFlow("for (var index = 1; index < count; index++)")
                .addStatement("placeholders.append($S)", ", ?")
                .endControlFlow()
                .addStatement("return placeholders.toString()")
                .build();
    }

    public static boolean isNegated(final String rawStatement, final String parameter) {
        return Pattern.compile("\\bnot\\s+in\\s*\\(\\s*:" + Pattern.quote(parameter) + "\\b",
                        Pattern.CASE_INSENSITIVE)
                .matcher(rawStatement)
                .find();
    }


}
