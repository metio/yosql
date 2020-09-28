/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import wtf.metio.yosql.generator.blocks.api.Classes;

import javax.lang.model.element.Modifier;

final class DefaultClasses implements Classes {

    @Override
    public TypeSpec.Builder publicClass(final ClassName name) {
        // TODO: configure modifiers?
        return TypeSpec.classBuilder(name).addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    @Override
    public TypeSpec.Builder openClass(final ClassName name) {
        // TODO: configure modifiers?
        return TypeSpec.classBuilder(name).addModifiers(Modifier.PUBLIC);
    }

}
