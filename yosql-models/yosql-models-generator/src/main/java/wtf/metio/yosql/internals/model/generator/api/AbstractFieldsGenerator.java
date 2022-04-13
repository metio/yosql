/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.model.generator.api;

import com.squareup.javapoet.*;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import javax.lang.model.element.Modifier;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractFieldsGenerator implements Generator {

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
                .ifPresent(s -> builder
                        .addParameter(ParameterSpec.builder(TypicalTypes.STRING, "projectBaseDirectory", Modifier.FINAL)
                                .build()));
        final var configBuilder = CodeBlock.builder()
                .add("return $T.builder()\n", returnType);
        group.settings().stream()
                .map(this::fieldConfiguration)
                .forEach(configBuilder::add);
        configBuilder.add(".build()");
        return builder.addStatement(configBuilder.build()).build();
    }

    private CodeBlock fieldConfiguration(final ConfigurationSetting setting) {
        if (usesResultRowConverter(setting)) {
            return CodeBlock.builder()
                    .add(".set$L($T.ofNullable($L($L)))\n", Strings.upperCase(setting.name()), Optional.class, "createRowConverter", setting.name())
                    .build();
        }
        if (usesResultRowConverters(setting)) {
            return CodeBlock.builder()
                    .add(".set$L($L())\n", Strings.upperCase(setting.name()), "createRowConverters")
                    .build();
        }
        if (usesNioPath(setting)) {
            return CodeBlock.builder()
                    .add(".set$L($T.get($L, $L))\n", Strings.upperCase(setting.name()), Paths.class, "projectBaseDirectory", setting.name())
                    .build();
        }
        if (usesCharset(setting)) {
            return CodeBlock.builder()
                    .add(".set$L($T.forName($L))\n", Strings.upperCase(setting.name()), Charset.class, setting.name())
                    .build();
        }
        return CodeBlock.of(".set$L($L)\n", Strings.upperCase(setting.name()), setting.name());
    }

    protected final List<MethodSpec> defaultConverters(final ConfigurationGroup group) {
        return group.settings().stream()
                .filter(this::resultRowConverter)
                .findFirst()
                .map(setting -> List.of(createRowConverters(Modifier.PRIVATE), createRowConverter(Modifier.PRIVATE)))
                .orElse(Collections.emptyList());
    }

    private MethodSpec createRowConverters(final Modifier... modifiers) {
        return MethodSpec.methodBuilder("createRowConverters")
                .addModifiers(modifiers)
                .returns(TypicalTypes.listOf(ResultRowConverter.class))
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable(userTypes)\n", Stream.class)
                        .add("\t\t.flatMap($T::stream)\n", List.class)
                        .add("\t\t.map(this::createRowConverter)\n")
                        .add("\t\t.filter($T::nonNull)\n", Objects.class)
                        .add("\t\t.collect($T.toList())", Collectors.class)
                        .build())
                .build();
    }

    private MethodSpec createRowConverter(final Modifier... modifiers) {
        return MethodSpec.methodBuilder("createRowConverter")
                .addModifiers(modifiers)
                .returns(ResultRowConverter.class)
                .addParameter(String.class, "converterDefinition", Modifier.FINAL)
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable(converterDefinition)\n", Optional.class)
                        .add("\t\t.map($T::strip)\n", String.class)
                        .add("\t\t.filter($T.not($T::isBlank))\n", Predicate.class, Strings.class)
                        .add("\t\t.map(value -> value.split($S))\n", ":")
                        .add("\t\t.map(values -> $T.builder()\n", ResultRowConverter.class)
                        .add("\t\t\t.setAlias(values[0])\n")
                        .add("\t\t\t.setConverterType(values[1])\n")
                        .add("\t\t\t.setMethodName(values[2])\n")
                        .add("\t\t\t.setResultType(values[3])\n")
                        .add("\t\t\t.build())\n")
                        .add("\t\t.orElse(ResultRowConverter.builder()\n")
                        .add("\t\t\t.setAlias($S)\n", "resultRow")
                        .add("\t\t\t.setConverterType(utilityPackageName + $S)\n", ".ToResultRowConverter")
                        .add("\t\t\t.setMethodName($S)\n", "asUserType")
                        .add("\t\t\t.setResultType(utilityPackageName + $S + resultRowClassName)\n", ".")
                        .add("\t\t\t.build())")
                        .build())
                .build();
    }

}
