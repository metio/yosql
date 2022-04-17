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
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
                    .add(".set$L($T.of($L($L)))\n", Strings.upperCase(setting.name()),
                            Optional.class, "createRowConverter", setting.name())
                    .build();
        }
        if (usesResultRowConverters(setting)) {
            return CodeBlock.builder()
                    .add(".set$L($L())\n", Strings.upperCase(setting.name()), "createRowConverters")
                    .build();
        }
        if (usesNioPath(setting)) {
            return CodeBlock.builder()
                    .add(".set$L($T.get($L, $L))\n", Strings.upperCase(setting.name()), Paths.class,
                            "projectBaseDirectory", setting.name())
                    .build();
        }
        if (usesCharset(setting)) {
            return CodeBlock.builder()
                    .add(".set$L($T.forName($L))\n", Strings.upperCase(setting.name()), Charset.class, setting.name())
                    .build();
        }
        return CodeBlock.of(".set$L($L)\n", Strings.upperCase(setting.name()), setting.name());
    }

    protected final List<MethodSpec> resultRowConverters(final ConfigurationGroup group) {
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
                        .add("return $T.ofNullable(rowConverters)", Stream.class)
                        .add("$>$>\n.flatMap($T::stream)", List.class)
                        .add("\n.map($T::strip)", String.class)
                        .add("\n.filter($T.not($T::isBlank))", Predicate.class, Strings.class)
                        .add("\n.map(this::createRowConverter)")
                        .add("\n.filter($T::nonNull)", Objects.class)
                        .add("\n.collect($T.toList())", Collectors.class)
                        .build())
                .build();
    }

    private MethodSpec createRowConverter(final Modifier... modifiers) {
        return MethodSpec.methodBuilder("createRowConverter")
                .addModifiers(modifiers)
                .returns(ResultRowConverter.class)
                .addParameter(String.class, "converterDefinition", Modifier.FINAL)
                .addStatement(CodeBlock.builder()
                        .add("return $T.ofNullable(converterDefinition)", Optional.class)
                        .add("$>$>\n.map($T::strip)", String.class)
                        .add("\n.filter($T.not($T::isBlank))", Predicate.class, Strings.class)
                        .add("\n.map(value -> value.split($S))", ":")
                        .add("\n.map(values -> $T.builder()", ResultRowConverter.class)
                        .add("$>\n.setAlias(values[0])")
                        .add("\n.setConverterType(values[1])")
                        .add("\n.setMethodName(values[2])")
                        .add("\n.setResultType(values[3])$<")
                        .add("\n.build())")
                        .add("\n.orElse(ResultRowConverter.builder()")
                        .add("$>\n.setAlias($S)", "resultRow")
                        .add("\n.setConverterType(utilityPackageName + $S)", ".ToResultRowConverter")
                        .add("\n.setMethodName($S)", "apply")
                        .add("\n.setResultType(utilityPackageName + $S + resultRowClassName)", ".")
                        .add("\n.build())")
                        .build())
                .build();
    }

}
