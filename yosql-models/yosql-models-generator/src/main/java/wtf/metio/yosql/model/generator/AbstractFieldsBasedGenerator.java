/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.generator;

import com.squareup.javapoet.*;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

import javax.lang.model.element.Modifier;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generator that exposes configuration groups/settings as fields.
 */
public abstract class AbstractFieldsBasedGenerator implements Generator {

    protected final List<FieldSpec> defaultFields(final ConfigurationGroup group, final Modifier... modifiers) {
        return group.settings().stream()
                .filter(setting -> valueOf(setting).isPresent())
                .map(setting -> defaultField(setting, modifiers))
                .collect(Collectors.toList());
    }

    protected final FieldSpec defaultField(final ConfigurationSetting setting, final Modifier... modifiers) {
        return FieldSpec.builder(typeOf(setting), setting.name(), modifiers)
                .addJavadoc(setting.description())
                .addAnnotations(annotationsFor(setting))
                .initializer(defaultValue(valueOf(setting).orElseThrow(), typeOf(setting))).build();
    }

    protected final List<FieldSpec> optionalFields(final ConfigurationGroup group, final Modifier... modifiers) {
        return group.settings().stream()
                .filter(setting -> valueOf(setting).isEmpty())
                .map(setting -> optionalField(setting, modifiers))
                .collect(Collectors.toList());
    }

    protected final FieldSpec optionalField(final ConfigurationSetting setting, final Modifier... modifiers) {
        return FieldSpec.builder(typeOf(setting), setting.name(), modifiers)
                .addJavadoc(setting.description())
                .addAnnotations(annotationsFor(setting))
                .build();
    }

    protected MethodSpec asConfiguration(final ConfigurationGroup group, final String immutablesBasePackage) {
        final var returnType = ClassName.get(immutablesBasePackage, group.configurationName());
        final var builder = MethodSpec.methodBuilder("asConfiguration")
                .addModifiers(Modifier.PUBLIC)
                .returns(returnType);
        group.settings().stream()
                .filter(this::usesNioPath)
                .findAny()
                .ifPresent(setting -> builder.addParameter(ParameterSpec
                        .builder(ClassName.get(String.class), "projectBaseDirectory", Modifier.FINAL).build()));
        final var configBuilder = CodeBlock.builder()
                .add("return $T.builder()\n", returnType);
        group.settings().stream()
                .map(this::fieldConfiguration)
                .forEach(configBuilder::add);
        configBuilder.add(".build()");
        return builder.addStatement(configBuilder.build()).build();
    }

    protected final CodeBlock fieldConfiguration(final ConfigurationSetting setting) {
        if (usesResultRowConverter(setting)) {
            return CodeBlock.builder()
                    .add(".set$L($L())\n", Strings.upperCase(setting.name()), "createRowConverter")
                    .build();
        }
        if (usesResultRowConverters(setting)) {
            return CodeBlock.builder()
                    .add(".set$L($L())\n", Strings.upperCase(setting.name()), "createRowConverters")
                    .build();
        }
        if (usesNioPath(setting)) {
            return CodeBlock.builder()
                    .add(".set$L($T.get($L, $L))\n", Strings.upperCase(setting.name()),
                            Paths.class, "projectBaseDirectory", setting.name())
                    .build();
        }
        if (usesCharset(setting)) {
            return CodeBlock.builder()
                    .add(".set$L($T.forName($L))\n", Strings.upperCase(setting.name()), Charset.class, setting.name())
                    .build();
        }
        return CodeBlock.of(".set$L($L)\n", Strings.upperCase(setting.name()), setting.name());
    }

}
