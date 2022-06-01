/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.configuration;

/**
 * Enumeration of possible return modes.
 */
public enum ReturningMode { // TODO: rename to 'returning' in .sql files

    /**
     * Statement returns no data.
     */
    NONE,

    /**
     * Statement returns 0..1 results.
     */
    SINGLE,

    /**
     * Statement returns 0..n results.
     */
    MULTIPLE,

    /**
     * Statement lazy returns 0..n results.
     */
    CURSOR;

}
