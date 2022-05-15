/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import wtf.metio.yosql.testing.configs.JavaConfigurations;

@Disabled
@DisplayName("DefaultJdbcBlocks")
class DefaultJdbcBlocksTest {

    @Nested
    @DisplayName("using default configuration")
    class Defaults extends JdbcBlocksTCK {

        @Override
        public JdbcBlocks generator() {
            return DaoObjectMother.jdbcBlocks(JavaConfigurations.defaults());
        }

        @Override
        String connectionVariableExpectation() {
            return "";
        }

        @Override
        String statementVariableExpectation() {
            return "";
        }

        @Override
        String callableVariableExpectation() {
            return "";
        }

        @Override
        String readMetaDataExpectation() {
            return "";
        }

        @Override
        String resultSetVariableExpectation() {
            return "";
        }

        @Override
        String executeUpdateExpectation() {
            return "";
        }

        @Override
        String executeBatchExpectation() {
            return "";
        }

        @Override
        String closeResultSetExpectation() {
            return "";
        }

        @Override
        String closePrepareStatementExpectation() {
            return "";
        }

        @Override
        String closeConnectionExpectation() {
            return "";
        }

        @Override
        String closeStateExpectation() {
            return "";
        }

        @Override
        String executeStatementExpectation() {
            return "";
        }

        @Override
        String openConnectionExpectation() {
            return "";
        }

        @Override
        String tryPrepareCallableExpectation() {
            return "";
        }

        @Override
        String createStatementExpectation() {
            return "";
        }

        @Override
        String prepareBatchExpectation() {
            return "";
        }

        @Override
        String pickVendorQueryExpectation() {
            return "";
        }

        @Override
        String logExecutedQueryExpectation() {
            return "";
        }

        @Override
        String logExecutedBatchQueryExpectation() {
            return "";
        }

        @Override
        String returnAsMultipleExpectation() {
            return "";
        }

        @Override
        String returnAsSingleExpectation() {
            return "";
        }

        @Override
        String setParametersExpectation() {
            return "";
        }

        @Override
        String setBatchParametersExpectation() {
            return "";
        }

    }

    @Nested
    @DisplayName("using Java 8 configuration")
    class Java8 extends JdbcBlocksTCK {

        @Override
        public JdbcBlocks generator() {
            return DaoObjectMother.jdbcBlocks(JavaConfigurations.java8());
        }

        @Override
        String connectionVariableExpectation() {
            return "";
        }

        @Override
        String statementVariableExpectation() {
            return "";
        }

        @Override
        String callableVariableExpectation() {
            return "";
        }

        @Override
        String readMetaDataExpectation() {
            return "";
        }

        @Override
        String resultSetVariableExpectation() {
            return "";
        }

        @Override
        String executeUpdateExpectation() {
            return "";
        }

        @Override
        String executeBatchExpectation() {
            return "";
        }

        @Override
        String closeResultSetExpectation() {
            return "";
        }

        @Override
        String closePrepareStatementExpectation() {
            return "";
        }

        @Override
        String closeConnectionExpectation() {
            return "";
        }

        @Override
        String closeStateExpectation() {
            return "";
        }

        @Override
        String executeStatementExpectation() {
            return "";
        }

        @Override
        String openConnectionExpectation() {
            return "";
        }

        @Override
        String tryPrepareCallableExpectation() {
            return "";
        }

        @Override
        String createStatementExpectation() {
            return "";
        }

        @Override
        String prepareBatchExpectation() {
            return "";
        }

        @Override
        String pickVendorQueryExpectation() {
            return "";
        }

        @Override
        String logExecutedQueryExpectation() {
            return "";
        }

        @Override
        String logExecutedBatchQueryExpectation() {
            return "";
        }

        @Override
        String returnAsMultipleExpectation() {
            return "";
        }

        @Override
        String returnAsSingleExpectation() {
            return "";
        }

