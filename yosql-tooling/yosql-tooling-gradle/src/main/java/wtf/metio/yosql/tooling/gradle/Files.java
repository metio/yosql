/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputDirectory;
import wtf.metio.yosql.models.immutables.FilesConfiguration;

/**
 * Configures input- and output-directories as well as other file related options.
 */
public abstract class Files {

    /**
     * @return The base path of the input directory.
     */
    @InputDirectory
    public abstract DirectoryProperty getInputBaseDirectory();

    /**
     * @return The base path of the output directory.
     */
    @OutputDirectory
    public abstract DirectoryProperty getOutputBaseDirectory();

    FilesConfiguration asConfiguration() {
        return FilesConfiguration.usingDefaults()
                .setInputBaseDirectory(getInputBaseDirectory().get().getAsFile().toPath())
                .setOutputBaseDirectory(getOutputBaseDirectory().get().getAsFile().toPath())
                .build();
    }

}
