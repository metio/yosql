/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.models.configuration;

/**
 * Enumeration of possible SQL statement types.
 */
public enum SqlStatementType {

    /**
     * Statement reads data from a database.
     */
    READING,

    /**
     * Statement writes data to a database.
     */
    WRITING,

    /**
     * Statement calls a (stored) procedure in a database.
     */
    CALLING,

}
