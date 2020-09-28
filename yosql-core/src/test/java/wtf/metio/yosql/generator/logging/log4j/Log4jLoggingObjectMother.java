/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.logging.log4j;

import wtf.metio.yosql.generator.api.LoggingGenerator;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.fields;
import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.names;

public final class Log4jLoggingObjectMother {

    public static LoggingGenerator log4jLoggingGenerator() {
        return new Log4jLoggingGenerator(names(), fields());
    }

    private Log4jLoggingObjectMother() {
        // factory class
    }

}
