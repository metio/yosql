package wtf.metio.yosql.generator.api;

import com.squareup.javapoet.AnnotationSpec;

/**
 * Generates annotations for classes, fields, and methods.
 */
public interface AnnotationGenerator {

    /**
     * @return Generated annotations for a single class.
     */
    Iterable<AnnotationSpec> generatedClass();

    /**
     * @return Generated annotations for a single field.
     */
    Iterable<AnnotationSpec> generatedField();

    /**
     * @return Generated annotations for a single method.
     */
    Iterable<AnnotationSpec> generatedMethod();

}
