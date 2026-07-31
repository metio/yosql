/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.configuration;

/**
 * Options for members of the "@Generated" annotation.
 */
public enum GeneratedAnnotationMembers {

    /**
     * Use all available annotation members.
     */
    ALL,

    /**
     * Use no annotation members.
     */
    NONE,

    /**
     * Only use the "value" annotation member.
     */
    VALUE,

    /**
     * Only use the "date" annotation member.
     */
    DATE,

    /**
     * Only use the "comment" annotation member.
     */
    COMMENT,

    /**
     * Use all annotation members except "date".
     */
    WITHOUT_DATE,

}