        @Override
        String setParametersExpectation() {
            return "";
        }

        @Override
        String setBatchParametersExpectation() {
            return "";
        }

    }

    @Nested
    @DisplayName("using Java 9 configuration")
    class Java9 extends JdbcBlocksTCK {

        @Override
        public JdbcBlocks generator() {
            return DaoObjectMother.jdbcBlocks(JavaConfigurations.java9());
        }

        @Override
        String connectionVariableExpectation() {
            return "";
        }

        @Override
        String statementVariableExpectation() {
            return "";
        }

        @Override
        String callableVariableExpectation() {
            return "";
        }

        @Override
        String readMetaDataExpectation() {
            return "";
        }

        @Override
        String resultSetVariableExpectation() {
            return "";
        }

        @Override
        String executeUpdateExpectation() {
            return "";
        }

        @Override
        String executeBatchExpectation() {
            return "";
        }

        @Override
        String closeResultSetExpectation() {
            return "";
        }

        @Override
        String closePrepareStatementExpectation() {
            return "";
        }

        @Override
        String closeConnectionExpectation() {
            return "";
        }

        @Override
        String closeStateExpectation() {
            return "";
        }

        @Override
        String executeStatementExpectation() {
            return "";
        }

        @Override
        String openConnectionExpectation() {
            return "";
        }

        @Override
        String tryPrepareCallableExpectation() {
            return "";
        }

        @Override
        String createStatementExpectation() {
            return "";
        }

        @Override
        String prepareBatchExpectation() {
            return "";
        }

        @Override
        String pickVendorQueryExpectation() {
            return "";
        }

        @Override
        String logExecutedQueryExpectation() {
            return "";
        }

        @Override
        String logExecutedBatchQueryExpectation() {
            return "";
        }

        @Override
        String returnAsMultipleExpectation() {
            return "";
        }

        @Override
        String returnAsSingleExpectation() {
            return "";
        }

        @Override
        String setParametersExpectation() {
            return "";
        }

        @Override
        String setBatchParametersExpectation() {
            return "";
        }

    }

    @Nested
    @DisplayName("using Java 11 configuration")
    class Java11 extends JdbcBlocksTCK {

        @Override
        public JdbcBlocks generator() {
            return DaoObjectMother.jdbcBlocks(JavaConfigurations.java11());
        }

        @Override
        String connectionVariableExpectation() {
            return "";
        }

        @Override
        String statementVariableExpectation() {
            return "";
        }

        @Override
        String callableVariableExpectation() {
            return "";
        }

        @Override
        String readMetaDataExpectation() {
            return "";
        }

        @Override
        String resultSetVariableExpectation() {
            return "";
        }

        @Override
        String executeUpdateExpectation() {
            return "";
        }

        @Override
        String executeBatchExpectation() {
            return "";
        }

        @Override
        String closeResultSetExpectation() {
            return "";
        }

        @Override
        String closePrepareStatementExpectation() {
            return "";
        }

        @Override
        String closeConnectionExpectation() {
            return "";
        }

        @Override
        String closeStateExpectation() {
            return "";
        }

        @Override
        String executeStatementExpectation() {
            return "";
        }

        @Override
        String openConnectionExpectation() {
            return "";
        }

        @Override
        String tryPrepareCallableExpectation() {
            return "";
        }

        @Override
        String createStatementExpectation() {
            return "";
        }

        @Override
        String prepareBatchExpectation() {
            return "";
        }

        @Override
        String pickVendorQueryExpectation() {
            return "";
        }

        @Override
        String logExecutedQueryExpectation() {
            return "";
        }

        @Override
        String logExecutedBatchQueryExpectation() {
            return "";
        }

        @Override
        String returnAsMultipleExpectation() {
            return "";
        }

        @Override
        String returnAsSingleExpectation() {
            return "";
        }

        @Override
        String setParametersExpectation() {
            return "";
        }

