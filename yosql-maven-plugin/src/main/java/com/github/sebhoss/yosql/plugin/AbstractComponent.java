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
