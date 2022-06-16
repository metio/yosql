/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import java.util.List;
import java.util.stream.Collectors;

public final class Runtime extends AbstractConfigurationGroup {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(Runtime.class.getSimpleName())
                .setDescription("Wrapper around all existing configs")
                .addAllSettings(all())
                .addImmutableAnnotations(immutableAnnotation())
                .addImmutableMethods(immutableBuilder(Runtime.class.getSimpleName()))
                .addImmutableMethods(immutableCopyOf(Runtime.class.getSimpleName()))
                .build();
    }

    private static List<ConfigurationSetting> all() {
        return AllConfigurations.allConfigurationGroups()
                .map(Runtime::single)
                .collect(Collectors.toList());
    }

    private static ConfigurationSetting single(final ConfigurationGroup group) {
        final var returnType = immutableType(configurationName(group.name()));
        final var name = Strings.lowerCase(group.name());
        final var description = group.description();
        return ConfigurationSetting.builder()
                .setName(name)
                .setDescription(description)
                .addImmutableMethods(immutableMethod(returnType, name, description, CodeBlock.of("$T.builder().build()", returnType)))
                .build();
    }

    private Runtime() {
        // data class
    }

}
