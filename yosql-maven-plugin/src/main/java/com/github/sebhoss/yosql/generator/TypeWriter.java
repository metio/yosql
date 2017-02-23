/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package com.github.sebhoss.yosql.generator;

import java.io.IOException;
import java.nio.file.Path;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.plugin.AbstractComponent;
import com.github.sebhoss.yosql.plugin.PluginConfig;
import com.github.sebhoss.yosql.plugin.PluginErrors;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

@Named
@Singleton
@SuppressWarnings({ "nls", "javadoc" })
public class TypeWriter extends AbstractComponent {

    @Inject
    public TypeWriter(
            final PluginErrors pluginErrors,
            final PluginConfig pluginConfig) {
        super(pluginConfig, pluginErrors);
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
