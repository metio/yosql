/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.logging;

import wtf.metio.yosql.generator.api.LoggingGenerator;
import wtf.metio.yosql.tests.ObjectMother;

public final class LoggingObjectMother extends ObjectMother {

    public static LoggingGenerator loggingGenerator() {
        return yoSqlComponent().loggingGenerator();
    }

    private LoggingObjectMother() {
        // factory class
    }

}
