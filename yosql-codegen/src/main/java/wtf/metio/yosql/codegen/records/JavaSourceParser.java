/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import wtf.metio.javapoet.TypeGuesser;
import wtf.metio.yosql.codegen.exceptions.UnparsableRecordException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads the shape of a type out of its Java source.
 *
 * <p>A record named as the result row type of a statement is user code that has not been compiled
 * yet — code generation runs before compilation, and the record usually lives in the very module
 * being generated for. So its components are read from the source text rather than by loading the
 * class, which also means nothing about the mapping needs to survive to runtime.</p>
 *
 * <p>What is understood is the canonical constructor of a record declaration: its component names,
 * and their types resolved against the file's package and imports. Anything else about the file —
 * bodies, compact constructors, nested types, members — is not read, because a converter needs
 * none of it.</p>
 */
public final class JavaSourceParser {

    private static final Pattern PACKAGE = Pattern.compile("(?m)^\\s*package\\s+([\\w.]+)\\s*;");
    private static final Pattern IMPORT = Pattern.compile("(?m)^\\s*import\\s+(?!static\\s)([\\w.]+(?:\\.\\*)?)\\s*;");
    private static final Pattern DECLARATION = Pattern.compile(
            "\\b(record|enum|class|interface)\\s+([A-Za-z_$][\\w$]*)");
    private static final Pattern TYPE_REFERENCE = Pattern.compile(
            "[A-Za-z_$][\\w$]*(?:\\s*\\.\\s*[A-Za-z_$][\\w$]*)*");
    private static final Pattern ANNOTATION = Pattern.compile("^@\\s*[\\w.$]+");
    /**
     * A method taking one ResultSet and nothing else. The modifier run in front of it is captured
     * so that a static factory or a non-public helper of the same shape can be told apart.
     */
    private static final Pattern RESULT_SET_METHOD = Pattern.compile(
            "((?:\\b(?:public|protected|private|static|final|synchronized|native|strictfp|abstract|default)\\b\\s+)*)"
                    + "([\\w.$]+(?:\\s*<[^>()]*>)?(?:\\s*\\[\\s*\\])*)\\s+"
                    + "([A-Za-z_$][\\w$]*)\\s*\\(\\s*(?:final\\s+)?"
                    + "(?:java\\s*\\.\\s*sql\\s*\\.\\s*)?ResultSet\\s+[A-Za-z_$][\\w$]*\\s*\\)");

    private static final Set<String> PRIMITIVES = Set.of(
            "boolean", "byte", "char", "double", "float", "int", "long", "short", "void", "var");

    /**
     * The subset of {@code java.lang} a persistence type plausibly uses. An unlisted simple name
     * falls back to the file's own package, which is where a domain type sitting next to the record
     * lives.
     */
    private static final Set<String> JAVA_LANG = Set.of(
            "Boolean", "Byte", "Character", "CharSequence", "Comparable", "Double", "Enum", "Float",
            "Integer", "Iterable", "Long", "Number", "Object", "Record", "Short", "String", "Void");

    /**
     * @param source the file's text
     * @param location the file it came from, named in diagnostics
     * @param expected the type the caller went looking for
     * @return the declaration matching {@code expected}
     */
    public JavaSourceType parse(final String source, final Path location, final ClassName expected) {
        final var text = strip(source);
        final var packageName = find(PACKAGE, text).orElse("");
        final var imports = imports(text);

        final var declaration = DECLARATION.matcher(text);
        while (declaration.find()) {
            if (!expected.simpleName().equals(declaration.group(2))) {
                continue;
            }
            final var members = directMembers(text, declaration.end());
            return switch (declaration.group(1)) {
                case "record" -> JavaSourceType.record(expected,
                        components(text, declaration.end(), location, expected, packageName, imports),
                        valueOfParameters(members, expected, packageName, imports),
                        resultSetMethods(members, packageName, imports));
                case "enum" -> JavaSourceType.enumeration(expected);
                default -> JavaSourceType.other(expected,
                        valueOfParameters(members, expected, packageName, imports),
                        resultSetMethods(members, packageName, imports));
            };
        }
        throw new UnparsableRecordException(location, expected,
                "no type named '%s' is declared in it".formatted(expected.simpleName()));
    }


