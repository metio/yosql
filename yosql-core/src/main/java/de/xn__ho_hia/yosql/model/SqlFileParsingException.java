/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.model;

/**
 * Signals that something went wrong during SQL file parsing.
 */
public class SqlFileParsingException extends RuntimeException {

    private static final long serialVersionUID = 4967281564674584404L;

    /**
     * @param message
     *            The message to send.
     */
    public SqlFileParsingException(final String message) {
        super(message);
    }

}
