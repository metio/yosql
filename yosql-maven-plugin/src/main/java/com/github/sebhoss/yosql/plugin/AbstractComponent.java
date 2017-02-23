/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package com.github.sebhoss.yosql.plugin;

import org.apache.maven.plugin.logging.Log;

public abstract class AbstractComponent {

    protected final PluginConfig pluginConfig;
    protected final PluginErrors pluginErrors;

    protected AbstractComponent(
            final PluginConfig pluginConfig,
            final PluginErrors pluginErrors) {
        this.pluginConfig = pluginConfig;
        this.pluginErrors = pluginErrors;
    }

    protected Log log() {
        return pluginConfig.getLogger();
    }

    protected void addError(final Throwable throwable) {
        pluginErrors.add(throwable);
    }

}
