/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.spring.jdbc;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import wtf.metio.yosql.codegen.api.BlockingMethodGenerator;
import wtf.metio.yosql.codegen.tck.BlockingMethodGeneratorTCK;
import wtf.metio.yosql.testing.configs.JavaConfigurations;

@DisplayName("SpringJdbcBlockingMethodGenerator")
@Disabled
class SpringJdbcBlockingMethodGeneratorTest {

    @Nested
    @DisplayName("using default configuration")
    class Defaults implements BlockingMethodGeneratorTCK {

        @Override
        public BlockingMethodGenerator generator() {
            return SpringJdbcObjectMother.blockingMethodGenerator(JavaConfigurations.defaults());
        }

        @Override
        public String blockingReadOneMethodExpectation() {
            return """
                    
                    """;
        }

        @Override
        public String blockingReadFirstMethodExpectation() {
            return """
                    
                    """;
        }

        @Override
        public String blockingReadListMethodExpectation() {
            return """
                    
                    """;
        }

        @Override
        public String blockingWriteMethodExpectation() {
            return """
                    
                    """;
        }

        @Override
        public String blockingCallMethodExpectation() {
            return """
                    
                    """;
        }

    }

}