/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.testutils;

/**
 * A validation file is used by test to compare complex {@link String strings} against the expected content of the
 * validation file.
 */
@FunctionalInterface
public interface ValidationFile {

    /**
     * @return The content of this validation file, read with given charset.
     */
    default String read() {
        return read(0);
    }

    /**
     * @return The content of this validation file, read with given charset.
     */
    String read(int number);

}
