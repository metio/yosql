/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.exceptions.UnparsableRecordException;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        @DisplayName("reads a record declaring type parameters")
        void typeParameters() {
            final var type = parse("""
                    package com.example;

                    public record Tenant<T>(T value, String slug) {
                    }
                    """);
            assertEquals(List.of("value", "slug"), componentNames(type));
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
            assertTrue(exception.getMessage().contains("not closed"), exception.getMessage());
        }

        @Test
        @DisplayName("rejects a component that is only a type")
        void componentWithoutName() {
            final var exception = assertThrows(UnparsableRecordException.class, () -> parse("""
                    package com.example;

                    public record Tenant(String) {
                    }
                    """));
            assertTrue(exception.getMessage().contains("type followed by a name"), exception.getMessage());
        }

    }

}
