/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.orchestration;

import com.squareup.javapoet.JavaFile;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.errors.ExecutionErrors;
import wtf.metio.yosql.codegen.lifecycle.ApplicationErrors;
import wtf.metio.yosql.codegen.lifecycle.WriteLifecycle;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;

import java.io.IOException;

public final class DefaultTypeWriter implements TypeWriter {

    private final LocLogger logger;
    private final FilesConfiguration fileConfiguration;
    private final ExecutionErrors errors;

    public DefaultTypeWriter(
            final LocLogger logger,
            final FilesConfiguration fileConfiguration,
            final ExecutionErrors errors) {
        this.logger = logger;
        this.fileConfiguration = fileConfiguration;
        this.errors = errors;
    }

    @Override
    public void writeType(final PackagedTypeSpec typeSpec) {
        try {
            JavaFile.builder(typeSpec.getPackageName(), typeSpec.getType())
                    .build()
                    .writeTo(fileConfiguration.outputBaseDirectory());
            logger.debug(WriteLifecycle.FILE_WRITE_FINISHED,
                    fileConfiguration.outputBaseDirectory(),
                    typeSpec.getPackageName().replace(".", "/"),
                    typeSpec.getType().name);
        } catch (final IOException exception) {
            errors.add(exception);
            logger.error(ApplicationErrors.FILE_WRITE_FAILED,
                    typeSpec.getPackageName(),
                    typeSpec.getType().name,
                    fileConfiguration.outputBaseDirectory());
        }
    }

}
