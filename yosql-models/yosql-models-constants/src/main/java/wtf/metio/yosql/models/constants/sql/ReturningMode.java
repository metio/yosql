/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.constants.sql;

/**
 * Enumeration of possible return modes.
 */
public enum ReturningMode {

    // TODO: rename to MULTIPLE, SINGLE, NONE
    // TODO: remove ONE

    /**
     * Statement returns no data.
     */
    NONE,

    /**
     * Statement returns the first result, ignores all others in case they exist.
     */
    FIRST,

    /**
     * Statement returns the first result, fails if there are more than one.
     */
    ONE,

    /**
     * Statement returns the entire result set.
     */
    LIST

}
