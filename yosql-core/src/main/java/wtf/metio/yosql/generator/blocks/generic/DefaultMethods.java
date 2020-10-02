/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.blocks.api.Javadoc;
import wtf.metio.yosql.generator.blocks.api.Methods;

final class DefaultMethods implements Methods {

    private final AnnotationGenerator annotations;
    private final Javadoc javadoc;

    DefaultMethods(final AnnotationGenerator annotations, final Javadoc javadoc) {
        this.annotations = annotations;
        this.javadoc = javadoc;
    }

    @Override
    public MethodSpec.Builder publicMethod(final String name) {
        // TODO: implement
        return null;
    }

    @Override
    public MethodSpec.Builder implementation(final String name) {
        // TODO: implement
        return null;
    }

    @Override
    public MethodSpec.Builder constructor() {
        // TODO: implement
        return null;
    }

}
