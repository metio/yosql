/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.validation;

import wtf.metio.yosql.codegen.files.CodegenPreconditions;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Validates {@link wtf.metio.yosql.models.immutables.FilesConfiguration}s.
 */
public final class FilesConfigurationValidator implements RuntimeConfigurationValidator {

    private final CodegenPreconditions preconditions;

    public FilesConfigurationValidator(final CodegenPreconditions preconditions) {
        this.preconditions = preconditions;
    }

    @Override
    public void validate(final RuntimeConfiguration configuration) {
        final var files = configuration.files();

        preconditions.directoryIsReadable(files.inputBaseDirectory());
        preconditions.directoryIsWriteable(files.outputBaseDirectory());
    }

}
