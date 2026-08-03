/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.logging;

import org.junit.jupiter.api.DisplayName;

@DisplayName("TinylogLoggingGenerator")
class TinylogLoggingGeneratorTest implements LoggingGeneratorTCK {

    @Override
    public LoggingGenerator generator() {
        return new TinylogLoggingGenerator();
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
