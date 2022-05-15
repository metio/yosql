/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
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
     * @return The annotations configuration.
     */
    @Nested
    public abstract Annotations getAnnotations();

    /**
     * @return The API configuration.
     */
    @Nested
    public abstract Logging getLogging();

    /**
     * @return The files configuration.
     */
    @Nested
    public abstract Files getFiles();

    /**
     * @return The converter configuration.
     */
    @Nested
    public abstract Converter getConverter();

    /**
     * @return The Java configuration.
     */
    @Nested
    public abstract Java getJava();

    /**
     * @return The repository configuration.
     */
    @Nested
    public abstract Repositories getRepositories();

    /**
     * @return The resources configuration.
     */
    @Nested
    public abstract Resources getResources();

    /**
     * @param action The annotations config to apply.
     */
    public void annotations(Action<? super Annotations> action) {
        action.execute(getAnnotations());
    }

    /**
     * @param action The API config to apply.
     */
    public void api(Action<? super Logging> action) {
        action.execute(getLogging());
    }

    /**
     * @param action The files config to apply.
     */
    public void files(Action<? super Files> action) {
        action.execute(getFiles());
    }

    /**
     * @param action The converter config to apply.
     */
    public void converter(Action<? super Converter> action) {
        action.execute(getConverter());
    }

    /**
     * @param action The Java config to apply.
     */
    public void java(Action<? super Java> action) {
        action.execute(getJava());
    }

    /**
     * @param action The repository config to apply.
     */
    public void repositories(Action<? super Repositories> action) {
        action.execute(getRepositories());
    }

    /**
     * @param action The resources config to apply.
     */
    public void resources(Action<? super Resources> action) {
        action.execute(getResources());
    }

}
