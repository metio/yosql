/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
