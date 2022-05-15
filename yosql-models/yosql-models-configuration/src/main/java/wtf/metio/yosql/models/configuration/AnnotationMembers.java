/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.configuration;

/**
 * Options for members of the "@Generated" annotation.
 */
public enum AnnotationMembers {

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
