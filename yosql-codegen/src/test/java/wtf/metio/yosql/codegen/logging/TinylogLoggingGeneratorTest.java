/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.logging;

import org.junit.jupiter.api.DisplayName;
import wtf.metio.yosql.testing.configs.NamesConfigurations;

@DisplayName("TinylogLoggingGenerator")
class TinylogLoggingGeneratorTest implements LoggingGeneratorTCK {

    @Override
    public LoggingGenerator generator() {
        return new TinylogLoggingGenerator(NamesConfigurations.defaults());
    }

    @Override
    public String loggerExpectation() {
        return "";
    }

    @Override
    public String enteringExpectation() {
        return """
                org.tinylog.Logger.debug(() -> java.lang.String.format("Entering [%s#%s]", "TestRepository", "queryData"));
                """;
    }

    @Override
    public String executingQueryExpectation() {
        return """
                org.tinylog.Logger.info(() -> java.lang.String.format("Executing query [%s]", executedQuery));
                """;
    }

    @Override
    public String queryPickedExpectation() {
        return """
                org.tinylog.Logger.debug(() -> java.lang.String.format("Picked query [%s]", "queryData"));
                """;
    }

    @Override
    public String indexPickedExpectation() {
        return """
                org.tinylog.Logger.debug(() -> java.lang.String.format("Picked index [%s]", "queryData"));
                """;
    }

    @Override
    public String shouldLogExpectation() {
        return "org.tinylog.Logger.isInfoEnabled()";
    }

    @Override
    public String vendorDetectedExpectation() {
        return """
                org.tinylog.Logger.info(() -> java.lang.String.format("Detected database vendor [%s]", databaseProductName));
                """;
    }

    @Override
    public String vendorQueryPickedExpectation() {
        return """
                org.tinylog.Logger.debug(() -> java.lang.String.format("Picked query [%s]", "queryData"));
                """;
    }

    @Override
    public String vendorIndexPickedExpectation() {
        return """
                org.tinylog.Logger.debug(() -> java.lang.String.format("Picked index [%s]", "queryData"));
                """;
    }

    @Override
    public boolean isEnabledExpectation() {
        return true;
    }

}
