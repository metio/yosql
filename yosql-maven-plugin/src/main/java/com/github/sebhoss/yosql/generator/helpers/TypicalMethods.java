/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package com.github.sebhoss.yosql.generator.helpers;

import com.squareup.javapoet.MethodSpec;

@SuppressWarnings({ "javadoc" })
public class TypicalMethods {

    public static MethodSpec.Builder publicMethod(final String name) {
        return MethodSpec.methodBuilder(name)
                .addModifiers(TypicalModifiers.PUBLIC_METHOD);
    }

    public static MethodSpec.Builder implementation(final String name) {
        return publicMethod(name).addAnnotation(Override.class);
    }

    public static MethodSpec.Builder constructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(TypicalModifiers.PUBLIC_CONSTRUCTOR);
    }

}
