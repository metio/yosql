/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.api;

import java.io.IOException;

import javax.inject.Inject;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.PackageTypeSpec;

/**
 * Writes a single {@link TypeSpec type} into a directory.
 */
public class TypeWriter {

    private static final Logger          LOG = LoggerFactory.getLogger("yosql.codegen.output"); //$NON-NLS-1$

    private final ExecutionErrors        errors;
    private final ExecutionConfiguration config;

    /**
     * @param config
     *            The configuration to use.
     * @param errors
     *            The errors to use.
     */
    @Inject
    public TypeWriter(
            final ExecutionConfiguration config,
            final ExecutionErrors errors) {
        this.config = config;
        this.errors = errors;
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
            LOG.debug("Wrote [{}/{}/{}]", config.outputBaseDirectory(), typeSpec.getPackageName().replace(".", "/"),
                    typeSpec.getType().name.replace(".", "/") + ".java");
        } catch (final IOException exception) {
            errors.add(exception);
            LOG.error("Could not write [{}.{}] into [{}]",
                    typeSpec.getPackageName(), typeSpec.getType().name, config.outputBaseDirectory());
        }
    }

}
