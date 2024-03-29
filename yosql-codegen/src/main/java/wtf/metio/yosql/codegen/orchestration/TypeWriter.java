/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.orchestration;

import wtf.metio.yosql.models.immutables.PackagedTypeSpec;

/**
 * Writes a single {@link com.squareup.javapoet.TypeSpec type} into a directory.
 */
public interface TypeWriter {

    /**
     * Writes the given type specification into the
     * {@link wtf.metio.yosql.models.immutables.FilesConfiguration#outputBaseDirectory() configured output directory}.
     *
     * @param typeSpec The type specification to write.
     */
    void writeType(PackagedTypeSpec typeSpec);

}
