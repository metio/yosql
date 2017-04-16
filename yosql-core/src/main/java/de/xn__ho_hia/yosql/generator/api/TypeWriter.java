/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.api;

import java.io.IOException;
import java.io.PrintStream;

import javax.inject.Inject;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.PackageTypeSpec;

/**
 * Writes a single {@link TypeSpec type} into a directory.
 */
public class TypeWriter {

    private final ExecutionErrors        errors;
    private final PrintStream            out;
    private final ExecutionConfiguration config;

    /**
     * @param config
     *            The configuration to use.
     * @param errors
     *            The errors to use.
     * @param out
     *            The output to use for log messages.
     */
    @Inject
    public TypeWriter(
            final ExecutionConfiguration config,
            final ExecutionErrors errors,
            final PrintStream out) {
        this.config = config;
        this.errors = errors;
        this.out = out;
    }

    /**
     * @param typeSpec
     *            The type specification to write.
     */
    @SuppressWarnings("nls")
    public void writeType(final PackageTypeSpec typeSpec) {
        try {
            JavaFile.builder(typeSpec.getPackageName(), typeSpec.getType())
                    .build()
                    .writeTo(config.outputBaseDirectory());
            if (out != null) {
                out.println(String.format("Wrote [%s/%s/%s]",
                        config.outputBaseDirectory(),
                        typeSpec.getPackageName().replace(".", "/"),
                        typeSpec.getType().name.replace(".", "/") + ".java"));
            }
        } catch (final IOException exception) {
            errors.add(exception);
            out.println(String.format("Could not write [%s.%s] into [%s]", //$NON-NLS-1$
                    typeSpec.getPackageName(), typeSpec.getType().name, config.outputBaseDirectory()));
        }
    }

}
