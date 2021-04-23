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
     * @return The JDBC configuration.
     */
    @Nested
    public abstract Jdbc getJdbc();

    /**
     * @return The JDBC configuration.
     */
    @Nested
    public abstract Java getJava();

    /**
     * @return The JDBC configuration.
     */
    @Nested
    public abstract Repositories getRepositories();

    /**
     * @param action The files config to apply.
     */
    public void files(Action<? super Files> action) {
        action.execute(getFiles());
    }

    /**
     * @param action The JDBC config to apply.
     */
    public void jdbc(Action<? super Jdbc> action) {
        action.execute(getJdbc());
    }

    /**
     * @param action The JDBC config to apply.
     */
    public void java(Action<? super Java> action) {
        action.execute(getJava());
    }

    /**
     * @param action The JDBC config to apply.
     */
    public void repositories(Action<? super Repositories> action) {
        action.execute(getRepositories());
    }

}