    /**
     * The public instance methods that take a single {@link java.sql.ResultSet}.
     *
     * <p>What makes a hand-written converter a converter is its shape, so that is what identifies
     * it. A class holding exactly one such method needs to say nothing else: the method's name is
     * what the repository calls and its return type is what the statement produces, both already
     * written down in Java.</p>
     */
    public List<JavaSourceMethod> resultSetMethods(
            final String strippedSource, final String packageName, final Map<String, String> imports) {
        final var matcher = RESULT_SET_METHOD.matcher(strippedSource);
        final var methods = new ArrayList<JavaSourceMethod>();
        while (matcher.find()) {
            final var modifiers = matcher.group(1);
            if (!modifiers.contains("public") || modifiers.contains("static")) {
                continue;
            }
            final var returned = matcher.group(2).strip();
            if (returned.equals("new") || returned.equals("return")) {
                continue;
            }
            methods.add(new JavaSourceMethod(matcher.group(3),
                    TypeGuesser.guessTypeName(qualify(returned, packageName, imports))));
        }
        return methods;
    }

    /**
     * The parameter types of every {@code static <Type> valueOf(single argument)} the file declares.
     *
     * <p>Read as text like everything else here: a factory is recognised by returning the type it is
     * declared in and taking one argument. Several overloads are kept rather than picked between,
     * because choosing silently is worse than saying they are ambiguous.</p>
     */
    private List<TypeName> valueOfParameters(
            final String text,
            final ClassName owner,
            final String packageName,
            final Map<String, String> imports) {
        final var factory = Pattern.compile(
                "\\bstatic\\b[^;{}()]{0,120}?\\b(?:" + Pattern.quote(owner.simpleName())
                        + "|" + Pattern.quote(owner.toString()) + ")\\s+valueOf\\s*\\(([^)]*)\\)");
        final var matcher = factory.matcher(text);
        final var parameters = new ArrayList<TypeName>();
        while (matcher.find()) {
            final var arguments = matcher.group(1).strip();
            if (arguments.isEmpty()) {
                continue;
            }
            final var split = splitTopLevel(arguments);
            if (split.size() != 1) {
                continue;
            }
            var declaration = stripAnnotations(split.get(0)).strip();
            // `final` is a modifier on a parameter, not part of its type.
            if (declaration.startsWith("final ")) {
                declaration = declaration.substring("final ".length()).stripLeading();
            }
            final var space = lastTopLevelSpace(declaration);
            if (space < 0) {
                continue;
            }
            parameters.add(TypeGuesser.guessTypeName(
                    qualify(declaration.substring(0, space).strip(), packageName, imports)));
        }
        return parameters;
    }

    private List<JavaSourceComponent> components(
            final String text,
            final int afterName,
            final Path location,
            final ClassName owner,
            final String packageName,
            final Map<String, String> imports) {
        final var open = openingParenthesis(text, afterName, location, owner);
        final var close = matching(text, open, '(', ')', location, owner);
        final var inside = text.substring(open + 1, close).strip();
        if (inside.isBlank()) {
            throw new UnparsableRecordException(location, owner, "it declares no components");
        }
        final var components = new ArrayList<JavaSourceComponent>();
        for (final var part : splitTopLevel(inside)) {
            components.add(component(part, location, owner, packageName, imports));
        }
        return components;
    }

    private JavaSourceComponent component(
            final String declaration,
            final Path location,
            final ClassName owner,
            final String packageName,
            final Map<String, String> imports) {
        var rest = stripAnnotations(declaration).strip();
        // A trailing `...` belongs to the type, not to the name.
        final var varargs = rest.contains("...");
        rest = rest.replace("...", " ").strip();

        final var split = lastTopLevelSpace(rest);
        if (split < 0) {
            throw new UnparsableRecordException(location, owner,
                    "component '%s' is not a type followed by a name".formatted(declaration.strip()));
        }
        var type = rest.substring(0, split).strip();
        var name = rest.substring(split + 1).strip();
        // `String names[]` declares an array just as `String[] names` does.
        while (name.endsWith("[]")) {
            name = name.substring(0, name.length() - 2).strip();
            type = type + "[]";
        }
        if (varargs) {
            type = type + "[]";
        }
        if (name.isEmpty()) {
            throw new UnparsableRecordException(location, owner,
                    "component '%s' has no name".formatted(declaration.strip()));
        }
        return new JavaSourceComponent(name, TypeGuesser.guessTypeName(qualify(type, packageName, imports)));
    }

