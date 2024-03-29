/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
