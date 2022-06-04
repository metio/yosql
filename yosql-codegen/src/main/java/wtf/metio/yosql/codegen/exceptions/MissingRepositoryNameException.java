/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the name of a repository was not configured
 */
public final class MissingRepositoryNameException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 9036155498417209248L;

}
