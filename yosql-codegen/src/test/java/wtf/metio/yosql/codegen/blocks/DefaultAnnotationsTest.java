/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.blocks;

import org.junit.jupiter.api.*;
import wtf.metio.yosql.internals.testing.configs.AnnotationsConfigurations;
import wtf.metio.yosql.models.configuration.Annotation;
import wtf.metio.yosql.models.configuration.AnnotationMember;

import static wtf.metio.yosql.codegen.logging.LoggingObjectMother.messages;
import static wtf.metio.yosql.codegen.orchestration.OrchestrationObjectMother.executionErrors;

@DisplayName("DefaultAnnotations")
class DefaultAnnotationsTest {

    @Nested
    @DisplayName("Default Configuration")
    class DefaultConfig {

        private DefaultAnnotations generator;

        @BeforeEach
        void setUp() {
            generator = new DefaultAnnotations(AnnotationsConfigurations.defaults(), executionErrors(), messages());
        }

        @Test
        @DisplayName("can annotate classes")
        void shouldAnnotateClasses() {
            // when
            final var annotations = generator.generatedClass();

            // then
            Assertions.assertEquals("""
                            @javax.annotation.processing.Generated(value = "YoSQL", comments = "DO NOT MODIFY - automatically generated by YoSQL")""",
                    annotations.iterator().next().toString());
        }

        @Test
        @DisplayName("can annotate fields")
        void shouldAnnotateFields() {
            // when
            final var annotations = generator.generatedField();

            // then
            Assertions.assertEquals("""
                            @javax.annotation.processing.Generated(value = "YoSQL", comments = "DO NOT MODIFY - automatically generated by YoSQL")""",
                    annotations.iterator().next().toString());
        }

        @Test
        @DisplayName("can annotate methods")
        void shouldAnnotateMethods() {
            // when
            final var annotations = generator.generatedMethod();

            // then
            Assertions.assertEquals("""
                            @javax.annotation.processing.Generated(value = "YoSQL", comments = "DO NOT MODIFY - automatically generated by YoSQL")""",
                    annotations.iterator().next().toString());
        }

        @Test
        @DisplayName("can use javax.annotation.processing.Generated")
        void shouldUseProcessingApi() {
            // when
            final var annotations = generator.generatedClass();

            // then
            Assertions.assertEquals("""
                            @javax.annotation.processing.Generated(value = "YoSQL", comments = "DO NOT MODIFY - automatically generated by YoSQL")""",
                    annotations.iterator().next().toString());
        }

    }

    @Nested
    @DisplayName("Custom Configuration")
    class CustomConfig {

        private DefaultAnnotations generator;

        @BeforeEach
        void setUp() {
            generator = new DefaultAnnotations(AnnotationsConfigurations.generated(), executionErrors(), messages());
        }

        @Test
        @DisplayName("can annotate classes")
        void shouldAnnotateClasses() {
            // when
            final var annotations = generator.generatedClass();

            // then
            Assertions.assertEquals("""
                            @javax.annotation.Generated(value = "YoSQL", comments = "DO NOT MODIFY - automatically generated by YoSQL")""",
                    annotations.iterator().next().toString());
        }

        @Test
        @DisplayName("can annotate fields")
        void shouldAnnotateFields() {
            // when
            final var annotations = generator.generatedField();

            // then
            Assertions.assertEquals("""
                            @javax.annotation.Generated(value = "YoSQL", comments = "DO NOT MODIFY - automatically generated by YoSQL")""",
                    annotations.iterator().next().toString());
        }

        @Test
        @DisplayName("can annotate methods")
        void shouldAnnotateMethods() {
            // when
            final var annotations = generator.generatedMethod();

            // then
            Assertions.assertEquals("""
                            @javax.annotation.Generated(value = "YoSQL", comments = "DO NOT MODIFY - automatically generated by YoSQL")""",
                    annotations.iterator().next().toString());
        }

