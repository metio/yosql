/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.helpers;

import com.squareup.javapoet.MethodSpec;

/**
 * Creates typical methods found in the domain of yosql.
 */
public class TypicalMethods {

    /**
     * Prepares a new public method.
     *
     * @param name
     *            The name to use for the new method.
     * @return A method specification builder for a new public method.
     */
    public static MethodSpec.Builder publicMethod(final String name) {
        return MethodSpec.methodBuilder(name)
                .addModifiers(TypicalModifiers.publicMethod());
    }

    /**
     * Prepares a new implementation method.
     *
     * @param name
     *            The name to use for the new method.
     * @return A method specification builder for an implementation method.
     */
    public static MethodSpec.Builder implementation(final String name) {
        return publicMethod(name).addAnnotation(Override.class);
    }

    /**
     * @return A method specification builder for a new constructor.
     */
    public static MethodSpec.Builder constructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(TypicalModifiers.publicConstructor());
    }

}
