/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.testing.configs;

import wtf.metio.yosql.models.immutables.FilesConfiguration;

import java.nio.file.Paths;

/**
 * Object mother for {@link FilesConfiguration}s.
 */
public final class FilesConfigurations {

    public static FilesConfiguration defaults() {
        return FilesConfiguration.builder().build();
    }

    public static FilesConfiguration maven() {
        return FilesConfiguration.copyOf(defaults())
                .withInputBaseDirectory(Paths.get("src", "main", "yosql"))
                .withOutputBaseDirectory(Paths.get("target", "generated-sources", "yosql"));
    }

    private FilesConfigurations() {
        // factory class
    }

}
