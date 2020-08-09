package wtf.metio.yosql.generator.blocks.generic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.model.configuration.AnnotationConfiguration;
import wtf.metio.yosql.model.options.AnnotationClassOptions;
import wtf.metio.yosql.model.options.AnnotationMemberOptions;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

import static wtf.metio.yosql.i18n.I18nObjectMother.testTranslator;
import static wtf.metio.yosql.model.options.AnnotationClassOptions.ANNOTATION_API;
import static wtf.metio.yosql.model.options.AnnotationClassOptions.PROCESSING_API;
import static wtf.metio.yosql.model.options.AnnotationMemberOptions.ALL;
import static wtf.metio.yosql.model.options.AnnotationMemberOptions.WITHOUT_DATE;

@DisplayName("DefaultAnnotationGenerator")
class DefaultAnnotationGeneratorTest extends ValidationFileTest {

    @Test
    @DisplayName("can annotate classes")
    void shouldAnnotateClasses(final ValidationFile validationFile) {
        // given
        final var config = createConfig(ANNOTATION_API);
        final var generator = new DefaultAnnotationGenerator(config, testTranslator());

        // when
        final var annotations = generator.generatedClass();

        // then
        annotations.forEach(annotation -> validate(annotation, validationFile));
    }

    @Test
    @DisplayName("can annotate fields")
    void shouldAnnotateFields(final ValidationFile validationFile) {
        // given
        final var config = createConfig(ANNOTATION_API);
        final var generator = new DefaultAnnotationGenerator(config, testTranslator());

        // when
        final var annotations = generator.generatedField();

        // then
        annotations.forEach(annotation -> validate(annotation, validationFile));
    }

    @Test
    @DisplayName("can annotate methods")
    void shouldAnnotateMethods(final ValidationFile validationFile) {
        // given
        final var config = createConfig(ANNOTATION_API);
        final var generator = new DefaultAnnotationGenerator(config, testTranslator());

        // when
        final var annotations = generator.generatedMethod();

        // then
        annotations.forEach(annotation -> validate(annotation, validationFile));
    }

    @Test
    @DisplayName("can add date to generated annotation")
    void shouldWriteDate() {
        // given
        final var config = createConfig(ANNOTATION_API, ALL);
        final var generator = new DefaultAnnotationGenerator(config, testTranslator());

        // when
        final var annotations = generator.generatedMethod();

        // then
        annotations.forEach(annotation -> Assertions.assertTrue(annotation.toString().contains("date")));
    }

    @Test
    @DisplayName("can use javax.annotation.processing.Generated")
    void shouldUseProcessingApi(final ValidationFile validationFile) {
        // given
        final var config = createConfig(PROCESSING_API);
        final var generator = new DefaultAnnotationGenerator(config, testTranslator());

        // when
        final var annotations = generator.generatedClass();

        // then
        validate(annotations, validationFile);
    }

    private AnnotationConfiguration createConfig(final AnnotationClassOptions classOptions) {
        return createConfig(classOptions, WITHOUT_DATE);
    }

    private AnnotationConfiguration createConfig(
            final AnnotationClassOptions classOptions,
            final AnnotationMemberOptions memberOptions) {
        return AnnotationConfiguration.builder()
                .setClassAnnotation(classOptions)
                .setFieldAnnotation(classOptions)
                .setMethodAnnotation(classOptions)
                .setClassComment("class")
                .setFieldComment("field")
                .setMethodComment("method")
                .setGeneratorName("test")
                .setClassMembers(memberOptions)
                .setFieldMembers(memberOptions)
                .setMethodMembers(memberOptions)
                .build();
    }

}