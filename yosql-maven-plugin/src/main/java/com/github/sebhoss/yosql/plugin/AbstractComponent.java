package com.github.sebhoss.yosql.plugin;

import org.apache.maven.plugin.logging.Log;

public abstract class AbstractComponent {

    protected final PluginConfig runtimeConfig;
    protected final PluginErrors        pluginErrors;

    protected AbstractComponent(
            final PluginConfig runtimeConfig,
            final PluginErrors pluginErrors) {
        this.runtimeConfig = runtimeConfig;
        this.pluginErrors = pluginErrors;
    }

    protected Log log() {
        return runtimeConfig.getLogger();
    }

    protected void addError(final Throwable throwable) {
        pluginErrors.add(throwable);
    }

}