    /**
     * Rewrites every simple type name in {@code type} to a fully-qualified one, so the result reads
     * the same whether the source wrote {@code UUID} or {@code java.util.UUID}.
     */
    private String qualify(final String type, final String packageName, final Map<String, String> imports) {
        final var matcher = TYPE_REFERENCE.matcher(type);
        final var result = new StringBuilder();
        while (matcher.find()) {
            final var reference = matcher.group().replaceAll("\\s+", "");
            matcher.appendReplacement(result, Matcher.quoteReplacement(
                    reference.contains(".") || PRIMITIVES.contains(reference)
                            ? reference
                            : resolve(reference, packageName, imports)));
        }
        matcher.appendTail(result);
        return result.toString().replaceAll("\\s+", "");
    }

    private String resolve(final String simpleName, final String packageName, final Map<String, String> imports) {
        final var imported = imports.get(simpleName);
        if (imported != null) {
            return imported;
        }
        if (JAVA_LANG.contains(simpleName)) {
            return "java.lang." + simpleName;
        }
        final var onDemand = imports.get("*");
        if (onDemand != null) {
            return onDemand + "." + simpleName;
        }
        return packageName.isEmpty() ? simpleName : packageName + "." + simpleName;
    }

    private Map<String, String> imports(final String text) {
        final var imports = new HashMap<String, String>();
        final var onDemand = new ArrayList<String>();
        final var matcher = IMPORT.matcher(text);
        while (matcher.find()) {
            final var value = matcher.group(1);
            if (value.endsWith(".*")) {
                onDemand.add(value.substring(0, value.length() - 2));
            } else {
                imports.put(value.substring(value.lastIndexOf('.') + 1), value);
            }
        }
        // A single on-demand import can stand in for the package a name came from. Several cannot
        // be told apart without the classpath, so none of them is used.
        if (onDemand.size() == 1) {
            imports.put("*", onDemand.get(0));
        }
        return imports;
    }

    private static Optional<String> find(final Pattern pattern, final String text) {
        final var matcher = pattern.matcher(text);
        return matcher.find() ? Optional.of(matcher.group(1)) : Optional.empty();
    }

    private static int openingParenthesis(
            final String text, final int from, final Path location, final ClassName owner) {
        var index = from;
        var depth = 0;
        while (index < text.length()) {
            final var character = text.charAt(index);
            if (character == '<') {
                depth++;
            } else if (character == '>') {
                depth--;
            } else if (character == '(' && depth == 0) {
                return index;
            } else if (!Character.isWhitespace(character) && depth == 0) {
                break;
            }
            index++;
        }
        throw new UnparsableRecordException(location, owner, "its component list could not be found");
    }

    private static int matching(
            final String text, final int open, final char opening, final char closing,
            final Path location, final ClassName owner) {
        var depth = 0;
        for (var index = open; index < text.length(); index++) {
            final var character = text.charAt(index);
            if (character == opening) {
                depth++;
            } else if (character == closing && --depth == 0) {
                return index;
            }
        }
        throw new UnparsableRecordException(location, owner, "its component list is not closed");
    }

    private static List<String> splitTopLevel(final String text) {
        final var parts = new ArrayList<String>();
        final var current = new StringBuilder();
        var depth = 0;
        for (final var character : text.toCharArray()) {
            if (character == '<' || character == '(' || character == '[') {
                depth++;
            } else if (character == '>' || character == ')' || character == ']') {
                depth--;
            }
            if (character == ',' && depth == 0) {
                parts.add(current.toString());
                current.setLength(0);
            } else {
                current.append(character);
            }
        }
        if (!current.isEmpty()) {
            parts.add(current.toString());
        }
        return parts;
    }

