/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.configuration;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

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

}
