package com.github.sebhoss.yosql.generator;

import java.io.IOException;
import java.nio.file.Path;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.plugin.AbstractComponent;
import com.github.sebhoss.yosql.plugin.PluginErrors;
import com.github.sebhoss.yosql.plugin.PluginRuntimeConfig;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

@Named
@Singleton
public class TypeWriter extends AbstractComponent {

    @Inject
    public TypeWriter(
            final PluginErrors pluginErrors,
            final PluginRuntimeConfig runtimeConfig) {
        super(runtimeConfig, pluginErrors);
    }

    public void writeType(
            final Path baseDirectory,
            final String packageName,
            final TypeSpec typeSpec) {
        try {
            JavaFile.builder(packageName, typeSpec).build().writeTo(baseDirectory);
            log().info(String.format("Generated [%s.%s]", packageName, typeSpec.name));
        } catch (final IOException exception) {
            addError(exception);
            log().error(String.format("Could not write [%s.%s] into [%s]",
                    packageName, typeSpec.name, baseDirectory));
        }
    }

}
