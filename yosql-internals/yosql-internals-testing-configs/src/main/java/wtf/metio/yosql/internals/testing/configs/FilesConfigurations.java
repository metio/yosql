/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
