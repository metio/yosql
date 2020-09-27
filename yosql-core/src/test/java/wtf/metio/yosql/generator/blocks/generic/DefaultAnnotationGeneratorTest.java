package wtf.metio.yosql.generator.blocks.generic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static wtf.metio.yosql.i18n.I18nObjectMother.testTranslator;
import static wtf.metio.yosql.model.configuration.ModelConfigurationObjectMother.annotationConfig;
import static wtf.metio.yosql.model.options.AnnotationClassOptions.ANNOTATION_API;
import static wtf.metio.yosql.model.options.AnnotationClassOptions.PROCESSING_API;
import static wtf.metio.yosql.model.options.AnnotationMemberOptions.ALL;

@DisplayName("DefaultAnnotationGenerator")
class DefaultAnnotationGeneratorTest {

    @Test
    @DisplayName("can annotate classes")
    void shouldAnnotateClasses() {
        // given
        final var config = annotationConfig(ANNOTATION_API);
        final var generator = new DefaultAnnotationGenerator(config, testTranslator());

        // when
        final var annotations = generator.generatedClass();

        // then
        Assertions.assertEquals("""
                @javax.annotation.Generated(value = "test", comments = "class")""", annotations.iterator().next().toString());
    }

    @Test
    @DisplayName("can annotate fields")
    void shouldAnnotateFields() {
        // given
        final var config = annotationConfig(ANNOTATION_API);
        final var generator = new DefaultAnnotationGenerator(config, testTranslator());

        // when
        final var annotations = generator.generatedField();

        // then
        Assertions.assertEquals("""
                @javax.annotation.Generated(value = "test", comments = "field")""", annotations.iterator().next().toString());
    }

    @Test
    @DisplayName("can annotate methods")
    void shouldAnnotateMethods() {
        // given
        final var config = annotationConfig(ANNOTATION_API);
        final var generator = new DefaultAnnotationGenerator(config, testTranslator());

        // when
        final var annotations = generator.generatedMethod();

        // then
        Assertions.assertEquals("""
                @javax.annotation.Generated(value = "test", comments = "method")""", annotations.iterator().next().toString());
    }

    @Test
    @DisplayName("can add date to generated annotation")
    void shouldWriteDate() {
        // given
        final var config = annotationConfig(ANNOTATION_API, ALL);
        final var generator = new DefaultAnnotationGenerator(config, testTranslator());

        // when
        final var annotations = generator.generatedMethod();

        // then
        annotations.forEach(annotation -> Assertions.assertTrue(annotation.toString().contains("date")));
    }

    @Test
    @DisplayName("can use javax.annotation.processing.Generated")
    void shouldUseProcessingApi() {
        // given
        final var config = annotationConfig(PROCESSING_API);
        final var generator = new DefaultAnnotationGenerator(config, testTranslator());

        // when
        final var annotations = generator.generatedClass();

        // then
        Assertions.assertEquals("""
                @javax.annotation.processing.Generated(value = "test", comments = "class")""", annotations.iterator().next().toString());
    }

}