        @Override
        String setBatchParametersExpectation() {
            return "";
        }

    }

    @Nested
    @DisplayName("using Java 14 configuration")
    class Java14 extends JdbcBlocksTCK {

        @Override
        public JdbcBlocks generator() {
            return DaoObjectMother.jdbcBlocks(JavaConfigurations.java14());
        }

        @Override
        String connectionVariableExpectation() {
            return "";
        }

        @Override
        String statementVariableExpectation() {
            return "";
        }

        @Override
        String callableVariableExpectation() {
            return "";
        }

        @Override
        String readMetaDataExpectation() {
            return "";
        }

        @Override
        String resultSetVariableExpectation() {
            return "";
        }

        @Override
        String executeUpdateExpectation() {
            return "";
        }

        @Override
        String executeBatchExpectation() {
            return "";
        }

        @Override
        String closeResultSetExpectation() {
            return "";
        }

        @Override
        String closePrepareStatementExpectation() {
            return "";
        }

        @Override
        String closeConnectionExpectation() {
            return "";
        }

        @Override
        String closeStateExpectation() {
            return "";
        }

        @Override
        String executeStatementExpectation() {
            return "";
        }

        @Override
        String openConnectionExpectation() {
            return "";
        }

        @Override
        String tryPrepareCallableExpectation() {
            return "";
        }

        @Override
        String createStatementExpectation() {
            return "";
        }

        @Override
        String prepareBatchExpectation() {
            return "";
        }

        @Override
        String pickVendorQueryExpectation() {
            return "";
        }

        @Override
        String logExecutedQueryExpectation() {
            return "";
        }

        @Override
        String logExecutedBatchQueryExpectation() {
            return "";
        }

        @Override
        String returnAsMultipleExpectation() {
            return "";
        }

        @Override
        String returnAsSingleExpectation() {
            return "";
        }

        @Override
        String setParametersExpectation() {
            return "";
        }

        @Override
        String setBatchParametersExpectation() {
            return "";
        }

    }

    @Nested
    @DisplayName("using Java 16 configuration")
    class Java16 extends JdbcBlocksTCK {

        @Override
        public JdbcBlocks generator() {
            return DaoObjectMother.jdbcBlocks(JavaConfigurations.java16());
        }

        @Override
        String connectionVariableExpectation() {
            return "";
        }

        @Override
        String statementVariableExpectation() {
            return "";
        }

        @Override
        String callableVariableExpectation() {
            return "";
        }

        @Override
        String readMetaDataExpectation() {
            return "";
        }

        @Override
        String resultSetVariableExpectation() {
            return "";
        }

        @Override
        String executeUpdateExpectation() {
            return "";
        }

        @Override
        String executeBatchExpectation() {
            return "";
        }

        @Override
        String closeResultSetExpectation() {
            return "";
        }

        @Override
        String closePrepareStatementExpectation() {
            return "";
        }

        @Override
        String closeConnectionExpectation() {
            return "";
        }

        @Override
        String closeStateExpectation() {
            return "";
        }

        @Override
        String executeStatementExpectation() {
            return "";
        }

        @Override
        String openConnectionExpectation() {
            return "";
        }

        @Override
        String tryPrepareCallableExpectation() {
            return "";
        }

        @Override
        String createStatementExpectation() {
            return "";
        }

        @Override
        String prepareBatchExpectation() {
            return "";
        }

        @Override
        String pickVendorQueryExpectation() {
            return "";
        }

        @Override
        String logExecutedQueryExpectation() {
            return "";
        }

        @Override
        String logExecutedBatchQueryExpectation() {
            return "";
        }

        @Override
        String returnAsMultipleExpectation() {
            return "";
        }

        @Override
        String returnAsSingleExpectation() {
            return "";
        }

        @Override
        String setParametersExpectation() {
            return "";
        }

        @Override
        String setBatchParametersExpectation() {
            return "";
        }

    }

}
