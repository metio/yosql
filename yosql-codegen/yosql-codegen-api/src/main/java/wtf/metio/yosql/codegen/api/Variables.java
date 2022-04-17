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

    /**
     * Generates an inline variable without an initializer.
     *
     * @param variableClass The class of the variable.
     * @param name          The name of the variable.
     * @return The corresponding code block for the new variable.
     */
    CodeBlock variable(Class<?> variableClass, String name);

    /**
     * Generates an inline variable with an initializer.
     *
     * @param variableClass The class of the variable.
     * @param name          The name of the variable.
     * @param initializer   The initializer to use.
     * @return The corresponding code block for the new variable.
     */
    CodeBlock variable(Class<?> variableClass, String name, CodeBlock initializer);

    /**
     * Generates an inline variable with an initializer.
     *
     * @param variableType The type of the variable.
     * @param name         The name of the variable.
     * @param initializer  The initializer to use.
     * @return The corresponding code block for the new variable.
     */
    CodeBlock variable(TypeName variableType, String name, CodeBlock initializer);

    /**
     * Generates a variable statement with an initializer.
     *
     * @param variableClass The class of the variable.
     * @param name          The name of the variable.
     * @param initializer   The initializer to use.
     * @return The corresponding code block for the new variable.
     */
    CodeBlock variableStatement(Class<?> variableClass, String name, CodeBlock initializer);

    /**
     * Generates a variable statement with an initializer.
     *
     * @param variableType The type of the variable.
     * @param name         The name of the variable.
     * @param initializer  The initializer to use.
     * @return The corresponding code block for the new variable.
     */
    CodeBlock variableStatement(TypeName variableType, String name, CodeBlock initializer);

    /**
     * Generates a variable statement with an initializer.
     *
     * @param variableClass   The class of the variable.
     * @param name            The name of the variable.
     * @param initializer     The initializer to use.
     * @param initializerArgs The initializer arguments to use.
     * @return The corresponding code block for the new variable.
     */
    CodeBlock variable(Class<?> variableClass, String name, String initializer, Object... initializerArgs);

    /**
     * Generates a variable statement with an initializer.
     *
     * @param variableType    The type of the variable.
     * @param name            The name of the variable.
     * @param initializer     The initializer to use.
     * @param initializerArgs The initializer arguments to use.
     * @return The corresponding code block for the new variable.
     */
    CodeBlock variable(TypeName variableType, String name, String initializer, Object... initializerArgs);

}
