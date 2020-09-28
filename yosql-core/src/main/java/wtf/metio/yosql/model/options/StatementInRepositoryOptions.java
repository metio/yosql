/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.options;

/**
 * Enumeration of all the possible ways a SQL statement can be written into a repository.
 */
public enum StatementInRepositoryOptions {

    /**
     * SQL statements are copied into repositories using string concatenation.
     */
    INLINE_CONCAT,

    /**
     * SQL statements are copied into repositories using text blocks.
     *
     * @since Java 15
     */
    INLINE_TEXT_BLOCK,

}
