/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;
import wtf.metio.yosql.internals.javapoet.TypeGuesser;
import wtf.metio.yosql.codegen.exceptions.UnparsableRecordException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads the shape of a type out of its Java source.
 *
 * <p>A record named as the result row type of a statement is user code that has not been compiled
 * yet — code generation runs before compilation, and the record usually lives in the very module
 * being generated for. So it is read from the source text rather than by loading the class, which
 * also means nothing about the mapping needs to survive to runtime.</p>
 *
 * <p>Parsing is JavaParser's job. Ours is only to say what the names in the file mean, which no
 * parser can answer without a compiled classpath — see {@link Scope}. Everything read is scoped to
 * the type's own declaration, so a nested helper class, an anonymous class or a method body
 * contributes nothing.</p>
 */
public final class JavaSourceParser {

    private static final ClassName RESULT_SET = ClassName.get("java.sql", "ResultSet");
    private static final String VALUE_OF = "valueOf";

    /**
     * Splits a written-out type into the names inside it, so that both halves of a
     * {@code Map<String, UUID>} are resolved rather than the whole thing treated as one name.
     */
    private static final Pattern TYPE_REFERENCE = Pattern.compile(
            "[A-Za-z_$][\\w$]*(?:\\s*\\.\\s*[A-Za-z_$][\\w$]*)*");

    private static final Set<String> PRIMITIVES = Set.of(
            "boolean", "byte", "char", "double", "float", "int", "long", "short", "void", "var");

    /**
     * The public types of {@code java.lang}, which are in scope in every file without an import.
     *
     * <p>Listed rather than looked up because the generator runs ahead of compilation and inside a
     * native image, where asking the runtime what a name means is not available. The list is stable:
     * {@code java.lang} does not lose types.</p>
     */
    private static final Set<String> JAVA_LANG = Set.of(
            "Appendable", "AutoCloseable", "Boolean", "Byte", "Character", "CharSequence", "Class",
            "ClassLoader", "ClassValue", "Cloneable", "Comparable", "Double", "Enum", "Float",
            "InheritableThreadLocal", "Integer", "Iterable", "Long", "Math", "Module", "ModuleLayer",
            "Number", "Object", "Package", "Process", "ProcessBuilder", "ProcessHandle", "Readable",
            "Record", "Runnable", "Runtime", "RuntimePermission", "SecurityManager", "Short",
            "StackTraceElement", "StackWalker", "StrictMath", "String", "StringBuffer",
            "StringBuilder", "System", "Thread", "ThreadGroup", "ThreadLocal", "Throwable", "Void",
            "ArithmeticException", "ArrayIndexOutOfBoundsException", "ArrayStoreException",
            "ClassCastException", "ClassNotFoundException", "CloneNotSupportedException", "Exception",
            "IllegalAccessException", "IllegalArgumentException", "IllegalCallerException",
            "IllegalMonitorStateException", "IllegalStateException", "IllegalThreadStateException",
            "IndexOutOfBoundsException", "InstantiationException", "InterruptedException",
            "LayerInstantiationException", "NegativeArraySizeException", "NoSuchFieldException",
            "NoSuchMethodException", "NullPointerException", "NumberFormatException",
            "ReflectiveOperationException", "RuntimeException", "SecurityException",
            "StringIndexOutOfBoundsException", "UnsupportedOperationException",
            "AbstractMethodError", "AssertionError", "BootstrapMethodError", "ClassCircularityError",
            "ClassFormatError", "Error", "ExceptionInInitializerError", "IllegalAccessError",
            "IncompatibleClassChangeError", "InstantiationError", "InternalError", "LinkageError",
            "NoClassDefFoundError", "NoSuchFieldError", "NoSuchMethodError", "OutOfMemoryError",
            "StackOverflowError", "ThreadDeath", "UnknownError", "UnsatisfiedLinkError",
            "UnsupportedClassVersionError", "VerifyError", "VirtualMachineError",
            "Deprecated", "FunctionalInterface", "Override", "SafeVarargs", "SuppressWarnings");

    /**
     * Whether a type is one this run can read the source of.
     *
     * <p>A name written without a package can mean a type sitting next to the file or a type from
     * {@code java.lang}, and the language resolves the neighbour first. Finding its source is the
     * only way to tell, so a domain record called {@code Process} or {@code Record} keeps its own
     * meaning instead of quietly becoming the JDK's.</p>
     */
    @FunctionalInterface
    public interface KnownTypes {

