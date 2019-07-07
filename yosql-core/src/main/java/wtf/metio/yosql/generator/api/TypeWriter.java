/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.api;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import wtf.metio.yosql.model.annotations.Writer;
import wtf.metio.yosql.model.ApplicationEvents;
import wtf.metio.yosql.model.ExecutionConfiguration;
import wtf.metio.yosql.model.ExecutionErrors;
import wtf.metio.yosql.model.PackageTypeSpec;
import org.slf4j.cal10n.LocLogger;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Writes a single {@link TypeSpec type} into a directory.
 */
public final class TypeWriter {

    private final ExecutionErrors        errors;
    private final ExecutionConfiguration config;
    private final LocLogger              logger;

    /**
     * @param config
     *            The configuration to use.
     * @param errors
     *            The errors to use.
     * @param logger
     *            The logger to use.
     */
    @Inject
    public TypeWriter(
            final ExecutionConfiguration config,
            final ExecutionErrors errors,
            final @Writer LocLogger logger) {
        this.config = config;
        this.errors = errors;
        this.logger = logger;
    }

    /**
     * Writes the given type specification into the {@link ExecutionConfiguration#outputBaseDirectory() configured
     * output directory}.
     *
     * @param typeSpec
     *            The type specification to write.
     */
    public void writeType(final PackageTypeSpec typeSpec) {
        try {
            JavaFile.builder(typeSpec.getPackageName(), typeSpec.getType())
                    .build()
                    .writeTo(config.outputBaseDirectory());
            logger.debug(ApplicationEvents.FILE_WRITE_FINISHED,
                    config.outputBaseDirectory(),
                    typeSpec.getPackageName().replace(".", "/"),
                    typeSpec.getType().name);
        } catch (final IOException exception) {
            errors.add(exception);
            logger.error(ApplicationEvents.FILE_WRITE_FAILED,
                    typeSpec.getPackageName(),
                    typeSpec.getType().name,
                    config.outputBaseDirectory());
        }
    }

}
