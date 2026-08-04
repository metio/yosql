/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.palantir.javapoet.ArrayTypeName;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.exceptions.UnparsableRecordException;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("JavaSourceParser")
class JavaSourceParserTest {

    private static final Path LOCATION = Path.of("src", "main", "java", "com", "example", "Tenant.java");
    private static final ClassName TENANT = ClassName.get("com.example", "Tenant");

    private JavaSourceParser parser;

    @BeforeEach
    void setUp() {
        parser = new JavaSourceParser();
    }

    private JavaSourceType parse(final String source) {
        return parser.parse(source, LOCATION, TENANT);
    }

    private List<String> componentNames(final JavaSourceType type) {
        return type.components().stream().map(JavaSourceComponent::name).toList();
    }

    private TypeName typeOf(final JavaSourceType type, final String component) {
        return type.components().stream()
                .filter(candidate -> candidate.name().equals(component))
                .findFirst()
                .orElseThrow()
                .type();
    }

    @Nested
    @DisplayName("kinds")
    class Kinds {

        @Test
        @DisplayName("reads a record")
        void record() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(String slug) {
                    }
                    """);
            assertTrue(type.isRecord());
            assertFalse(type.isEnum());
            assertEquals(TENANT, type.type());
        }

        @Test
        @DisplayName("reads an enum")
        void enumeration() {
            final var type = parse("""
                    package com.example;

                    public enum Tenant {
                        ONE,
                        TWO
                    }
                    """);
            assertTrue(type.isEnum());
            assertFalse(type.isRecord());
            assertEquals(List.of(), type.components());
        }

        @Test
        @DisplayName("reads a class as neither")
        void plainClass() {
            final var type = parse("""
                    package com.example;

                    public class Tenant {
                        private String slug;
                    }
                    """);
            assertFalse(type.isRecord());
            assertFalse(type.isEnum());
        }

        @Test
        @DisplayName("reads an interface as neither")
        void anInterface() {
            final var type = parse("""
                    package com.example;

