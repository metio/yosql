/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class Runtime {

    public static ConfigurationGroup configurationGroup(final String immutablesBasePackage) {
        return ConfigurationGroup.builder()
                .setName(Runtime.class.getSimpleName())
                .setDescription("Wrapper around all existing configs")
                .addAllSettings(all(immutablesBasePackage))
                .build();
    }

    private static List<ConfigurationSetting> all(final String immutablesBasePackage) {
        return AllConfigurations.allConfigurationGroups()
                .map(group -> single(group, immutablesBasePackage))
                .collect(Collectors.toList());
    }

    private static ConfigurationSetting single(final ConfigurationGroup group, final String immutablesBasePackage) {
        final var returnType = ClassName.get(immutablesBasePackage, group.configurationName());
        return ConfigurationSetting.builder()
                .setName(lowerCase(group.name()))
                .setDescription(group.description())
                .setType(returnType)
                .setValue(CodeBlock.builder().add("$T.usingDefaults().build()", returnType).build())
                .build();
    }

    private static String lowerCase(final String name) {
        return name.substring(0, 1).toLowerCase(Locale.ROOT) + name.substring(1);
    }

    private Runtime() {
        // data class
    }

}
