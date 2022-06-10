/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.models.configuration;

/**
 * Options for the logging API used in the generated code.
 */
public enum GeneratedAnnotationApis {

    /**
     * Uses the javax.annotation API.
     */
    ANNOTATION_API("javax.annotation.Generated"),

    /**
     * Uses the javax.annotation.processing API.
     */
    PROCESSING_API("javax.annotation.processing.Generated");

    public final String annotationClass;

    GeneratedAnnotationApis(String annotationClass) {
        this.annotationClass = annotationClass;
    }

}
