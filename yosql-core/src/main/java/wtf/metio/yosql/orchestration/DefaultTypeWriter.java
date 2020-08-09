/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.orchestration;

import com.squareup.javapoet.JavaFile;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.model.configuration.FileConfiguration;
import wtf.metio.yosql.model.errors.ExecutionErrors;
import wtf.metio.yosql.model.internal.ApplicationEvents;
import wtf.metio.yosql.model.sql.PackageTypeSpec;

import java.io.IOException;

final class DefaultTypeWriter implements TypeWriter {

    private final LocLogger logger;
    private final FileConfiguration fileConfiguration;
    private final ExecutionErrors errors;

    DefaultTypeWriter(
            final LocLogger logger,
            final FileConfiguration fileConfiguration,
            final ExecutionErrors errors) {
        this.logger = logger;
        this.fileConfiguration = fileConfiguration;
        this.errors = errors;
    }

    @Override
    public void writeType(final PackageTypeSpec typeSpec) {
        try {
            JavaFile.builder(typeSpec.getPackageName(), typeSpec.getType())
                    .build()
                    .writeTo(fileConfiguration.outputBaseDirectory());
            logger.debug(ApplicationEvents.FILE_WRITE_FINISHED,
                    fileConfiguration.outputBaseDirectory(),
                    typeSpec.getPackageName().replace(".", "/"),
                    typeSpec.getType().name);
        } catch (final IOException exception) {
            // TODO: use some factory of 'errors'
            errors.add(exception);
            logger.error(ApplicationEvents.FILE_WRITE_FAILED,
                    typeSpec.getPackageName(),
                    typeSpec.getType().name,
                    fileConfiguration.outputBaseDirectory());
        }
    }

}
