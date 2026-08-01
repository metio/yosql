/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.orchestration;

import wtf.metio.yosql.models.immutables.PackagedTypeSpec;

/**
 * Writes a single {@link com.palantir.javapoet.TypeSpec type} into a directory.
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
