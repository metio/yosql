package com.github.sebhoss.yosql.generator;

import java.io.IOException;
import java.nio.file.Path;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.PluginErrors;
import com.github.sebhoss.yosql.PluginRuntimeConfig;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

@Named
@Singleton
public class TypeWriter {

    private final PluginErrors        pluginErrors;
    private final PluginRuntimeConfig runtimeConfig;

    @Inject
    public TypeWriter(
            final PluginErrors pluginErrors,
            final PluginRuntimeConfig runtimeConfig) {
        this.pluginErrors = pluginErrors;
        this.runtimeConfig = runtimeConfig;
    }

    public void writeType(
            final Path baseDirectory,
            final String packageName,
            final TypeSpec typeSpec) {
        try {
            JavaFile.builder(packageName, typeSpec).build().writeTo(baseDirectory);
        } catch (final IOException exception) {
            pluginErrors.add(exception);
            runtimeConfig.getLogger().error(String.format("Could not write [%s.%s] into [%s]",
                    packageName, typeSpec.name, baseDirectory));
        }
    }

}
