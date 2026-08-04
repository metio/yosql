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

    /**
     * Removes what an earlier run wrote and this one did not.
     *
     * <p>The output directory survives a build, so a repository whose {@code .sql} file has been
     * deleted or renamed stays on disk, stays on the compile source root, and keeps compiling and
     * shipping. The build stays green while a fresh checkout — every CI run, every other machine —
     * would not have had that class at all.</p>
     *
     * <p>Only files carrying the {@code @Generated} annotation naming YoSQL, so a file somebody put
     * in the output directory themselves is left alone whatever that directory is pointed at.</p>
     */
    void removeStaleOutput();

}
