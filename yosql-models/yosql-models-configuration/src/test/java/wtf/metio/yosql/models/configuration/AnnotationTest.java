/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.configuration;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationTest {

    @Test
    void mergeAnnotationsFirst() {
        final List<Annotation> first = List.of(Annotation.builder().setType("com.example.MyAnnotation").build());
        final List<Annotation> second = List.of();

        final var merged = Annotation.mergeAnnotations(first, second);

        assertIterableEquals(first, merged);
    }

    @Test
    void mergeAnnotationsSecond() {
        final List<Annotation> first = List.of();
        final List<Annotation> second = List.of(Annotation.builder().setType("com.example.MyAnnotation").build());

        final var merged = Annotation.mergeAnnotations(first, second);

        assertIterableEquals(second, merged);
    }

    @Test
    void mergeAnnotationsDuplicates() {
        final List<Annotation> first = List.of(Annotation.builder().setType("com.example.MyAnnotation").build());
        final List<Annotation> second = List.of(Annotation.builder().setType("com.example.MyAnnotation").build());

        final var merged = Annotation.mergeAnnotations(first, second);

        assertIterableEquals(first, merged);
        assertIterableEquals(second, merged);
    }

    @Test
    void mergeAnnotationsWithMembersFirst() {
        final List<Annotation> first = List.of(Annotation.builder()
                .setType("com.example.MyAnnotation")
                .addMembers(AnnotationMember.builder().setKey("value").setValue("some").build())
                .build());
        final List<Annotation> second = List.of(Annotation.builder()
                .setType("com.example.MyAnnotation")
                .build());

        final var merged = Annotation.mergeAnnotations(first, second);

        assertIterableEquals(first, merged);
    }

    @Test
    void mergeAnnotationsWithMembersSecond() {
        final List<Annotation> first = List.of(Annotation.builder()
                .setType("com.example.MyAnnotation")
                .build());
        final List<Annotation> second = List.of(Annotation.builder()
                .setType("com.example.MyAnnotation")
                .addMembers(AnnotationMember.builder().setKey("value").setValue("some").build())
                .build());

        final var merged = Annotation.mergeAnnotations(first, second);

        assertIterableEquals(second, merged);
    }

    @Test
    void fromStringWithoutMembers() {
        final var annotationType = "com.example.MyAnnotation";
        final var input = String.format("%s", annotationType);
        final var annotation = Annotation.fromString(input);

        assertNotNull(annotation);
        assertAll(
                () -> assertEquals(annotationType, annotation.type()),
                () -> assertTrue(annotation.members().isEmpty()));
    }

    @Test
    void fromStringWithMember() {
        final var annotationType = "com.example.MyAnnotation";
        final var key = "some";
        final var value = "test";
        final var input = String.format("%s:%s|%s", annotationType, key, value);
        final var annotation = Annotation.fromString(input);

        assertNotNull(annotation);
        assertAll(
                () -> assertEquals(annotationType, annotation.type()),
                () -> assertFalse(annotation.members().isEmpty()),
                () -> assertEquals(key, annotation.members().get(0).key()),
                () -> assertEquals(value, annotation.members().get(0).value()),
                () -> assertEquals("java.lang.String", annotation.members().get(0).type()));
    }

    @Test
    void fromStringWithMembers() {
        final var annotationType = "com.example.MyAnnotation";
        final var key1 = "some";
        final var value1 = "test";
        final var key2 = "other";
        final var value2 = "123";
        final var type2 = "java.lang.Integer";
        final var input = String.format("%s:%s|%s:%s|%s|%s", annotationType, key1, value1, key2, value2, type2);
        final var annotation = Annotation.fromString(input);

        assertNotNull(annotation);
        assertAll(
                () -> assertEquals(annotationType, annotation.type()),
                () -> assertFalse(annotation.members().isEmpty()),
                () -> assertEquals(key1, annotation.members().get(0).key()),
                () -> assertEquals(value1, annotation.members().get(0).value()),
                () -> assertEquals("java.lang.String", annotation.members().get(0).type()),
                () -> assertEquals(key2, annotation.members().get(1).key()),
                () -> assertEquals(value2, annotation.members().get(1).value()),
                () -> assertEquals("java.lang.Integer", annotation.members().get(1).type()));
    }

    @Test
    void fromStringWithMembersUsingPrimitiveType() {
        final var annotationType = "com.example.MyAnnotation";
        final var key1 = "some";
        final var value1 = "test";
        final var key2 = "other";
        final var value2 = "true";
        final var type2 = "boolean";
        final var input = String.format("%s:%s|%s:%s|%s|%s", annotationType, key1, value1, key2, value2, type2);
        final var annotation = Annotation.fromString(input);

        assertNotNull(annotation);
        assertAll(
                () -> assertEquals(annotationType, annotation.type()),
                () -> assertFalse(annotation.members().isEmpty()),
                () -> assertEquals(key1, annotation.members().get(0).key()),
                () -> assertEquals(value1, annotation.members().get(0).value()),
                () -> assertEquals("java.lang.String", annotation.members().get(0).type()),
                () -> assertEquals(key2, annotation.members().get(1).key()),
                () -> assertEquals(value2, annotation.members().get(1).value()),
                () -> assertEquals("boolean", annotation.members().get(1).type()));
    }

    @Test
    void fromStringWithMembersUsingDifferentOrder() {
        final var annotationType = "com.example.MyAnnotation";
        final var key1 = "some";
        final var value1 = "test";
        final var type1 = "boolean";
        final var key2 = "other";
        final var value2 = "true";
        final var input = String.format("%s:%s|%s|%s:%s|%s", annotationType, key1, value1, type1, key2, value2);
        final var annotation = Annotation.fromString(input);

        assertNotNull(annotation);
        assertAll(
                () -> assertEquals(annotationType, annotation.type()),
                () -> assertFalse(annotation.members().isEmpty()),
                () -> assertEquals(key1, annotation.members().get(0).key()),
                () -> assertEquals(value1, annotation.members().get(0).value()),
                () -> assertEquals("boolean", annotation.members().get(0).type()),
                () -> assertEquals(key2, annotation.members().get(1).key()),
                () -> assertEquals(value2, annotation.members().get(1).value()),
                () -> assertEquals("java.lang.String", annotation.members().get(1).type()));
    }

}