        boolean hasSource(String qualifiedName);

    }

    private static final KnownTypes NOTHING_KNOWN = qualifiedName -> false;

    /**
     * Parsing without the language-level validators.
     *
     * <p>Validation asks whether a file is legal for a given release of Java, which is the
     * compiler's question and not ours — we read declarations out of source the user is about to
     * compile anyway, and a file using a feature newer than whatever level we picked is their
     * business. Syntax errors still surface, because those come from the parser rather than from a
     * validator.</p>
     *
     * <p>It also keeps this off a reflective code path: the validators walk the AST through a
     * metamodel that reads node fields by name, which a closed-world native image has no reason to
     * keep and therefore drops.</p>
     */
    private final JavaParser parser = new JavaParser(new ParserConfiguration()
            .setLanguageLevel(ParserConfiguration.LanguageLevel.RAW));

    /**
     * @param source   the file's text
     * @param location the file it came from, named in diagnostics
     * @param expected the type the caller went looking for
     * @return the declaration matching {@code expected}
     */
    public JavaSourceType parse(final String source, final Path location, final ClassName expected) {
        return parse(source, location, expected, NOTHING_KNOWN);
    }

    /**
     * @param source   the file's text
     * @param location the file it came from, named in diagnostics
     * @param expected the type the caller went looking for
     * @param known    what this run can find the source of, which decides what an unqualified name means
     * @return the declaration matching {@code expected}
     */
    public JavaSourceType parse(
            final String source, final Path location, final ClassName expected, final KnownTypes known) {
        final var unit = compilationUnit(source, location, expected);
        final var scope = new Scope(packageOf(unit), importsOf(unit), known);
        final var declaration = unit.findAll(TypeDeclaration.class).stream()
                .filter(type -> expected.simpleName().equals(type.getNameAsString()))
                .findFirst()
                .orElseThrow(() -> new UnparsableRecordException(location, expected,
                        "no type named '%s' is declared in it".formatted(expected.simpleName())));
        rejectTypeParameters(declaration, location, expected);
        return switch (declaration) {
            case RecordDeclaration record -> JavaSourceType.record(expected,
                    components(record, location, expected, scope),
                    valueOfParameters(declaration, expected, scope),
                    resultSetMethods(declaration, scope));
            case EnumDeclaration _ -> JavaSourceType.enumeration(expected);
            default -> JavaSourceType.other(expected,
                    valueOfParameters(declaration, expected, scope),
                    resultSetMethods(declaration, scope));
        };
    }

    private CompilationUnit compilationUnit(
            final String source, final Path location, final ClassName expected) {
        final var result = parser.parse(source);
        if (!result.isSuccessful()) {
            throw new UnparsableRecordException(location, expected, result.getProblems().isEmpty()
                    ? "it is not valid Java"
                    : result.getProblem(0).getMessage());
        }
        return result.getResult().orElseThrow(() ->
                new UnparsableRecordException(location, expected, "it is not valid Java"));
    }

    /**
     * The components of the record's canonical constructor, in the order they are written.
     */
    private static List<JavaSourceComponent> components(
            final RecordDeclaration record,
            final Path location,
            final ClassName owner,
            final Scope scope) {
        if (record.getParameters().isEmpty()) {
            throw new UnparsableRecordException(location, owner, "it declares no components");
        }
        return record.getParameters().stream()
                .map(parameter -> new JavaSourceComponent(parameter.getNameAsString(),
                        TypeGuesser.guessTypeName(scope.qualify(writtenType(parameter)))))
                .toList();
    }

    /**
     * The parameter types of every {@code static <Type> valueOf(single argument)} the type declares.
     *
     * <p>A factory is recognised by returning the type it is declared in and taking one argument.
     * Several overloads are kept rather than picked between, because choosing silently is worse than
     * saying they are ambiguous.</p>
     */
    private static List<TypeName> valueOfParameters(
            final TypeDeclaration<?> declaration, final ClassName owner, final Scope scope) {
        return declaration.getMethods().stream()
                .filter(MethodDeclaration::isStatic)
                .filter(method -> VALUE_OF.equals(method.getNameAsString()))
                .filter(method -> method.getParameters().size() == 1)
                .filter(method -> owner.toString().equals(scope.qualify(method.getType().asString())))
                .map(method -> TypeGuesser.guessTypeName(scope.qualify(writtenType(method.getParameter(0)))))
                .toList();
    }

