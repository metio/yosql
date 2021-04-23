/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.Action;
import org.gradle.api.tasks.Nested;

/**
 * Gradle plugin extension that configures all aspects of YoSQL.
 */
public abstract class YoSqlExtension {

    /**
     * @return The files configuration.
     */
    @Nested
    public abstract Files getFiles();

    /**
     * @param action The config block to apply.
     */
    public void files(Action<? super Files> action) {
        action.execute(getFiles());
    }

}
