/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.api;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

/**
 * Controls the generation of variables. Handles the usage of keywords like 'final' or 'var'.
 */
public interface Variables {

    // TODO: change method signatures so that types are first, then variable names, followed by initializers

    /**
     * Generates an inline variable without an initializer.
     *
     * @param name The name of the variable.
     * @param variableClass The class of the variable.
     * @return The corresponding code block for the new variable.
     */
    CodeBlock variable(String name, Class<?> variableClass);

    /**
     * Generates an inline variable with an initializer.
     *
     * @param name The name of the variable.
     * @param variableClass The class of the variable.
     * @param initializer The initializer to use.
     * @return The corresponding code block for the new variable.
     */
    CodeBlock variable(String name, Class<?> variableClass, CodeBlock initializer);

    /**
     * Generates an inline variable with an initializer.
     *
     * @param name The name of the variable.
     * @param variableClass The class of the variable.
     * @param initializer The initializer to use.
     * @return The corresponding code block for the new variable.
     */
    CodeBlock variable(String name, TypeName variableClass, CodeBlock initializer);

    /**
     * Generates a variable statement with an initializer.
     *
     * @param name The name of the variable.
     * @param variableClass The class of the variable.
     * @param initializer The initializer to use.
     * @return The corresponding code block for the new variable.
     */
    CodeBlock variableStatement(String name, Class<?> variableClass, CodeBlock initializer);

    /**
     * Generates a variable statement with an initializer.
     *
     * @param name The name of the variable.
     * @param variableClass The class of the variable.
     * @param initializer The initializer to use.
     * @return The corresponding code block for the new variable.
     */
    CodeBlock variableStatement(String name, TypeName variableClass, CodeBlock initializer);

    CodeBlock variable(String name, Class<?> variableClass, String initializer, Object... initializerArgs);
    CodeBlock variable(String name, TypeName variableClass, String initializer, Object... initializerArgs);

}
