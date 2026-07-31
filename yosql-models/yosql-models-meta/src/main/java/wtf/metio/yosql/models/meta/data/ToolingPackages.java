/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.meta.data;

/**
 * Definition of base packages for the various tools.
 */
public interface ToolingPackages {

    /**
     * Base package used by Ant.
     */
    String ANT_PACKAGE = "wtf.metio.yosql.tooling.ant";

    /**
     * Base package used by CLI.
     */
    String CLI_PACKAGE = "wtf.metio.yosql.tooling.cli";

    /**
     * Base package used by Gradle.
     */
    String GRADLE_PACKAGE = "wtf.metio.yosql.tooling.gradle";

    /**
     * Base package used by Immutables.
     */
    String IMMUTABLES_PACKAGE = "wtf.metio.yosql.models.immutables";

    /**
     * Base package used by Maven.
     */
    String MAVEN_PACKAGE = "wtf.metio.yosql.tooling.maven";

}