        @Test
        @DisplayName("can use javax.annotation.Generated")
        void shouldUseAnnotationApi() {
            // when
            final var annotations = generator.generatedClass();

            // then
            Assertions.assertEquals("""
                            @javax.annotation.Generated(value = "YoSQL", comments = "DO NOT MODIFY - automatically generated by YoSQL")""",
                    annotations.iterator().next().toString());
        }

    }

    @Nested
    @DisplayName("")
    class UserGivenAnnotations {

        private DefaultAnnotations generator;

        @BeforeEach
        void setUp() {
            generator = new DefaultAnnotations(AnnotationsConfigurations.generated(), executionErrors(), messages());
        }

        @Test
        void asAnnotationSpec() {
            // given
            final var annotation = Annotation.builder().setType("com.example.Annotation").build();

            // when
            final var spec = generator.asAnnotationSpec(annotation);

            // then
            Assertions.assertTrue(spec.isPresent());
            Assertions.assertEquals("""
                    @com.example.Annotation""", spec.get().toString());
        }

        @Test
        void asAnnotationSpecWithMember() {
            // given
            final var annotation = Annotation.builder()
                    .setType("com.example.Annotation")
                    .addMembers(AnnotationMember.builder().setKey("value").setValue("test").build())
                    .build();

            // when
            final var spec = generator.asAnnotationSpec(annotation);

            // then
            Assertions.assertTrue(spec.isPresent());
            Assertions.assertEquals("""
                    @com.example.Annotation("test")""", spec.get().toString());
        }

        @Test
        void asAnnotationSpecWithMemberUsingCustomKey() {
            // given
            final var annotation = Annotation.builder()
                    .setType("com.example.Annotation")
                    .addMembers(AnnotationMember.builder().setKey("random").setValue("test").build())
                    .build();

            // when
            final var spec = generator.asAnnotationSpec(annotation);

            // then
            Assertions.assertTrue(spec.isPresent());
            Assertions.assertEquals("""
                    @com.example.Annotation(random = "test")""", spec.get().toString());
        }

        @Test
        void asAnnotationSpecWithMembers() {
            // given
            final var annotation = Annotation.builder()
                    .setType("com.example.Annotation")
                    .addMembers(AnnotationMember.builder().setKey("value").setValue("test").build())
                    .addMembers(AnnotationMember.builder().setKey("another").setValue("value").build())
                    .build();

            // when
            final var spec = generator.asAnnotationSpec(annotation);

            // then
            Assertions.assertTrue(spec.isPresent());
            Assertions.assertEquals("""
                    @com.example.Annotation(value = "test", another = "value")""", spec.get().toString());
        }

        @Test
        void asAnnotationSpecWithMemberUsingBoolType() {
            // given
            final var annotation = Annotation.builder()
                    .setType("com.example.Annotation")
                    .addMembers(AnnotationMember.builder()
                            .setKey("test")
                            .setValue("true")
                            .setType("boolean")
                            .build())
                    .build();

            // when
            final var spec = generator.asAnnotationSpec(annotation);

            // then
            Assertions.assertTrue(spec.isPresent());
            Assertions.assertEquals("""
                    @com.example.Annotation(test = true)""", spec.get().toString());
        }

        @Test
        void asAnnotationSpecWithMemberUsingIntType() {
            // given
            final var annotation = Annotation.builder()
                    .setType("com.example.Annotation")
                    .addMembers(AnnotationMember.builder()
                            .setKey("test")
                            .setValue("123")
                            .setType("int")
                            .build())
                    .build();

            // when
            final var spec = generator.asAnnotationSpec(annotation);

            // then
            Assertions.assertTrue(spec.isPresent());
            Assertions.assertEquals("""
                    @com.example.Annotation(test = 123)""", spec.get().toString());
        }

        @Test
        void asAnnotationSpecWithMemberUsingLongType() {
            // given
            final var annotation = Annotation.builder()
                    .setType("com.example.Annotation")
                    .addMembers(AnnotationMember.builder()
                            .setKey("test")
                            .setValue("456789")
                            .setType("long")
                            .build())
                    .build();

            // when
            final var spec = generator.asAnnotationSpec(annotation);

            // then
            Assertions.assertTrue(spec.isPresent());
            Assertions.assertEquals("""
                    @com.example.Annotation(test = 456789)""", spec.get().toString());
        }

