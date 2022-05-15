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
@DisplayName("DefaultReadMethodGenerator")
class DefaultReadMethodGeneratorTest {

    @Nested
    @DisplayName("using default configuration")
    class Defaults implements ReadMethodGeneratorTCK {

        @Override
        public ReadMethodGenerator generator() {
            return DaoObjectMother.readMethodGenerator(JavaConfigurations.defaults());
        }

        @Override
        public String readSingleMethodExpectation() {
            return "";
        }

        @Override
        public String readMultipleMethodExpectation() {
            return "";
        }

        @Override
        public String readCursorMethodExpectation() {
            return "";
        }

        @Override
        public String readNoneMethodExpectation() {
            return "";
        }

    }

    @Nested
    @DisplayName("using Java 8 configuration")
    class Java8 implements ReadMethodGeneratorTCK {

        @Override
        public ReadMethodGenerator generator() {
            return DaoObjectMother.readMethodGenerator(JavaConfigurations.java8());
        }

        @Override
        public String readSingleMethodExpectation() {
            return "";
        }

        @Override
        public String readMultipleMethodExpectation() {
            return "";
        }

        @Override
        public String readCursorMethodExpectation() {
            return "";
        }

        @Override
        public String readNoneMethodExpectation() {
            return "";
        }

    }

    @Nested
    @DisplayName("using Java 9 configuration")
    class Java9 implements ReadMethodGeneratorTCK {

        @Override
        public ReadMethodGenerator generator() {
            return DaoObjectMother.readMethodGenerator(JavaConfigurations.java9());
        }

        @Override
        public String readSingleMethodExpectation() {
            return "";
        }

        @Override
        public String readMultipleMethodExpectation() {
            return "";
        }

        @Override
        public String readCursorMethodExpectation() {
            return "";
        }

        @Override
        public String readNoneMethodExpectation() {
            return "";
        }

    }

    @Nested
    @DisplayName("using Java 11 configuration")
    class Java11 implements ReadMethodGeneratorTCK {

        @Override
        public ReadMethodGenerator generator() {
            return DaoObjectMother.readMethodGenerator(JavaConfigurations.java11());
        }

        @Override
        public String readSingleMethodExpectation() {
            return "";
        }

        @Override
        public String readMultipleMethodExpectation() {
            return "";
        }

        @Override
        public String readCursorMethodExpectation() {
            return "";
        }

        @Override
        public String readNoneMethodExpectation() {
            return "";
        }

    }

    @Nested
    @DisplayName("using Java 14 configuration")
    class Java14 implements ReadMethodGeneratorTCK {

        @Override
        public ReadMethodGenerator generator() {
            return DaoObjectMother.readMethodGenerator(JavaConfigurations.java14());
        }

        @Override
        public String readSingleMethodExpectation() {
            return "";
        }

        @Override
        public String readMultipleMethodExpectation() {
            return "";
        }

        @Override
        public String readCursorMethodExpectation() {
            return "";
        }

        @Override
        public String readNoneMethodExpectation() {
            return "";
        }

    }

    @Nested
    @DisplayName("using Java 16 configuration")
    class Java16 implements ReadMethodGeneratorTCK {

        @Override
        public ReadMethodGenerator generator() {
            return DaoObjectMother.readMethodGenerator(JavaConfigurations.java16());
        }

        @Override
        public String readSingleMethodExpectation() {
            return "";
        }

        @Override
        public String readMultipleMethodExpectation() {
            return "";
        }

        @Override
        public String readCursorMethodExpectation() {
            return "";
        }

        @Override
        public String readNoneMethodExpectation() {
            return "";
        }

    }

}
