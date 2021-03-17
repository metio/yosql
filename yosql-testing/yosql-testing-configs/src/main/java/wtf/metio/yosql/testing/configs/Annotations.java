/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.testing.configs;

import wtf.metio.yosql.models.constants.configuration.AnnotationClass;
import wtf.metio.yosql.models.immutables.AnnotationsConfiguration;

public final class Annotations {

    public static AnnotationsConfiguration defaults() {
        return AnnotationsConfiguration.usingDefaults()
                .setAnnotateClasses(true)
                .setAnnotateFields(true)
                .setAnnotateMethods(true)
                .build();
    }

    public static AnnotationsConfiguration processingApi() {
        return AnnotationsConfiguration.usingDefaults()
                .setApi(AnnotationClass.PROCESSING_API)
                .setAnnotateClasses(true)
                .setAnnotateFields(true)
                .setAnnotateMethods(true)
                .setClassAnnotation(AnnotationClass.PROCESSING_API)
                .setFieldAnnotation(AnnotationClass.PROCESSING_API)
                .setMethodAnnotation(AnnotationClass.PROCESSING_API)
                .build();
    }

    public static AnnotationsConfiguration annotationApi() {
        return AnnotationsConfiguration.usingDefaults()
                .setApi(AnnotationClass.ANNOTATION_API)
                .setAnnotateClasses(true)
                .setAnnotateFields(true)
                .setAnnotateMethods(true)
                .setClassAnnotation(AnnotationClass.ANNOTATION_API)
                .setFieldAnnotation(AnnotationClass.ANNOTATION_API)
                .setMethodAnnotation(AnnotationClass.ANNOTATION_API)
                .build();
    }

    private Annotations() {
        // factory class
    }

}
