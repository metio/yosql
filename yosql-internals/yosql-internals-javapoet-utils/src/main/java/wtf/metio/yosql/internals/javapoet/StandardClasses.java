/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.javapoet;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

public final class StandardClasses {
    
    private StandardClasses() {
        // factory class
    }

    public static TypeSpec.Builder openClass(final ClassName name) {
        return TypeSpec.classBuilder(name).addModifiers(Modifier.PUBLIC);
    }

    public static TypeSpec.Builder abstractClass(final ClassName name) {
        return TypeSpec.classBuilder(name).addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
    }

    public static TypeSpec.Builder closedClass(final ClassName name) {
        return TypeSpec.classBuilder(name).addModifiers(Modifier.PUBLIC).addModifiers(Modifier.FINAL);
    }

}
