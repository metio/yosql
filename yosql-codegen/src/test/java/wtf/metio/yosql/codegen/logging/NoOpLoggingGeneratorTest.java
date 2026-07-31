/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