        @Test
        void asAnnotationSpecWithMemberUsingShortType() {
            // given
            final var annotation = Annotation.builder()
                    .setType("com.example.Annotation")
                    .addMembers(AnnotationMember.builder()
                            .setKey("test")
                            .setValue("123")
                            .setType("short")
                            .build())
                    .build();

            // when
            final var spec = generator.asAnnotationSpec(annotation);

            // then
            Assertions.assertTrue(spec.isPresent());
            Assertions.assertEquals("""
                    @com.example.Annotation(test = 123)""", spec.get().toString());
        }

        @Test
        void asAnnotationSpecWithMemberUsingByteType() {
            // given
            final var annotation = Annotation.builder()
                    .setType("com.example.Annotation")
                    .addMembers(AnnotationMember.builder()
                            .setKey("test")
                            .setValue("123")
                            .setType("byte")
                            .build())
                    .build();

            // when
            final var spec = generator.asAnnotationSpec(annotation);

            // then
            Assertions.assertTrue(spec.isPresent());
            Assertions.assertEquals("""
                    @com.example.Annotation(test = 123)""", spec.get().toString());
        }

        @Test
        void asAnnotationSpecWithMemberUsingFloatType() {
            // given
            final var annotation = Annotation.builder()
                    .setType("com.example.Annotation")
                    .addMembers(AnnotationMember.builder()
                            .setKey("test")
                            .setValue("3.14")
                            .setType("float")
                            .build())
                    .build();

            // when
            final var spec = generator.asAnnotationSpec(annotation);

            // then
            Assertions.assertTrue(spec.isPresent());
            Assertions.assertEquals("""
                    @com.example.Annotation(test = 3.14)""", spec.get().toString());
        }

        @Test
        void asAnnotationSpecWithMemberUsingDoubleType() {
            // given
            final var annotation = Annotation.builder()
                    .setType("com.example.Annotation")
                    .addMembers(AnnotationMember.builder()
                            .setKey("test")
                            .setValue("3.14")
                            .setType("double")
                            .build())
                    .build();

            // when
            final var spec = generator.asAnnotationSpec(annotation);

            // then
            Assertions.assertTrue(spec.isPresent());
            Assertions.assertEquals("""
                    @com.example.Annotation(test = 3.14)""", spec.get().toString());
        }

        @Test
        void asAnnotationSpecWithMemberUsingCharType() {
            // given
            final var annotation = Annotation.builder()
                    .setType("com.example.Annotation")
                    .addMembers(AnnotationMember.builder()
                            .setKey("test")
                            .setValue("a")
                            .setType("char")
                            .build())
                    .build();

            // when
            final var spec = generator.asAnnotationSpec(annotation);

            // then
            Assertions.assertTrue(spec.isPresent());
            Assertions.assertEquals("""
                    @com.example.Annotation(test = 'a')""", spec.get().toString());
        }

        @Test
        void asAnnotationSpecWithUnguessableType() {
            // given
            final var annotation = Annotation.builder()
                    .setType("")
                    .build();

            // when
            final var spec = generator.asAnnotationSpec(annotation);

            // then
            Assertions.assertTrue(spec.isEmpty());
        }

        @Test
        void asAnnotationSpecWithUnguessableMemberType() {
            // given
            final var annotation = Annotation.builder()
                    .setType("com.example.Annotation")
                    .addMembers(AnnotationMember.builder()
                            .setKey("test")
                            .setValue("a")
                            .setType("")
                            .build())
                    .build();

            // when
            final var spec = generator.asAnnotationSpec(annotation);

            // then
            Assertions.assertTrue(spec.isPresent());
            Assertions.assertEquals("""
                    @com.example.Annotation""", spec.get().toString());
        }

    }

}
