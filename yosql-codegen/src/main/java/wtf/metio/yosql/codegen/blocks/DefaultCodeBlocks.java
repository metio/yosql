/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.codegen.exceptions.MissingConverterAliasException;
import wtf.metio.yosql.codegen.exceptions.MissingConverterTypeNameException;
import wtf.metio.yosql.models.configuration.ResultRowConverter;

public final class DefaultCodeBlocks implements CodeBlocks {

    @Override
    public CodeBlock returnTrue() {
        return CodeBlock.builder().addStatement("return $L", true).build();
    }

    @Override
    public CodeBlock returnFalse() {
        return CodeBlock.builder().addStatement("return $L", false).build();
    }

    @Override
    public CodeBlock returnValue(final CodeBlock value) {
        return CodeBlock.builder()
                .addStatement("return $L", value)
                .build();
    }

    @Override
    public CodeBlock close(final String resource) {
        return CodeBlock.builder().addStatement("$N.close()", resource).build();
    }

    @Override
    public CodeBlock initializeFieldToSelf(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("this.$N = $N", fieldName, fieldName)
                .build();
    }

    @Override
    public CodeBlock initializeConverter(final ResultRowConverter converter) {
        return CodeBlock.builder()
                .addStatement("this.$N = new $T()",
                        converter.alias().orElseThrow(MissingConverterAliasException::new),
                        converter.converterTypeName().orElseThrow(MissingConverterTypeNameException::new))
                .build();
    }

}
