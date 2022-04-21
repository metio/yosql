/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import wtf.metio.yosql.codegen.api.DelegatingMethodsGenerator;

@DisplayName("JDBC DelegatingMethodsGenerator")
class JdbcGenericMethodsGeneratorTest { // TODO: create TCK

    @Nested
    @DisplayName("using default configuration")
    class Defaults {

        private DelegatingMethodsGenerator generator;

        @BeforeEach
        void setUp() {
            generator = JdbcObjectMother.delegatingMethodsGenerator();
        }

    }

}