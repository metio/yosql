/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.logging;

import org.junit.jupiter.api.DisplayName;

@DisplayName("NoOpLoggingGenerator")
class NoOpLoggingGeneratorTest implements LoggingGeneratorTCK {

    @Override
    public LoggingGenerator generator() {
        return new NoOpLoggingGenerator();
    }

    @Override
    public String loggerExpectation() {
        return "";
    }

    @Override
    public String enteringExpectation() {
        return "";
    }

    @Override
    public String executingQueryExpectation() {
        return "";
    }

    @Override
    public String queryPickedExpectation() {
        return "";
    }

    @Override
    public String indexPickedExpectation() {
        return "";
    }

    @Override
    public String shouldLogExpectation() {
        return "";
    }

    @Override
    public String vendorDetectedExpectation() {
        return "";
    }

    @Override
    public String vendorQueryPickedExpectation() {
        return "";
    }

    @Override
    public String vendorIndexPickedExpectation() {
        return "";
    }

    @Override
    public boolean isEnabledExpectation() {
        return false;
    }

}
