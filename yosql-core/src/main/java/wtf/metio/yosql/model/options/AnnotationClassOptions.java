/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.options;

/**
 * Options for the "@Generated" annotation class.
 */
public enum AnnotationClassOptions {

    /**
     * Option that disables adding a new annotation
     */
    NONE("please.report.this.Bug"),

    /**
     * Uses the "javax.annotation.Generated" annotation
     */
    ANNOTATION_API("javax.annotation.Generated"),

    /**
     * Uses the "javax.annotation.processing.Generated" annotation
     */
    PROCESSING_API("javax.annotation.processing.Generated");

    public final String annotationClass;

    AnnotationClassOptions(String annotationClass) {
        this.annotationClass = annotationClass;
    }

}
