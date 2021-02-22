/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.r2dbc;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.codegen.api.ConstructorGenerator;
import wtf.metio.yosql.codegen.api.Methods;
import wtf.metio.yosql.codegen.blocks.GenericBlocks;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

public final class R2dbcConstructorGenerator implements ConstructorGenerator {

    private final GenericBlocks blocks;
    private final Methods methods;

    public R2dbcConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods) {
        this.blocks = blocks;
        this.methods = methods;
    }

    @Override
    public MethodSpec forRepository(final List<SqlStatement> statements) {
        return null;
    }
}
