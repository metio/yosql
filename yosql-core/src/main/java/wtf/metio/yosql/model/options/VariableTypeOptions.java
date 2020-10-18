/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.options;

/**
 * Options that control how the type of a variable should be declared.
 */
public enum VariableTypeOptions {

    /**
     * Generate variable declarations using the type of the variable (<code>int x = 123</code>).
     */
    TYPE,

    /**
     * Generate variable declarations using the 'var' keyword introduced in Java 11 (<code>var x = 123</code>).
     *
     * @since Java 11
     */
    VAR

}
