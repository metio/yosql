/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.sql;

/**
 * Enumeration of possible return modes.
 */
public enum ReturningMode {

    /**
     * Statement returns no data.
     */
    NONE,

    /**
     * Statement returns the first result.
     */
    SINGLE,

    /**
     * Statement returns the first result, fails if there are more than one.
     */
    FIRST,

    /**
     * Statement returns the entire result set.
     */
    LIST

}
