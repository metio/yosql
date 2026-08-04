/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.tooling.ant;

import org.apache.tools.ant.IntrospectionHelper;
import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * Everything the Ant frontend declares can actually be written in a build file.
 *
 * <p>Ant configures a task, and every nested element under it, by introspection: an attribute needs
 * a setter it can call with a String, and a child element needs an {@code addConfigured} or
 * {@code create} method. Neither is a compile-time contract, so a generated class can carry a field
 * per setting, compile, pass every other test here — and refuse the attribute at build time with
 * "doesn't support the ... attribute".</p>
 *
 * <p>That is what {@code List<String>} settings did: Ant cannot make a list out of a String, so
 * three documented settings were unreachable. The annotation elements did it too, having fields and
 * no setters at all.</p>
 *
 * <p>Asking {@link IntrospectionHelper} is asking the thing that decides, rather than a reading of
 * what it decides.</p>
 */
@DisplayName("what an Ant build can configure")
class AntConfigurabilityTest {

    private static final Project PROJECT = new Project();

    private static IntrospectionHelper helperFor(final Class<?> type) {
        return IntrospectionHelper.getHelper(PROJECT, type);
    }

    /**
     * Every group class the meta-model emits, and every setting on it, taken from the fields it
     * declares — which is one per setting.
     */
    static Stream<Arguments> settings() {
        final var groups = java.util.List.<Class<?>>of(
                Annotations.class, Converter.class, Files.class, Logging.class,
                Repositories.class, Resources.class, Schema.class);
        final var arguments = new ArrayList<Arguments>();
        for (final var group : groups) {
            final var helper = helperFor(group);
            final var attributes = java.util.Collections.list(helper.getAttributes());
            final var elements = java.util.Collections.list(helper.getNestedElements());
            for (final var field : group.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                arguments.add(Arguments.of(
                        group.getSimpleName() + "." + field.getName(),
                        field.getName().toLowerCase(Locale.ROOT),
                        attributes,
                        elements));
            }
        }
        return arguments.stream();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("settings")
    @DisplayName("is reachable as an attribute or a nested element")
    void isConfigurable(
            final String description,
            final String name,
            final java.util.List<String> attributes,
            final java.util.List<String> elements) {
        Assertions.assertTrue(attributes.contains(name) || elements.contains(name),
                () -> description + " is declared but Ant registers neither an attribute nor a nested "
                        + "element for it — a build file using it fails with \"doesn't support\".\n"
                        + "attributes: " + attributes + "\nelements: " + elements);
    }

    /**
     * The nested elements of the annotations group, which are configured exactly as a task is and so
     * need the same setters. Named rather than derived: they are the only child types the frontend
     * declares, and naming them says what is expected of one.
     */
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"type"})
    @DisplayName("includes the attributes of an annotation element")
    void annotationSpecTakesAttributes(final String attribute) {
        final var attributes = java.util.Collections.list(helperFor(AnnotationSpec.class).getAttributes());
        Assertions.assertTrue(attributes.contains(attribute),
                () -> "AnnotationSpec cannot be told its " + attribute + ": " + attributes);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"type", "key", "value"})
    @DisplayName("includes the attributes of an annotation member element")
    void annotationMemberSpecTakesAttributes(final String attribute) {
        final var attributes = java.util.Collections.list(
                helperFor(AnnotationMemberSpec.class).getAttributes());
        Assertions.assertTrue(attributes.contains(attribute),
                () -> "AnnotationMemberSpec cannot be told its " + attribute + ": " + attributes);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("lets an annotation element hold members")
    void annotationSpecTakesMembers() {
        final var elements = java.util.Collections.list(helperFor(AnnotationSpec.class).getNestedElements());
        Assertions.assertTrue(elements.contains("member"),
                () -> "an annotation with no members is an annotation nobody can parameterise: " + elements);
    }

}
