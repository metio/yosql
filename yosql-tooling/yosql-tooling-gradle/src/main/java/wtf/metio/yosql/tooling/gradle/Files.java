/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;

/**
 * Configures input- and output-directories as well as other file related options.
 */
public abstract class Files {

    /**
     * @return The base path of the input directory.
     */
    @InputDirectory
    abstract public RegularFileProperty inputBaseDirectory();

    /**
     * @return The base path of the output directory.
     */
    @OutputDirectory
    abstract public RegularFileProperty outputBaseDirectory();

}
