/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.generator.blocks.generic;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.tooling.codegen.model.sql.ResultRowConverter;

public interface GenericBlocks {

    CodeBlock returnTrue();

    CodeBlock returnFalse();

    CodeBlock close(String resource);

    CodeBlock initializeFieldToSelf(String fieldName);

    CodeBlock returnValue(CodeBlock value);

    CodeBlock initializeConverter(ResultRowConverter converter);

}