    private static int lastTopLevelSpace(final String text) {
        var depth = 0;
        var last = -1;
        for (var index = 0; index < text.length(); index++) {
            final var character = text.charAt(index);
            if (character == '<' || character == '[') {
                depth++;
            } else if (character == '>' || character == ']') {
                depth--;
            } else if (Character.isWhitespace(character) && depth == 0) {
                last = index;
            }
        }
        return last;
    }

    private static String stripAnnotations(final String declaration) {
        var rest = declaration.strip();
        while (true) {
            final var matcher = ANNOTATION.matcher(rest);
            if (!matcher.find()) {
                return rest;
            }
            rest = rest.substring(matcher.end()).stripLeading();
            if (rest.startsWith("(")) {
                var depth = 0;
                var index = 0;
                for (; index < rest.length(); index++) {
                    final var character = rest.charAt(index);
                    if (character == '(') {
                        depth++;
                    } else if (character == ')' && --depth == 0) {
                        index++;
                        break;
                    }
                }
                rest = rest.substring(index).stripLeading();
            }
        }
    }

    /**
     * Blanks out comments and string, text-block and character literals, so a brace, a comma or the
     * word {@code record} inside one cannot be mistaken for syntax. Lengths are preserved so
     * offsets still line up with the original.
     */
    /**
     * What the type declares itself, with everything inside a further pair of braces blanked out.
     *
     * <p>A converter's method and a value type's factory are both recognised by their shape, and the
     * same shape occurring in a nested class, an anonymous class or a method body belongs to
     * something else. Keeping only what sits directly in the type's own body is what makes "the one
     * public method taking a ResultSet" mean the type's own, and stops a private helper class from
     * being reported as a second candidate.</p>
     *
     * <p>Positions are not preserved, so this is for shape matching only — a diagnostic that has to
     * name a place works from the full text.</p>
     */
    private static String directMembers(final String text, final int declarationEnd) {
        final var open = text.indexOf('{', declarationEnd);
        if (open < 0) {
            return "";
        }
        final var members = new StringBuilder();
        var depth = 0;
        for (var index = open + 1; index < text.length(); index++) {
            final var character = text.charAt(index);
            if (character == '{') {
                depth++;
                members.append(' ');
            } else if (character == '}') {
                if (depth == 0) {
                    break;
                }
                depth--;
                members.append(' ');
            } else {
                members.append(depth == 0 || character == '\n' ? character : ' ');
            }
        }
        return members.toString();
    }

    private static String strip(final String source) {
        final var result = new StringBuilder(source);
        var index = 0;
        while (index < source.length()) {
            final var character = source.charAt(index);
            final var next = index + 1 < source.length() ? source.charAt(index + 1) : '\0';
            if (character == '/' && next == '/') {
                index = blankUntil(result, source, index, "\n", false);
            } else if (character == '/' && next == '*') {
                index = blankUntil(result, source, index + 2, "*/", true);
            } else if (character == '"' && next == '"' && index + 2 < source.length() && source.charAt(index + 2) == '"') {
                index = blankUntil(result, source, index + 3, "\"\"\"", true);
            } else if (character == '"') {
                index = blankLiteral(result, source, index, '"');
            } else if (character == '\'') {
                index = blankLiteral(result, source, index, '\'');
            } else {
                index++;
            }
        }
        return result.toString();
    }

    private static int blankUntil(
            final StringBuilder result, final String source, final int from,
            final String terminator, final boolean includeTerminator) {
        final var end = source.indexOf(terminator, from);
        final var stop = end < 0 ? source.length() : (includeTerminator ? end + terminator.length() : end);
        blank(result, from, stop);
        return stop;
    }

    private static int blankLiteral(
            final StringBuilder result, final String source, final int start, final char quote) {
        var index = start + 1;
        while (index < source.length()) {
            final var character = source.charAt(index);
            if (character == '\\') {
                index += 2;
                continue;
            }
            if (character == quote || character == '\n') {
                index++;
                break;
            }
            index++;
        }
        blank(result, start + 1, Math.min(index, source.length()));
        return index;
    }

    private static void blank(final StringBuilder result, final int from, final int to) {
        for (var index = from; index < to && index < result.length(); index++) {
            if (result.charAt(index) != '\n') {
                result.setCharAt(index, ' ');
            }
        }
    }

}
