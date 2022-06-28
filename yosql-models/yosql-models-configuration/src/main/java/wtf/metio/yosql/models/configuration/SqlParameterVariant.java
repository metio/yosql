/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.configuration;

/**
 * Parameter variants that are mostly used by callable statements.
 */
public enum SqlParameterVariant {

    /**
     * The parameter is used as an input for a statement.
     */
    IN,

    /**
     * The parameter is used to capture an output of a statement.
     */
    OUT,

    /**
     * The parameter is both used as input and output of a statement.
     */
    INOUT,

}