                    public interface Tenant {
                    }
                    """);
            assertFalse(type.isRecord());
            assertFalse(type.isEnum());
        }

    }

    @Nested
    @DisplayName("components")
    class Components {

        @Test
        @DisplayName("keeps declaration order")
        void order() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(String slug, String name, String language) {
                    }
                    """);
            assertEquals(List.of("slug", "name", "language"), componentNames(type));
        }

        @Test
        @DisplayName("reads components spread over several lines")
        void multiline() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(
                            String slug,
                            String name) {
                    }
                    """);
            assertEquals(List.of("slug", "name"), componentNames(type));
        }

        @Test
        @DisplayName("reads primitives")
        void primitives() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(long id, int count, boolean active, double rate) {
                    }
                    """);
            assertEquals(TypeName.LONG, typeOf(type, "id"));
            assertEquals(TypeName.INT, typeOf(type, "count"));
            assertEquals(TypeName.BOOLEAN, typeOf(type, "active"));
            assertEquals(TypeName.DOUBLE, typeOf(type, "rate"));
        }

        @Test
        @DisplayName("resolves a type the same file declares, rather than a sibling that does not exist")
        void nestedTypeInTheSameFile() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(java.util.UUID id, Money balance) {
                        public record Money(long cents) {
                        }
                    }
                    """);
            assertEquals(ClassName.get("com.example", "Tenant", "Money"), typeOf(type, "balance"));
        }

        @Test
        @DisplayName("resolves a sibling top-level type of the same file")
        void siblingTypeInTheSameFile() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(java.util.UUID id, Slug slug) {
                    }

                    record Slug(String value) {
                    }
                    """);
            assertEquals(ClassName.get("com.example", "Slug"), typeOf(type, "slug"));
        }

        @Test
        @DisplayName("prefers an explicit import over a type of the same name declared here")
        void importWinsOverDeclaration() {
            final var type = parse("""
                    package com.example;

                    import com.example.money.Money;

                    public record Tenant(Money balance) {
                    }
                    """);
            assertEquals(ClassName.get("com.example.money", "Money"), typeOf(type, "balance"));
        }

        @Test
        @DisplayName("a non-ASCII name is one name, not the fragments around its letters")
        void readsNonAsciiTypeNames() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(Größe größe) {
                    }
                    """);

            assertAll(
                    () -> assertIterableEquals(List.of("größe"), componentNames(type)),
                    () -> assertEquals(ClassName.get("com.example", "Größe"), typeOf(type, "größe"),
                            "matching identifiers as ASCII split this into pieces and qualified each "
                                    + "one into a type nobody declared"));
        }

        @Test
        @DisplayName("reads an array written before the name")
        void arrayBeforeName() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(byte[] payload) {
                    }
                    """);
            assertEquals(ArrayTypeName.of(TypeName.BYTE), typeOf(type, "payload"));
        }

        @Test
        @DisplayName("reads an array written after the name")
        void arrayAfterName() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(byte payload[]) {
                    }
                    """);
            assertEquals(ArrayTypeName.of(TypeName.BYTE), typeOf(type, "payload"));
        }

        @Test
        @DisplayName("reads varargs as an array")
        void varargs() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(String slug, byte... payload) {
                    }
                    """);
            assertEquals(ArrayTypeName.of(TypeName.BYTE), typeOf(type, "payload"));
        }

        @Test
        @DisplayName("keeps a generic type's arguments")
        void generics() {
            final var type = parse("""
                    package com.example;

                    import java.util.List;

                    public record Tenant(List<String> tags) {
                    }
                    """);
            assertEquals("java.util.List<java.lang.String>", typeOf(type, "tags").toString());
        }

        @Test
        @DisplayName("ignores a marker annotation on a component")
        void markerAnnotation() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(@Deprecated String slug) {
                    }
                    """);
            assertEquals(List.of("slug"), componentNames(type));
            assertEquals(ClassName.get(String.class), typeOf(type, "slug"));
        }

        @Test
        @DisplayName("ignores an annotation carrying arguments, commas and all")
        void annotationWithArguments() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(@Size(min = 1, max = 64) String slug, String name) {
                    }
                    """);
            assertEquals(List.of("slug", "name"), componentNames(type));
        }

        @Test
        @DisplayName("ignores several annotations on one component")
        void severalAnnotations() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(@Deprecated @Size(min = 1) String slug) {
                    }
                    """);
            assertEquals(List.of("slug"), componentNames(type));
        }

        @Test
        @DisplayName("rejects a record declaring type parameters")
        void typeParameters() {
            final var exception = assertThrows(UnparsableRecordException.class, () -> parse("""
                    package com.example;

                    public record Tenant<T>(T value, String slug) {
                    }
                    """));
            assertTrue(exception.getMessage().contains("type parameters"), exception.getMessage());
        }

        @Test
        @DisplayName("reads a record that has a body")
        void withBody() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(String slug) {
                        public Tenant {
                            if (slug == null) {
                                throw new IllegalArgumentException("slug");
                            }
                        }

                        public String upper() {
                            return slug.toUpperCase();
                        }
                    }
                    """);
            assertEquals(List.of("slug"), componentNames(type));
        }

        @Test
        @DisplayName("reads a record nested inside a class")
        void nestedDeclaration() {
            final var type = parse("""
                    package com.example;

                    public final class Holder {
                        public record Tenant(String slug) {
                        }
                    }
                    """);
            assertTrue(type.isRecord());
            assertEquals(List.of("slug"), componentNames(type));
        }

    }

    @Nested
    @DisplayName("type resolution")
    class Resolution {

        @Test
        @DisplayName("resolves a simple name through its import")
        void throughImport() {
            final var type = parse("""
                    package com.example;

                    import java.util.UUID;

                    public record Tenant(UUID id) {
                    }
                    """);
            assertEquals(ClassName.get("java.util", "UUID"), typeOf(type, "id"));
        }

        @Test
        @DisplayName("leaves an already qualified name alone")
        void alreadyQualified() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(java.util.UUID id) {
                    }
                    """);
            assertEquals(ClassName.get("java.util", "UUID"), typeOf(type, "id"));
        }

        @Test
        @DisplayName("resolves an unimported name to java.lang when it lives there")
        void javaLang() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(String slug, Long size) {
                    }
                    """);
            assertEquals(ClassName.get("java.lang", "String"), typeOf(type, "slug"));
            assertEquals(ClassName.get("java.lang", "Long"), typeOf(type, "size"));
        }

        @Test
        @DisplayName("falls back to the file's own package")
        void samePackage() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(Money balance) {
                    }
                    """);
            assertEquals(ClassName.get("com.example", "Money"), typeOf(type, "balance"));
        }

        @Test
        @DisplayName("uses a single on-demand import when nothing else resolves the name")
        void singleWildcardImport() {
            final var type = parse("""
                    package com.example;

                    import com.example.domain.*;

                    public record Tenant(Money balance) {
                    }
                    """);
            assertEquals(ClassName.get("com.example.domain", "Money"), typeOf(type, "balance"));
        }

        @Test
        @DisplayName("ignores on-demand imports when several could have supplied the name")
        void severalWildcardImports() {
            // Two candidates cannot be told apart without the classpath, so the file's own package
            // is the answer rather than a coin flip.
            final var type = parse("""
                    package com.example;

                    import com.example.domain.*;
                    import com.example.billing.*;

                    public record Tenant(Money balance) {
                    }
                    """);
            assertEquals(ClassName.get("com.example", "Money"), typeOf(type, "balance"));
        }

        @Test
        @DisplayName("ignores static imports")
        void staticImports() {
            final var type = parse("""
                    package com.example;

                    import static java.util.Objects.requireNonNull;

                    public record Tenant(Money balance) {
                    }
                    """);
            assertEquals(ClassName.get("com.example", "Money"), typeOf(type, "balance"));
        }

        @Test
        @DisplayName("handles a file without a package declaration")
        void defaultPackage() {
            final var type = parser.parse("""
                    public record Tenant(Money balance) {
                    }
                    """, LOCATION, ClassName.get("", "Tenant"));
            assertEquals(ClassName.get("", "Money"), typeOf(type, "balance"));
        }

        @Test
        @DisplayName("resolves an unimported name to java.lang beyond the handful in daily use")
        void resolvesLessCommonJavaLangTypes() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(StringBuilder text, Process job) {
                    }
                    """);

            assertAll(
                    () -> assertEquals(ClassName.get(StringBuilder.class), typeOf(type, "text")),
                    () -> assertEquals(ClassName.get(Process.class), typeOf(type, "job"))
            );
        }

        @Test
        @DisplayName("a type of this project outranks the java.lang type of the same name")
        void ownTypeBeatsJavaLang() {
            final var type = new JavaSourceParser().parse("""
                    package com.example;

                    public record Tenant(Process job) {
                    }
                    """, LOCATION, TENANT, "com.example.Process"::equals);

            assertEquals(ClassName.get("com.example", "Process"), typeOf(type, "job"));
        }

        @Test
        @DisplayName("a member type is qualified through the import of the type holding it")
        void resolvesNestedTypeReference() {
            final var type = parse("""
                    package com.example;

                    import com.example.other.Outer;

                    public record Tenant(Outer.Inner value) {
                    }
                    """);

            assertEquals(ClassName.get("com.example.other", "Outer", "Inner"), typeOf(type, "value"));
        }

        @Test
        @DisplayName("a package path is left alone")
        void leavesPackagePathsAlone() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(java.util.UUID id) {
                    }
                    """);

            assertEquals(ClassName.get(java.util.UUID.class), typeOf(type, "id"));
        }

        @Test
        @DisplayName("resolves both halves of a generic type")
        void genericArguments() {
            final var type = parse("""
                    package com.example;

                    import java.util.List;
                    import java.util.UUID;

                    public record Tenant(List<UUID> owners) {
                    }
                    """);
            assertEquals("java.util.List<java.util.UUID>", typeOf(type, "owners").toString());
        }

    }

    @Nested
    @DisplayName("text that is not code")
    class NotCode {

        @Test
        @DisplayName("ignores a record declaration inside a line comment")
        void lineComment() {
            final var type = parse("""
                    package com.example;

                    // public record Tenant(String decoy, String other) {
                    public record Tenant(String slug) {
                    }
                    """);
            assertEquals(List.of("slug"), componentNames(type));
        }

        @Test
        @DisplayName("ignores a record declaration inside a block comment")
        void blockComment() {
            final var type = parse("""
                    package com.example;

                    /*
                     * public record Tenant(String decoy) {
                     */
                    public record Tenant(String slug) {
                    }
                    """);
            assertEquals(List.of("slug"), componentNames(type));
        }

        @Test
        @DisplayName("ignores a record declaration inside javadoc")
        void javadoc() {
            final var type = parse("""
                    package com.example;

                    /**
                     * Looks like {@code record Tenant(String decoy)} but is prose.
                     */
                    public record Tenant(String slug) {
                    }
                    """);
            assertEquals(List.of("slug"), componentNames(type));
        }

        @Test
        @DisplayName("ignores parentheses and commas inside a string literal")
        void stringLiteral() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(String slug) {
                        public static final String NOTE = "record Tenant(String a, String b) {";
                    }
                    """);
            assertEquals(List.of("slug"), componentNames(type));
        }

        @Test
        @DisplayName("ignores a text block")
        void textBlock() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(String slug) {
                        public static final String QUERY = \"""
                                select a, b, c
                                from record Tenant(nope)
                                \""";
                    }
                    """);
            assertEquals(List.of("slug"), componentNames(type));
        }

        @Test
        @DisplayName("ignores an escaped quote inside a character literal")
        void characterLiteral() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(String slug) {
                        public static final char QUOTE = '\\'';
                        public static final char COMMA = ',';
                    }
                    """);
            assertEquals(List.of("slug"), componentNames(type));
        }

    }

    @Nested
    @DisplayName("value factories")
    class Factories {

        @Test
        @DisplayName("finds a static valueOf returning the type")
        void findsValueOf() {
            final var type = parse("""
                    package com.example;

                    import java.util.UUID;

                    public record Tenant(UUID value) {
                        public static Tenant valueOf(final UUID value) {
                            return new Tenant(value);
                        }
                    }
                    """);
            assertTrue(type.hasValueOf());
            assertEquals(List.of(ClassName.get("java.util", "UUID")), type.valueOfParameters());
        }

        @Test
        @DisplayName("finds one on a nested type, whose return type is written unqualified")
        void findsValueOfOnANestedType() {
            final var money = ClassName.get("com.example", "Tenant", "Money");
            final var type = parser.parse("""
                    package com.example;

                    public record Tenant(java.util.UUID id, Money balance) {
                        public record Money(long cents) {
                            public static Money valueOf(final long cents) {
                                return new Money(cents);
                            }
                        }
                    }
                    """, LOCATION, money);
            assertAll(
                    () -> assertTrue(type.hasValueOf(),
                            "the factory returns Money, and inside this file Money means Tenant.Money"),
                    () -> assertEquals(List.of(TypeName.LONG), type.valueOfParameters()));
        }

        @Test
        @DisplayName("finds one on a class as well as on a record")
        void findsValueOfOnAClass() {
            final var type = parse("""
                    package com.example;

                    public final class Tenant {
                        public static Tenant valueOf(String slug) {
                            return new Tenant();
                        }
                    }
                    """);
            assertFalse(type.isRecord());
            assertEquals(List.of(ClassName.get("java.lang", "String")), type.valueOfParameters());
        }

        @Test
        @DisplayName("does not care in which order the modifiers were written")
        void modifierOrder() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(long value) {
                        static public Tenant valueOf(long value) {
                            return new Tenant(value);
                        }
                    }
                    """);
            assertEquals(List.of(TypeName.LONG), type.valueOfParameters());
        }

        @Test
        @DisplayName("reads a parameter declared final")
        void finalParameter() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(String value) {
                        public static Tenant valueOf(final String value) {
                            return new Tenant(value);
                        }
                    }
                    """);
            assertEquals(List.of(ClassName.get("java.lang", "String")), type.valueOfParameters());
        }

        @Test
        @DisplayName("keeps every overload rather than choosing between them")
        void overloads() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(String value) {
                        public static Tenant valueOf(String value) {
                            return new Tenant(value);
                        }

                        public static Tenant valueOf(long value) {
                            return new Tenant(Long.toString(value));
                        }
                    }
                    """);
            assertEquals(2, type.valueOfParameters().size());
        }

        @Test
        @DisplayName("ignores a valueOf that returns something else")
        void ignoresForeignReturnType() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(String value) {
                        public static String valueOf(int input) {
                            return Integer.toString(input);
                        }
                    }
                    """);
            assertFalse(type.hasValueOf());
        }

        @Test
        @DisplayName("ignores an instance method of the same name")
        void ignoresInstanceMethod() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(String value) {
                        public Tenant valueOf(String other) {
                            return new Tenant(other);
                        }
                    }
                    """);
            assertFalse(type.hasValueOf());
        }

        @Test
        @DisplayName("ignores one taking no argument or several")
        void ignoresWrongArity() {
            assertFalse(parse("""
                    package com.example;

                    public record Tenant(String value) {
                        public static Tenant valueOf() {
                            return new Tenant("");
                        }
                    }
                    """).hasValueOf());
            assertFalse(parse("""
                    package com.example;

                    public record Tenant(String value) {
                        public static Tenant valueOf(String a, String b) {
                            return new Tenant(a + b);
                        }
                    }
                    """).hasValueOf());
        }

        @Test
        @DisplayName("ignores one written inside a comment")
        void ignoresCommentedFactory() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(String value) {
                        // public static Tenant valueOf(String value) { … }
                    }
                    """);
            assertFalse(type.hasValueOf());
        }

        @Test
        @DisplayName("a factory declared in a nested type belongs to that type")
        void ignoresFactoryInNestedType() {
            final var type = parse("""
                    package com.example;

                    import java.util.UUID;

                    public record Tenant(UUID value) {
                        public static Tenant valueOf(UUID value) { return new Tenant(value); }
                        record Other(UUID v) {
                            static Tenant valueOf(String slug) { return null; }
                        }
                    }
                    """);

            assertIterableEquals(List.of(ClassName.get(java.util.UUID.class)), type.valueOfParameters());
        }

        @Test
        @DisplayName("says a record has none when it declares none")
        void plainRecordHasNoFactory() {
            final var type = parse("""
                    package com.example;

                    public record Tenant(String slug, String name) {
                    }
                    """);
            assertFalse(type.hasValueOf());
        }

    }

    @Nested
    @DisplayName("diagnostics")
    class Diagnostics {

        @Test
        @DisplayName("names the file and the type when the declaration is missing")
        void missingDeclaration() {
            final var exception = assertThrows(UnparsableRecordException.class, () -> parse("""
                    package com.example;

                    public record Other(String slug) {
                    }
                    """));
            assertTrue(exception.getMessage().contains("com.example.Tenant"), exception.getMessage());
            assertTrue(exception.getMessage().contains("Tenant.java"), exception.getMessage());
        }

        @Test
        @DisplayName("rejects a record with no components")
        void noComponents() {
            final var exception = assertThrows(UnparsableRecordException.class, () -> parse("""
                    package com.example;

                    public record Tenant() {
                    }
                    """));
            assertTrue(exception.getMessage().contains("no components"), exception.getMessage());
        }

        @Test
        @DisplayName("rejects an unclosed component list")
        void unclosedComponents() {
            final var exception = assertThrows(UnparsableRecordException.class, () -> parse("""
                    package com.example;

                    public record Tenant(String slug
                    """));
            assertTrue(exception.getMessage().contains("Tenant.java"), exception.getMessage());
        }

        @Test
        @DisplayName("rejects a component that is only a type")
        void componentWithoutName() {
            final var exception = assertThrows(UnparsableRecordException.class, () -> parse("""
                    package com.example;

                    public record Tenant(String) {
                    }
                    """));
            assertTrue(exception.getMessage().contains("Tenant.java"), exception.getMessage());
        }

    }

    @Nested
    @DisplayName("converter methods")
    class ConverterMethods {

        private List<String> methodNames(final String source) {
            return parse(source).resultSetMethods().stream().map(JavaSourceMethod::name).toList();
        }

        @Test
        @DisplayName("finds a public method taking a ResultSet")
        void findsConverterMethod() {
            final var type = parse("""
                    package com.example;

                    import java.sql.ResultSet;

                    public class Tenant {
                        public String asUserType(ResultSet resultSet) {
                            return null;
                        }
                    }
                    """);

            assertAll(
                    () -> assertEquals(1, type.resultSetMethods().size()),
                    () -> assertEquals("asUserType", type.resultSetMethods().get(0).name()),
                    () -> assertEquals(ClassName.get(String.class), type.resultSetMethods().get(0).returnType())
            );
        }

        @Test
        @DisplayName("a method returning nothing is not a converter")
        void ignoresVoidMethods() {
            assertIterableEquals(List.of("read"), methodNames("""
                    package com.example;

                    import java.sql.ResultSet;

                    public class Tenant {
                        public void consume(ResultSet resultSet) {
                        }

                        public String read(ResultSet resultSet) {
                            return null;
                        }
                    }
                    """), "a converter hands a row back, and asking for a type made out of 'void' "
                    + "threw an IllegalArgumentException naming neither the class nor the method");
        }

        @Test
        @DisplayName("a final parameter is the same parameter")
        void acceptsFinalParameter() {
            assertIterableEquals(List.of("read"), methodNames("""
                    package com.example;

                    import java.sql.ResultSet;

                    public class Tenant {
                        public String read(final ResultSet resultSet) { return null; }
                    }
                    """));
        }

        @Test
        @DisplayName("a fully-qualified ResultSet is the same type")
        void acceptsQualifiedParameter() {
            assertIterableEquals(List.of("read"), methodNames("""
                    package com.example;

                    public class Tenant {
                        public String read(java.sql.ResultSet resultSet) { return null; }
                    }
                    """));
        }

        @Test
        @DisplayName("throws clauses do not hide the method")
        void acceptsThrowsClause() {
            assertIterableEquals(List.of("read"), methodNames("""
                    package com.example;

                    import java.sql.ResultSet;
                    import java.sql.SQLException;

                    public class Tenant {
                        public String read(ResultSet resultSet) throws SQLException { return null; }
                    }
                    """));
        }

        @Test
        @DisplayName("a static method is not called on an instance")
        void skipsStaticMethods() {
            assertTrue(methodNames("""
                    package com.example;

                    import java.sql.ResultSet;

                    public class Tenant {
                        public static Tenant of(ResultSet resultSet) { return null; }
                    }
                    """).isEmpty());
        }

        @Test
        @DisplayName("a method the repository cannot reach is not a candidate")
        void skipsNonPublicMethods() {
            assertTrue(methodNames("""
                    package com.example;

                    import java.sql.ResultSet;

                    public class Tenant {
                        String packagePrivate(ResultSet resultSet) { return null; }
                        private String hidden(ResultSet resultSet) { return null; }
                        protected String shielded(ResultSet resultSet) { return null; }
                    }
                    """).isEmpty());
        }

        @Test
        @DisplayName("a method taking something else is not a candidate")
        void skipsOtherParameters() {
            assertTrue(methodNames("""
                    package com.example;

                    import java.sql.ResultSet;

                    public class Tenant {
                        public String read(String notAResultSet) { return null; }
                        public String readTwo(ResultSet resultSet, int column) { return null; }
                    }
                    """).isEmpty());
        }

        @Test
        @DisplayName("reports every candidate so an ambiguous class can be named")
        void findsSeveralMethods() {
            assertIterableEquals(List.of("readName", "readAge"), methodNames("""
                    package com.example;

                    import java.sql.ResultSet;

                    public class Tenant {
                        public String readName(ResultSet resultSet) { return null; }
                        public Integer readAge(ResultSet resultSet) { return null; }
                    }
                    """));
        }

        @Test
        @DisplayName("an import decides what an unqualified return type means")
        void qualifiesReturnTypeFromImports() {
            final var type = parse("""
                    package com.example;

                    import com.example.domain.Item;

                    import java.sql.ResultSet;

                    public class Tenant {
                        public Item read(ResultSet resultSet) { return null; }
                    }
                    """);

            assertEquals(ClassName.get("com.example.domain", "Item"), type.resultSetMethods().get(0).returnType());
        }

        @Test
        @DisplayName("a generic return type keeps its arguments")
        void keepsTypeArguments() {
            final var type = parse("""
                    package com.example;

                    import java.sql.ResultSet;
                    import java.util.List;

                    public class Tenant {
                        public List<String> read(ResultSet resultSet) { return null; }
                    }
                    """);

            assertEquals("java.util.List<java.lang.String>", type.resultSetMethods().get(0).returnType().toString());
        }

        @Test
        @DisplayName("a ResultSet named in a comment is not a method")
        void ignoresComments() {
            assertTrue(methodNames("""
                    package com.example;

                    public class Tenant {
                        // public String read(ResultSet resultSet)
                        /* public String other(ResultSet resultSet) */
                    }
                    """).isEmpty());
        }

        @Test
        @DisplayName("a ResultSet named in a string literal is not a method")
        void ignoresStringLiterals() {
            assertTrue(methodNames("""
                    package com.example;

                    public class Tenant {
                        private static final String DOC = "public String read(ResultSet resultSet)";
                    }
                    """).isEmpty());
        }

        @Test
        @DisplayName("a method declared in a nested class belongs to that class")
        void ignoresMethodInNestedClass() {
            assertIterableEquals(List.of("asUserType"), methodNames("""
                    package com.example;

                    import java.sql.ResultSet;

                    public final class Tenant {
                        public String asUserType(ResultSet resultSet) { return null; }
                        private static final class Helper {
                            public Integer read(ResultSet resultSet) { return null; }
                        }
                    }
                    """));
        }

        @Test
        @DisplayName("a method declared in an anonymous class is not the converter's")
        void ignoresMethodInAnonymousClass() {
            assertIterableEquals(List.of("asUserType"), methodNames("""
                    package com.example;

                    import java.sql.ResultSet;

                    public final class Tenant {
                        public String asUserType(ResultSet resultSet) { return null; }
                        private final Object helper = new Object() {
                            public Integer read(ResultSet resultSet) { return null; }
                        };
                    }
                    """));
        }

        @Test
        @DisplayName("a local class inside a method body declares nothing for the type")
        void ignoresMethodBodies() {
            assertIterableEquals(List.of("asUserType"), methodNames("""
                    package com.example;

                    import java.sql.ResultSet;

                    public final class Tenant {
                        public String asUserType(ResultSet resultSet) {
                            class Local {
                                public Integer read(ResultSet nested) { return null; }
                            }
                            return null;
                        }
                    }
                    """));
        }

        @Test
        @DisplayName("a record can carry its own converter method")
        void findsMethodOnRecord() {
            assertIterableEquals(List.of("read"), methodNames("""
                    package com.example;

                    import java.sql.ResultSet;

                    public record Tenant(String slug) {
                        public Tenant read(ResultSet resultSet) { return null; }
                    }
                    """));
        }

    }

}
