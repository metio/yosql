/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.api;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

public interface Variables {

    CodeBlock variable(String name, Class<?> variableClass);

    CodeBlock exception(String name, Class<?> variableClass);

    CodeBlock variable(String name, TypeName variableClass, CodeBlock initializer);

    CodeBlock variable(String name, Class<?> variableClass, CodeBlock initializer);

    CodeBlock variableStatement(String name, Class<?> variableClass, CodeBlock initializer);

    CodeBlock variableStatement(String name, TypeName variableClass, CodeBlock initializer);

    CodeBlock variable(String name, Class<?> variableClass, String initializer, Object... initializerArgs);

}