    /**
     * The public instance methods that take a single {@link java.sql.ResultSet}.
     *
     * <p>What makes a hand-written converter a converter is its shape, so that is what identifies
     * it. A class holding exactly one such method needs to say nothing else: the method's name is
     * what the repository calls and its return type is what the statement produces, both already
     * written down in Java.</p>
     */
    private static List<JavaSourceMethod> resultSetMethods(
            final TypeDeclaration<?> declaration, final Scope scope) {
        return declaration.getMethods().stream()
                .filter(MethodDeclaration::isPublic)
                .filter(method -> !method.isStatic())
                .filter(method -> method.getParameters().size() == 1)
                .filter(method -> RESULT_SET.toString()
                        .equals(scope.qualify(writtenType(method.getParameter(0)))))
                .map(method -> new JavaSourceMethod(method.getNameAsString(),
                        TypeGuesser.guessTypeName(scope.qualify(method.getType().asString()))))
                .toList();
    }

    /**
     * A type declaring type parameters has no fixed shape, and a statement says nothing about what
     * to substitute — so there is no converter to write and no result type to return.
     */
    private static void rejectTypeParameters(
            final TypeDeclaration<?> declaration, final Path location, final ClassName owner) {
        if (declaration instanceof final NodeWithTypeParameters<?> generic
                && !generic.getTypeParameters().isEmpty()) {
            throw new UnparsableRecordException(location, owner,
                    "it declares type parameters, and a generic type cannot be mapped to a result row");
        }
    }

    /**
     * A parameter's type as written, with varargs read as the array they are.
     */
    private static String writtenType(final Parameter parameter) {
        return parameter.isVarArgs()
                ? parameter.getType().asString() + "[]"
                : parameter.getType().asString();
    }

    private static String packageOf(final CompilationUnit unit) {
        return unit.getPackageDeclaration()
                .map(declaration -> declaration.getNameAsString())
                .orElse("");
    }

    private static Map<String, String> importsOf(final CompilationUnit unit) {
        final var imports = new HashMap<String, String>();
        final var onDemand = new ArrayList<String>();
        for (final var declaration : unit.getImports()) {
            if (declaration.isStatic()) {
                continue;
            }
            final var name = declaration.getNameAsString();
            if (declaration.isAsterisk()) {
                onDemand.add(name);
            } else {
                imports.put(name.substring(name.lastIndexOf('.') + 1), name);
            }
        }
        // A single on-demand import can stand in for the package a name came from. Several cannot
        // be told apart without the classpath, so none of them is used.
        if (onDemand.size() == 1) {
            imports.put("*", onDemand.getFirst());
        }
        return imports;
    }

    /**
     * What the names written in one file mean.
     *
     * <p>Resolution follows the language: an explicit import first, then a type sitting next to the
     * file, then {@code java.lang}, then a lone on-demand import. Only the neighbour needs looking
     * up, and only its source can answer, which is what {@code known} is for.</p>
     */
    private record Scope(String packageName, Map<String, String> imports, KnownTypes known) {

        String qualify(final String type) {
            final var matcher = TYPE_REFERENCE.matcher(type);
            final var result = new StringBuilder();
            while (matcher.find()) {
                final var reference = matcher.group().replaceAll("\\s+", "");
                matcher.appendReplacement(result, Matcher.quoteReplacement(qualifyReference(reference)));
            }
            matcher.appendTail(result);
            return result.toString().replaceAll("\\s+", "");
        }

        private String qualifyReference(final String reference) {
            if (PRIMITIVES.contains(reference)) {
                return reference;
            }
            final var dot = reference.indexOf('.');
            if (dot < 0) {
                return resolve(reference);
            }
            // A dotted name is already qualified unless it starts with a type: `java.util.UUID` is a
            // package path, `Outer.Inner` is a member of a type whose own name still has to be found.
            final var head = reference.substring(0, dot);
            return Character.isUpperCase(head.charAt(0))
                    ? resolve(head) + reference.substring(dot)
                    : reference;
        }

        private String resolve(final String simpleName) {
            final var imported = imports.get(simpleName);
            if (imported != null) {
                return imported;
            }
            final var neighbour = packageName.isEmpty() ? simpleName : packageName + "." + simpleName;
            if (known.hasSource(neighbour)) {
                return neighbour;
            }
            if (JAVA_LANG.contains(simpleName)) {
                return "java.lang." + simpleName;
            }
            final var onDemand = imports.get("*");
            return onDemand != null ? onDemand + "." + simpleName : neighbour;
        }

    }

}
