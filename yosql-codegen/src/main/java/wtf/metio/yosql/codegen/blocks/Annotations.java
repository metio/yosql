/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.AnnotationSpec;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

/**
 * Generates annotations for classes, fields, and methods.
 */
public interface Annotations {

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

    /**
     * @return User specified annotations for generated repositories and their interfaces.
     */
    Iterable<AnnotationSpec> generatedRepository();

    /**
     * @return User specified annotations for generated methods.
     */
    Iterable<AnnotationSpec> generatedMethod(SqlConfiguration configuration);

    /**
     * @return User specified annotations for generated constructors.
     */
    Iterable<AnnotationSpec> generatedConstructor();

}
