/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.logging;

import org.junit.jupiter.api.DisplayName;
import wtf.metio.yosql.codegen.blocks.BlocksObjectMother;
import wtf.metio.yosql.internals.testing.configs.NamesConfigurations;

@DisplayName("SystemLoggingGenerator")
class SystemLoggingGeneratorTest implements LoggingGeneratorTCK {

    @Override
    public LoggingGenerator generator() {
        return new SystemLoggingGenerator(NamesConfigurations.defaults(), BlocksObjectMother.fields());
    }

    @Override
    public String loggerExpectation() {
        return """
                @javax.annotation.processing.Generated(
                    value = "YoSQL",
                    comments = "DO NOT MODIFY - automatically generated by YoSQL"
                )
                private static final java.lang.System.Logger LOG = java.lang.System.getLogger("com.example.TestRepository");
                """;
    }

    @Override
    public String enteringExpectation() {
        return """
                LOG.log(java.lang.System.Logger.Level.DEBUG, java.lang.String.format("Entering [%s#%s]", "TestRepository", "queryData"));
                """;
    }

    @Override
    public String executingQueryExpectation() {
        return """
                LOG.log(java.lang.System.Logger.Level.INFO, java.lang.String.format("Executing query [%s]", executedQuery));
                """;
    }

    @Override
    public String queryPickedExpectation() {
        return """
                LOG.log(java.lang.System.Logger.Level.DEBUG, java.lang.String.format("Picked query [%s]", "queryData"));
                """;
    }

    @Override
    public String indexPickedExpectation() {
        return """
                LOG.log(java.lang.System.Logger.Level.DEBUG, java.lang.String.format("Picked index [%s]", "queryData"));
                """;
    }

    @Override
    public String shouldLogExpectation() {
        return """
                LOG.isLoggable(java.lang.System.Logger.Level.INFO)""";
    }

    @Override
    public String vendorDetectedExpectation() {
        return """
                LOG.log(java.lang.System.Logger.Level.INFO, java.lang.String.format("Detected database vendor [%s]", "databaseProductName"));
                """;
    }

    @Override
    public String vendorQueryPickedExpectation() {
        return """
                LOG.log(java.lang.System.Logger.Level.DEBUG, java.lang.String.format("Picked query [%s]", "queryData"));
                """;
    }

    @Override
    public String vendorIndexPickedExpectation() {
        return """
                LOG.log(java.lang.System.Logger.Level.DEBUG, java.lang.String.format("Picked index [%s]", "queryData"));
                """;
    }

    @Override
    public boolean isEnabledExpectation() {
        return true;
    }

}
