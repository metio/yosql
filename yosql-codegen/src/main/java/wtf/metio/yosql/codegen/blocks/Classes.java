/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;

public interface Classes {

    TypeSpec.Builder publicInterface(ClassName name);

    TypeSpec.Builder publicClass(ClassName name);

    TypeSpec.Builder openClass(ClassName name);

}
