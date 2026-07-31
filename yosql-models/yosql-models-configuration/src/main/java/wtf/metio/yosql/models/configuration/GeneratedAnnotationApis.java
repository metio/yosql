/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
