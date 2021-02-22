/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.api;

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
