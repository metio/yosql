/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.api;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import java.lang.reflect.Type;

public interface Fields {

    FieldSpec field(Type type, String name);
    FieldSpec field(TypeName type, String name);

    FieldSpec.Builder prepareConstant(Type type, String name);
    FieldSpec.Builder prepareConstant(TypeName type, String name);

    FieldSpec privateField(TypeName type, String name);

}
