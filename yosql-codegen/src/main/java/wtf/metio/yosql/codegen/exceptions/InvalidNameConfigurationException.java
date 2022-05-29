/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the name configuration is invalid.
 */
public final class InvalidNameConfigurationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8418727559429742655L;

    /**
     * @param message The message to send.
     */
    public InvalidNameConfigurationException(final String message) {
        super(message);
    }

}
