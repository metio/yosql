/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;

import javax.inject.Inject;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import de.xn__ho_hia.yosql.model.ExecutionErrors;

/**
 * Writes a single {@link TypeSpec type} into a directory.
 */
public class TypeWriter {

    private final ExecutionErrors errors;
    private final PrintStream     out;

    /**
     * @param errors
     *            The errors to use.
     * @param out
     *            The output to use for log messages.
     */
    @Inject
    public TypeWriter(
            final ExecutionErrors errors,
            final PrintStream out) {
        this.errors = errors;
        this.out = out;
    }

    /**
     * @param baseDirectory
     *            The base directory to use.
     * @param packageName
     *            The package name to use.
     * @param typeSpec
     *            The type specification to write.
     */
    public void writeType(
            final Path baseDirectory,
            final String packageName,
            final TypeSpec typeSpec) {
        try {
            JavaFile.builder(packageName, typeSpec).build().writeTo(baseDirectory);
            out.println(String.format("Generated [%s.%s]", packageName, typeSpec.name)); //$NON-NLS-1$
        } catch (final IOException exception) {
            errors.add(exception);
            out.println(String.format("Could not write [%s.%s] into [%s]", //$NON-NLS-1$
                    packageName, typeSpec.name, baseDirectory));
        }
    }

}
